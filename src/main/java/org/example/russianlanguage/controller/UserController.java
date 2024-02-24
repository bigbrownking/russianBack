package org.example.russianlanguage.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.russianlanguage.model.Proverb;
import org.example.russianlanguage.model.User;
import org.example.russianlanguage.service.ProverbService;
import org.example.russianlanguage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@ResponseBody
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
    public ResponseEntity<User> login(@RequestBody Map<String, Object> params, HttpServletRequest request){
        Map<String, String> userParams = (Map<String, String>) params.get("params");
        String login = userParams.get("login");
        String password = userParams.get("password");
        User user = new User(login, password);
        User loggedInUser = userService.login(user);
        if(loggedInUser != null){
            HttpSession session = request.getSession(); // Get the session from the request
            session.setAttribute("user", loggedInUser); // Set user in session
            return ResponseEntity.status(HttpStatus.OK).body(loggedInUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/reg")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> params) {
        Map<String, String> userParams = (Map<String, String>) params.get("params");
        String login = userParams.get("login");
        String password = userParams.get("password");
        User user = new User(login, password);
        User registeredUser = userService.signup(user);

        Map<String, Object> response = new HashMap<>();
        if (registeredUser != null) {
            response.put("status", 200);
            response.put("id", registeredUser.get_id());
            response.put("login", registeredUser.getUsername());
            response.put("isAdmin", registeredUser.isAdmin());
        } else {
            response.put("status", 400);
        }

        return ResponseEntity.ok(response);
    }
//    @GetMapping("/logout")
//    public User logout(@PathVariable String _id){
//        User userToLogout = userService.getUser(_id);
//
//
//    }

    @PutMapping("/update/{username}")
    public User updateUser(@PathVariable String username, @RequestBody User user){
        return userService.updateUser(username, user);
    }


    @DeleteMapping("/delete/{username}")
    public User deleteUser(@PathVariable String username){
        return userService.deleteUser(username);
    }


    @GetMapping("/get/{username}")
    public User getUser(@PathVariable String username){
        return userService.getUserByName(username);
    }

    @PostMapping("/addFavorite")
    public ResponseEntity<User> addProverbToFavorites(@RequestParam String user_id, @RequestBody String proverb_id){
        User user = userService.getUserByName(user_id);
        Proverb proverb = proverbService.getProverb(proverb_id);
        if(user != null){
            userService.addProverbToFavorites(user, proverb);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}

