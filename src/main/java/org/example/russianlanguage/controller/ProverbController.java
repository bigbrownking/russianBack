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
@RequestMapping("/")
public class ProverbController {
    private ProverbService proverbService;
    private UserService userService;

    @Autowired
    public ProverbController(ProverbService proverbService, UserService userService) {
        this.proverbService = proverbService;
        this.userService = userService;
    }

    @GetMapping("/getProverbs")
    public ResponseEntity<List<Proverb>> getProverbs(){
        return ResponseEntity.status(HttpStatus.OK).body(proverbService.getAllProverbs());

    }

    @PostMapping("/addProverb")
    public ResponseEntity<Map<String, Object>> addProverb(@RequestBody Map<String, Object> params, HttpServletRequest request){
        Map<String, String> proverbParams = (Map<String, String>) params.get("params");
        String category = proverbParams.get("category");
        String description = proverbParams.get("description");
        String meaning = proverbParams.get("meaning");
        Proverb proverb = new Proverb(description, meaning, category);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        Map<String, Object> response = new HashMap<>();
        if(user != null && user.isAdmin()){
            Proverb addedProverb = proverbService.addProverb(proverb);
            response.put("id", addedProverb.get_id());
            response.put("description", addedProverb.getDescription());
            response.put("meaning", addedProverb.getMeaning());
            response.put("category", addedProverb.getCategory());
        }
        return ResponseEntity.ok(response);
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

//    @PostMapping("/category")
//    public ResponseEntity<List<Proverb>> getCategories(){
//
//    }
}
