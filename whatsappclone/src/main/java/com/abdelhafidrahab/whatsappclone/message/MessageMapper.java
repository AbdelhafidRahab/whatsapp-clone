package com.abdelhafidrahab.whatsappclone.message;

import org.springframework.stereotype.Service;

import com.abdelhafidrahab.whatsappclone.file.FileUtils;

@Service
public class MessageMapper {
    public MessageResponse toMessageResponse(Message message) {
        return MessageResponse.builder()
            .id(message.getId())
            .content(message.getContent())
            .senderId(message.getSenderId())
            .receiverId(message.getReceiverId())
            .type(message.getType())
            .state(message.getState())
            .createdAt(message.getCreatedDate())
            .media(FileUtils.readFileFromLocation(message.getMediaFilePath()))
        .build();
    }
}
