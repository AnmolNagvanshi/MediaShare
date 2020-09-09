package com.chatmate.social.dao;

import com.chatmate.social.entity.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {

}
