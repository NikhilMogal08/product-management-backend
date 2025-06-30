package com.data.service;

import com.data.dto.LoginRequest;
import com.data.dto.RegisterRequest;
import com.data.entity.User;
import com.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;
    
    private Map<String, String> otpStorage = new HashMap<>();

    public User register(RegisterRequest request) {
        User user = new User(null, request.getName(), request.getEmail(), request.getPassword(), request.getRole(),request.getMobileNumber(),request.getAddress());
        return userRepository.save(user);
    }

    public User login(LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .filter(u -> u.getPassword().equals(request.getPassword()))
                .orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setMobileNumber(updatedUser.getMobileNumber());
            existingUser.setAddress(updatedUser.getAddress());
            return userRepository.save(existingUser);
        });
    }
    
    public String sendOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
        otpStorage.put(email, otp);
        emailService.sendOtp(email, otp);
        return "OTP sent to " + email;
    }

    public boolean verifyOtp(String email, String enteredOtp) {
        return otpStorage.containsKey(email) && otpStorage.get(email).equals(enteredOtp);
    }


}
