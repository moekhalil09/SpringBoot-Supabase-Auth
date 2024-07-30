package com.moe.springbootsupabaseauth.service;

import com.moe.springbootsupabaseauth.model.User;
import com.moe.springbootsupabaseauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    private final PasswordEncoder passwordEncoder;

    public User registerUser(String username, String email,String password) {
        User registredUser = new User();
        registredUser.setUsername(username);
        registredUser.setPassword(passwordEncoder.encode((password)));
        registredUser.setEnabled(false);
        registredUser.setCreatedAt(LocalDateTime.now());
        registredUser.setVerificationCode(UUID.randomUUID().toString());
        registredUser.setVerificationExpiration(LocalDateTime.now().plusHours(24));
        registredUser.setEmail(email);
        userRepository.save(registredUser);
        sendVerification(registredUser);
        return registredUser;
    }
    public Boolean login(String username, String password) {
        User user = userRepository.findByUsername(username);
        return passwordEncoder.matches(password, user.getPassword());
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private void sendVerification(User registredUser) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(registredUser.getEmail());
        message.setSubject(registredUser.getUsername());
        message.setText("Verify your email address, click the link: http://localhost:8080/auth/verify?code=" + registredUser.getVerificationCode());
        mailSender.send(message);
    }
    public Boolean verifyUser(String code){
        User user = userRepository.findAll().stream()
                .filter(u -> code.equals(u.getVerificationCode())
                        && LocalDateTime.now().isBefore(u.getVerificationExpiration()))
                .findFirst()
                .orElse(null);
        if(user != null ){
            user.setEnabled(true);
            user.setVerificationCode(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

}
