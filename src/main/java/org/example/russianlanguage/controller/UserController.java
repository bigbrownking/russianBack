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
import java.util.Set;

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


    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }


    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Map<String, String> userParams = (Map<String, String>) params.get("params");
        String login = userParams.get("login");
        String password = userParams.get("password");
        User user = new User(login, password);

        User loggedInUser = userService.login(user);
        if (loggedInUser != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).body(loggedInUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/reg")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Map<String, String> userParams = (Map<String, String>) params.get("params");
        String login = userParams.get("login");
        String password = userParams.get("password");
        User user = new User(login, password);
        User registeredUser = userService.signup(user);

        Map<String, Object> response = new HashMap<>();
        if (registeredUser != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", registeredUser);
            response.put("status", 200);
            response.put("id", registeredUser.get_id());
            response.put("login", registeredUser.getUsername());
            response.put("isAdmin", registeredUser.isAdmin());
        } else {
            response.put("status", 400);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/logOut")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user != null){
            user.setLogin(false);
        }
        request.getSession().invalidate();
        return ResponseEntity.ok("User has been logged out");
    }

    @PostMapping("/addToFav")
    public ResponseEntity<User> addProverbToFavorites(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Map<String, Object> userParams = (Map<String, Object>) params.get("params");
        String proverbId = (String) userParams.get("proverbId");
        String userId = (String) userParams.get("userId");
        User user = userService.getUserById(userId);
        HttpSession session = request.getSession();
        User userGlob = (User) session.getAttribute("user");
        Proverb proverb = proverbService.getProverb(proverbId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else if (proverb == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            userService.addProverbToFavorites(userGlob, proverb);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }

    @PostMapping("/getFavProverbs")
    public ResponseEntity<Set<Proverb>> getFavProverbs(@RequestBody Map<String, Object> params, HttpServletRequest request){
        Map<String, Object> userParams = (Map<String, Object>) params.get("params");
        String userId = (String) userParams.get("id");
        User user = userService.getUserById(userId);
        HttpSession session = request.getSession();
        User userGlob = (User) session.getAttribute("user");
        if(user != null){
            Set<Proverb> favProverbs = userService.getFavProverbs(userGlob);
            return ResponseEntity.status(HttpStatus.OK).body(favProverbs);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PostMapping("/deleteUser")
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestBody Map<String, Object> params) {
        String id = (String) params.get("id");
        User userToDelete = userService.deleteUser(id);
        Map<String, Object> response = new HashMap<>();
        if (userToDelete != null) {
            response.put("status", 200);
            response.put("message", "Пользователь был удален");
        } else {
            response.put("status", 400);
        }
        return ResponseEntity.ok(response);
    }
}

