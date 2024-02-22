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
@RequestMapping("/api/proverbs")
public class ProverbController {
    private ProverbService proverbService;
    private UserService userService;

    @Autowired
    public ProverbController(ProverbService proverbService, UserService userService) {
        this.proverbService = proverbService;
        this.userService = userService;
    }

    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK, reason = "OK")
    public List<Proverb> getUsers(){
        return proverbService.getAllProverbs();
    }

    @PostMapping("/add")
    @ResponseStatus(code = HttpStatus.OK, reason = "OK")
    public Proverb addProverb(@RequestBody Proverb proverb){
        return proverbService.addProverb(proverb);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(code = HttpStatus.OK, reason = "OK")
    public Proverb updateProverb(@PathVariable String id, @RequestBody Proverb proverb){
        return proverbService.updateProverb(id, proverb);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProverb(@PathVariable String id, @RequestHeader("userId") String userId){
        User user = userService.getUser(userId);
        if(user != null && user.isAdmin()){
            proverbService.deleteProverb(id);
            return ResponseEntity.status(HttpStatus.OK).body("Proverb deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only admin users can delete proverbs");
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Proverb> getProverb(@PathVariable String id){
        Proverb proverb = proverbService.getProverb(id);
        if(proverb != null){
            return ResponseEntity.status(HttpStatus.OK).body(proverb);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



}
