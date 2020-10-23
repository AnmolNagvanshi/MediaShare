package com.chatmate.social.respositories;

import com.chatmate.social.entity.User;

import java.util.List;

public interface UserRepository {

    Long create(User user);

    User findByEmailAndPassword(String email, String password);

    Integer getCountByEmail(String email);

    User findById(Long userId);

    List<User> findAll();

}
