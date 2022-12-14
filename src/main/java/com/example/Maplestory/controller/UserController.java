package com.example.Maplestory.controller;

import com.example.Maplestory.entity.User;
import com.example.Maplestory.repository.ProfileRepository;
import com.example.Maplestory.repository.UserRepository;
import com.example.Maplestory.response.HttpCustomResponse;
import com.example.Maplestory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileRepository profileRepository;

    @GetMapping("/getUsers")
    public List<User> getUsers() {
        return userRepository.findAll();
    }


    @DeleteMapping("/delete")
    @Transactional
    public ResponseEntity<HttpCustomResponse> deleteUser(RequestEntity<User> request) {
        User user = request.getBody();
        assert user != null;
        if(userService.findByName(user.getName()) != null) {
            if(profileRepository.findByAuthName(user.getName()) != null) {
                profileRepository.deleteByAuthName(user.getName());
            }
            userRepository.deleteByName(user.getName());
            return ResponseEntity.status(200).body(new HttpCustomResponse(200, user.getName() + " has been deleted"));
        } else {
            return ResponseEntity.status(400).body(new HttpCustomResponse(400, user.getName() + " does not exist"));
        }
    }

    @PostMapping("/updateMembership")
    public ResponseEntity<HttpCustomResponse> updateMembership(@RequestParam(value = "name") String name) {
        User user = userService.findByName(name);
        if(user != null) {
            user.setMembership(null);
            userRepository.save(user);
            return ResponseEntity.status(200).body(new HttpCustomResponse(200, "Membership has been updated "));
        }

        return ResponseEntity.status(400).body(new HttpCustomResponse(400, name + " does not exist"));

    }
}
