package com.chatmate.social.rest;

import com.chatmate.social.dao.UserRepository;
import com.chatmate.social.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserRepository userRepository;
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        return userRepository.findAll();
    }


    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow();
    }

    @PostMapping("/users")
    public User addEmployee(@RequestBody User user) {
        // In case, any client passes an id in JSON, set id to 0
        // this is to force a save of new item, instead of update
        user.setId(0);
        user.setActive(true);
        user.setDateJoined(LocalDate.now());

        userRepository.save(user);
        return user;
    }

    @PutMapping("/users")
    public User updateEmployee(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }
}
