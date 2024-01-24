package com.example.demo.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;
import com.example.demo.services.UserService;

import lombok.Data;


@RestController
@Data
@RequestMapping("/users")
public class UserController {

	private UserService userService;
	public UserController(UserService userService) {
		this.userService = userService;
	}
@GetMapping
	public List<User> getAllUser(){
	return userService.getAllUsers();
}

@PostMapping
public User createUser(@RequestBody User newUser) {
	return userService.saveOneUser(newUser);
	
}

@GetMapping("/{userId}")
public User getOneUser(@PathVariable Long userId){
	return userService.getOneUserById(userId);
	
	
}

@PutMapping("/{UserId}")
public User updateOneUser(@PathVariable Long userId, @RequestBody User newUser) {
	return userService.updateOneUser(userId, newUser);
}

@DeleteMapping("/{userId}")
private void deleteOneUser(@PathVariable Long userId) {
	userService.deleteOneUser(userId);
}

}
