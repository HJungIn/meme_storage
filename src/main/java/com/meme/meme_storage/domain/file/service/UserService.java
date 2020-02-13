package com.meme.meme_storage.domain.file.service;

import com.meme.meme_storage.domain.account.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public String getUserRole(String email){
        return userRepository.findByEmail(email).get().getRoleKey();
    }
}
