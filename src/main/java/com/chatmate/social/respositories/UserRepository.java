package com.chatmate.social.respositories;

import com.chatmate.social.entity.User;

import java.util.List;

public interface UserRepository {

    Integer create(String firstName, String lastName, String email, String password);

    User findByEmailAndPassword(String email, String password);

    Integer getCountByEmail(String email);

    User findById(Integer userId);

    List<User> findAll();

}
