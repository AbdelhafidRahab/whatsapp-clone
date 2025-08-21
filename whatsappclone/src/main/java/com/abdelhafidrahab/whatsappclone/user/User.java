package com.abdelhafidrahab.whatsappclone.user;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.List;

import com.abdelhafidrahab.whatsappclone.chat.Chat;
import com.abdelhafidrahab.whatsappclone.common.BaseAuditingEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseAuditingEntity{
    
    private static final int LAST_ACTIVATE_INTERVAL = 5;

    @Id
    private String id; // we dont use long because the id will come from keyclock as uuid

    private String firstName;
    
    private String lastName;
    
    private String email;
    
    private LocalDateTime lastSeen;

    @OneToMany(mappedBy = "sender")
    private List<Chat> chatsAsSender;

    @OneToMany(mappedBy = "receiver")
    private List<Chat> chatsAsReceiver;

    @Transient
    public boolean isUserOnline() {
        return lastSeen != null && lastSeen.isAfter(LocalDateTime.now().minusMinutes(LAST_ACTIVATE_INTERVAL));
    }
}
