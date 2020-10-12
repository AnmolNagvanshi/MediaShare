package com.chatmate.social.service;

import com.chatmate.social.entity.User;
import com.chatmate.social.exceptions.EtAuthException;
import com.chatmate.social.respositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User validateUser(String email, String password) {
        email = email.toLowerCase();
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public User registerUser(User user) {
        return user;
    }

    @Override
    public User registerUser(String firstName, String lastName,
                             String email, String password) {
        // validate email
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        email = email.toLowerCase();
        if(!pattern.matcher(email).matches()) {
            throw new EtAuthException("Invalid Email format");
        }

        // validate if the user already exists
        Integer count = userRepository.getCountByEmail(email);
        if (count > 0) {
            throw new EtAuthException("Email already in use");
        }

        Integer userId = userRepository.create(firstName, lastName, email, password);
        return userRepository.findById(userId);
    }

    @Override
    public User getUser(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
