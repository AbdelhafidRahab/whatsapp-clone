package com.abdelhafidrahab.whatsappclone.message;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.abdelhafidrahab.whatsappclone.chat.Chat;
import com.abdelhafidrahab.whatsappclone.chat.ChatRepository;
import com.abdelhafidrahab.whatsappclone.file.FileService;
import com.abdelhafidrahab.whatsappclone.file.FileUtils;
import com.abdelhafidrahab.whatsappclone.notification.Notification;
import com.abdelhafidrahab.whatsappclone.notification.NotificationService;
import com.abdelhafidrahab.whatsappclone.notification.NotificationType;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper mapper;
    private final FileService fileService;
    private final NotificationService notificationService;

    public void saveMessage(MessageRequest messageRequest) {
        Chat chat = chatRepository.findById(messageRequest.getChatId()).orElseThrow(
            () -> new EntityNotFoundException("Chat Not Found")
        );

        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setChat(chat);
        message.setSenderId(messageRequest.getSenderId());
        message.setReceiverId(messageRequest.getReveiverId());
        message.setType(messageRequest.getType());
        message.setState(MessageState.SENT);

        messageRepository.save(message);

        Notification notification = Notification.builder()
            .chatId(chat.getId())
            .messageType(messageRequest.getType())
            .content(messageRequest.getContent())
            .senderId(messageRequest.getSenderId())
            .receiverId(messageRequest.getReveiverId())
            .type(NotificationType.MESSAGE)
            .chatName(chat.getChatName(message.getSenderId()))
            .build();

        notificationService.sendNotification(messageRequest.getReveiverId(), notification);
    }

    public List<MessageResponse> findChatMessages(String chatId) {
        return messageRepository.findMessageByChatId(chatId).stream().map(mapper::toMessageResponse).toList();
    }

    @Transactional
    public void setMessagesToSeen(String chatId, Authentication authentication) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(
            () -> new EntityNotFoundException("Chat Not Found")
        );

        final String receiverId = getReceiverId(chat, authentication);

        messageRepository.setMessagesToSeenByChatId(chatId, MessageState.SEEN);

        Notification notification = Notification.builder()
            .chatId(chat.getId())
            .senderId(getSenderId(chat, authentication))
            .receiverId(receiverId)
            .type(NotificationType.SEEN)
            .build();

        notificationService.sendNotification(receiverId, notification);
    }

    public void uploadMediaMessage(String chatId, MultipartFile file, Authentication authentication) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(
            () -> new EntityNotFoundException("Chat Not Found")
        );

        final String senderId = getSenderId(chat, authentication);
        final String receiverId = getReceiverId(chat, authentication);

        final String filePath = fileService.saveFile(file, senderId);

        Message message = new Message();
        message.setChat(chat);
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setType(MessageType.IMAGE);
        message.setState(MessageState.SENT);
        message.setMediaFilePath(filePath);

        messageRepository.save(message);

        Notification notification = Notification.builder()
            .chatId(chat.getId())
            .messageType(MessageType.IMAGE)
            .senderId(senderId)
            .receiverId(receiverId)
            .type(NotificationType.IMAGE)
            .media(FileUtils.readFileFromLocation(filePath))
            .build();

        notificationService.sendNotification(receiverId, notification);
    }

    private String getSenderId(Chat chat, Authentication authentication) {
        if (chat.getSender().getId().equals(authentication.getName())) {
            return chat.getSender().getId();
        }
        return chat.getReceiver().getId();
    }

    private String getReceiverId(Chat chat, Authentication authentication) {
        if (chat.getSender().getId().equals(authentication.getName())) {
            return chat.getReceiver().getId();
        }

        return chat.getSender().getId();
    }
}
