package com.example.demo.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Post;


public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findByUserId(Long userId);

	

}
