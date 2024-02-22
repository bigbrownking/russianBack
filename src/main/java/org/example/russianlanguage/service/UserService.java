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

    public User getUser(String _id){
        User user = userRepository.findById(_id).get();
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }
    public User deleteUser(String _id){
        User userToDelete = userRepository.findById(_id).get();
        userRepository.delete(userToDelete);

        return userToDelete;
    }

    public User updateUser(String _id, User user){
        User userToUpdate = userRepository.findById(_id).get();
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(user.getPassword());

        userRepository.save(userToUpdate);
        return userToUpdate;
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User signup(User user){
        return userRepository.save(user);
    }

    public User login(User user) {
        User foundUser = userRepository.findById(user.get_id()).get();
        if (foundUser != null) {
            return foundUser;
        } else {
            throw new UnauthorizedException("Invalid username or password");
        }
    }
    public User addProverbToFavorites(User user, Proverb proverb){
        List<String> favorites = user.getFavorites();
        favorites.add(proverb.getDescription());
        user.setFavorites(favorites);
        return userRepository.save(user);
    }
}
