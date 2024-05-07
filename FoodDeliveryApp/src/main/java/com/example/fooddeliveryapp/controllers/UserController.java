package com.example.fooddeliveryapp.controllers;

import com.example.fooddeliveryapp.entities.User;
import com.example.fooddeliveryapp.exceptions.ResourceNotFoundException;
import com.example.fooddeliveryapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAllUsersPageable")
    public ResponseEntity<Page<User>> getAllUsersPageable(@PageableDefault(size = 10, sort = "id") Pageable pageable) {

//        return ResponseEntity.ok(userService.findAllUsers());
        Page<User> users = userService.findAllUsers(pageable);
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("List of Users is empty.");
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {

//        return ResponseEntity.ok(userService.findAllUsers());
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("List of Users is empty.");
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
//        return userService.findUserById(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
        User user = userService.findUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find User with this id: " + id.toString() + "\n"));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.saveUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
//        return userService.findUserById(id)
//                .map(user -> {
//                    updatedUser.setId(id);
//                    User savedUser = userService.saveUser(updatedUser);
//                    return ResponseEntity.ok(savedUser);
//                })
//                .orElseGet(() -> ResponseEntity.notFound().build());
        User user = userService.findUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find User with this id:  " + id.toString() + "\n"));
        updatedUser.setId(id);
        User savedUser = userService.saveUser(updatedUser);
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        return userService.findUserById(id)
//                .map(user -> {
//                    userService.deleteUser(id);
//                    return ResponseEntity.ok().build();
//                })
//                .orElseGet(() -> ResponseEntity.notFound().build());
        User user = userService.findUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find User with this id: " + id.toString() + "\n"));
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}