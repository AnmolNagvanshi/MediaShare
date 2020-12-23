package com.chatmate.social.service;

import com.chatmate.social.entity.User;
import com.chatmate.social.exceptions.UserAuthException;
import com.chatmate.social.respositories.UserRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
//@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepositoryJPA userRepository;

    @Autowired
    public UserServiceImpl(UserRepositoryJPA userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User validateUser(String email, String password) {
        email = email.toLowerCase();
        Optional<User> user = userRepository.findByEmailAndPassword(email, password);
        if (user.isEmpty())
            throw new UserAuthException("Invalid credentials");
        return user.get();
    }

    @Override
    public User registerUser(User user) {
        String email = user.getEmail();

        // validate email
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        email = email.toLowerCase();
        if(!pattern.matcher(email).matches()) {
            throw new UserAuthException("Invalid Email format");
        }

        // validate if the user already exists
        if (userRepository.existsByEmail(email)) {
            throw new UserAuthException("Email already in use");
        }

        user.setActive(true);
        user.setDateJoined(new Date());
        return userRepository.save(user);
    }

    @Override
    public User getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new UserAuthException("user id does not exist");
        return user.get();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
