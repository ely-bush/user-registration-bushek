package com.elkabani.userregistration;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public UserService(UserRepository userRepository,
                       NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public boolean registerUser(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            System.out.println("Registration failed: a user with an email address is required.");
            return false;
        }

        User existing = userRepository.findByEmail(user.getEmail());
        if (existing != null) {
            System.out.println("Duplicate registration rejected: "
                    + user.getEmail() + " is already registered to " + existing.getName());
            notificationService.send(
                    "An account already exists for this email. Registration was not repeated.",
                    user.getEmail());
            return false;
        }

        userRepository.save(user);
        notificationService.send(
                "Welcome, " + user.getName() + "! Your registration is confirmed.",
                user.getEmail());
        System.out.println("Registration successful for " + user.getEmail());
        return true;
    }
}