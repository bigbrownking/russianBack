package org.example.russianlanguage.service;

import org.example.russianlanguage.errors.UnauthorizedException;
import org.example.russianlanguage.model.Proverb;
import org.example.russianlanguage.model.User;
import org.example.russianlanguage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User deleteUser(String id){
        User userToDelete = userRepository.findById(id).get();
        if(userToDelete != null) {
            userRepository.delete(userToDelete);
        }
        else{
            throw new NoSuchElementException();
        }
        return userToDelete;
    }
    public User getUserById(String id){
        User user = userRepository.findById(id).get();
        if(user != null){
            return user;
        }
        else{
            return null;
        }
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User signup(User user){
        user.setLogin(true);
        user.setFavorites(new HashSet<>());
        return userRepository.save(user);
    }

    public User login(User user) {
        User foundUser = userRepository.findUserByPasswordAndUsername(user.getPassword(), user.getUsername());
        if (foundUser != null) {
            foundUser.setLogin(true);
            return foundUser;
        } else {
            throw new UnauthorizedException("Invalid username or password");
        }
    }

    public void addProverbToFavorites(User user, Proverb proverb){
        Set<Proverb> favorites = user.getFavorites();
        favorites.add(proverb);
        user.setFavorites(favorites);
        userRepository.save(user);
    }
    public Set<Proverb> getFavProverbs(User user){
        return user.getFavorites();
    }
    public Proverb deleteProverb(User user, Proverb proverb){
        Set<Proverb> fav = user.getFavorites();
        fav.remove(proverb);
        userRepository.save(user);
        return proverb;
    }
}
