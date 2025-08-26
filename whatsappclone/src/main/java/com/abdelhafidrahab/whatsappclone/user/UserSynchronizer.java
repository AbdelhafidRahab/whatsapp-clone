package com.abdelhafidrahab.whatsappclone.user;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSynchronizer {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void synchronizeWithIdp(Jwt token) {
        log.info("Synchronizing user with IDP");
        getUserEmail(token).ifPresent(userEmail -> {
            log.info("Synchronizing user having email: {}", userEmail);

            Optional<User> userOpt = userRepository.findByEmail(userEmail);
            User user = userMapper.fromTokenAttributes(token.getClaims());
            userOpt.ifPresent(value -> user.setId(userOpt.get().getId()));

            userRepository.save(user);
        });
    }
    
    private Optional<String> getUserEmail(Jwt token) {

        Map<String, Object> attributes = token.getClaims();
        if (attributes.containsKey("email")) {
            return Optional.of(attributes.get("email").toString());
        }

        return Optional.empty();
    }
}
