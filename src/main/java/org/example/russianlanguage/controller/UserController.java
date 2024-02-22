package org.example.russianlanguage.controller;

import org.example.russianlanguage.model.Proverb;
import org.example.russianlanguage.model.User;
import org.example.russianlanguage.service.ProverbService;
import org.example.russianlanguage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;
    private ProverbService proverbService;

    @Autowired
    public UserController(UserService userService, ProverbService proverbService) {
        this.userService = userService;
        this.proverbService = proverbService;
    }

    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK, reason = "OK")
    public List<User> getUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user){
        User loggedInUser = userService.login(user);
        if(loggedInUser != null){
            return ResponseEntity.status(HttpStatus.OK).body(loggedInUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user){
        User registeredUser = userService.signup(user);
        if(registeredUser != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/update/{_id}")
    public User updateUser(@PathVariable String _id,@RequestBody User user){
        return userService.updateUser(_id, user);
    }

    @DeleteMapping("/delete/{_id}")
    public User deleteUser(@PathVariable String _id){
        return userService.deleteUser(_id);
    }

    @GetMapping("/get/{_id}")
    public User getUser(@PathVariable String _id){
        return userService.getUser(_id);
    }

    @PostMapping("/addFavorite/{userId}/{proverbId}")
    public ResponseEntity<User> addProverbToFavorites(@PathVariable String userId, @PathVariable String proverbId){
        User user = userService.getUser(userId);
        Proverb proverb = proverbService.getProverb(proverbId);
        if(user != null && proverb != null){
            userService.addProverbToFavorites(user, proverb);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}

