package org.example.russianlanguage.service;

import org.example.russianlanguage.errors.UnauthorizedException;
import org.example.russianlanguage.model.Proverb;
import org.example.russianlanguage.model.User;
import org.example.russianlanguage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByName(String username){
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }
    public User deleteUser(String username){
        User userToDelete = userRepository.findUserByUsername(username);
        userRepository.delete(userToDelete);

        return userToDelete;
    }

    public User updateUser(String username, User user){
        User userToUpdate = userRepository.findUserByUsername(username);
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(user.getPassword());

        userRepository.save(userToUpdate);
        return userToUpdate;
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User signup(User user){
        user.setLogin(true);
        return userRepository.save(user);
    }

    public User login(User user) {
        User foundUser = userRepository.findUserByPasswordAndUsername(user.getPassword(), user.getUsername());
        if (foundUser != null) {
            return foundUser;
        } else {
            throw new UnauthorizedException("Invalid username or password");
        }
    }

    public void addProverbToFavorites(User user, Proverb proverb){
        List<String> favorites = user.getFavorites();
        favorites.add(proverb.getDescription());
        user.setFavorites(favorites);
        userRepository.save(user);
    }
}
