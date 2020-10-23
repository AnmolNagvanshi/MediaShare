package com.chatmate.social.service;

import com.chatmate.social.entity.User;

import java.util.List;

public interface UserService {

    User validateUser(String email, String password);

    User registerUser(User user);

    User getUser(Long userId);

    List<User> getAllUsers();

}
