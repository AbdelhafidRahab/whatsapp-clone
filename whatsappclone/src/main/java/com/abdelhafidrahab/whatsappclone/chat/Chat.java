package com.abdelhafidrahab.whatsappclone.chat;

import com.abdelhafidrahab.whatsappclone.common.BaseAuditingEntity;
import com.abdelhafidrahab.whatsappclone.message.Message;
import com.abdelhafidrahab.whatsappclone.message.MessageState;
import com.abdelhafidrahab.whatsappclone.message.MessageType;
import com.abdelhafidrahab.whatsappclone.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.UUID;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "chats")
public class Chat extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    @OrderBy("createdDate DESC")
    private List<Message> messages;

    @Transient
    public String getChatName(final String senderId) {
        if (senderId.equals(sender.getId())) {
            return receiver.getFirstName() + " " + receiver.getLastName();
        } else {
            return sender.getFirstName() + " " + sender.getLastName();
        }
    }

    @Transient
    public Long getUnreadMessages(final String senderId) {
        return messages.stream()
                .filter(message -> message.getState() == MessageState.SENT && message.getReceiverId().equals(senderId))
                .count();
    }

    @Transient
    public String getLastMessage() {
        if (messages == null) {
            return null;
        }

        if (messages.isEmpty()) {
            return "";
        }

        Message lastMessage = messages.get(0);

        if (lastMessage.getType() != MessageType.TEXT) {
            return "Attachment";
        }

        return lastMessage.getContent();
    }

    @Transient
    public LocalDateTime getLastMessageTime() {
        if (messages == null || messages.isEmpty()) {
            return null;
        }

        return messages.get(0).getCreatedDate();
    }
}
