package com.chatmate.social.service;

import com.chatmate.social.entity.User;

import java.util.List;

public interface UserService {

    User validateUser(String email, String password);

    User registerUser(User user);

    User registerUser(String firstName, String lastName,
                      String email, String password);

    User getUser(Integer userId);

    List<User> getAllUsers();

}
