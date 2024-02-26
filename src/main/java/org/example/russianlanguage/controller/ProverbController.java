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
@RequestMapping("/")
public class ProverbController {
    private ProverbService proverbService;
    private UserService userService;

    @Autowired
    public ProverbController(ProverbService proverbService, UserService userService) {
        this.proverbService = proverbService;
        this.userService = userService;
    }

    @PostMapping("/getProverbs")
    public ResponseEntity<List<Proverb>> getProverbs(@RequestBody Map<String, Object> params){
        System.out.println(params);
        Map<String, String> provParams = (Map<String, String>) params.get("query");
        String category = provParams.get("category");
        String search = provParams.get("search");
        

        if(category == null && search == null){
            return ResponseEntity.status(HttpStatus.OK).body(proverbService.getAllProverbs());
        }
        else if(category != null && search == null){
            List<Proverb> proverbsByCategory = proverbService.getProverbsByCategory(category);
            if(proverbsByCategory != null) {
                return ResponseEntity.status(HttpStatus.OK).body(proverbsByCategory);
            } else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        else if(category != null && search != null){
            List<Proverb> proverbsByCategory = proverbService.getProverbsByCategory(category);
            if(proverbsByCategory != null) {
                List<Proverb> proverbsByWord = proverbService.getProverbsByWord(proverbsByCategory, search);
                if(proverbsByWord != null){
                    return ResponseEntity.status(HttpStatus.OK).body(proverbsByWord);
                } else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            } else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        else if(category == null && search != null){
            List<Proverb> proverbsByWord = proverbService.getProverbsByWord(search);
            if(proverbsByWord != null){
                return ResponseEntity.status(HttpStatus.OK).body(proverbsByWord);
            } else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(proverbService.getAllProverbs());
    }

    @GetMapping("/getCategories")
    public ResponseEntity<Set<String>> getCategories(){
        return ResponseEntity.status(HttpStatus.OK).body(proverbService.getCategories());
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
    @PostMapping("/deleteProverb")
    public ResponseEntity<Proverb> deleteProverb(@RequestBody Map<String, Object> params, HttpServletRequest request){
        Map<String, Object> proverbParams = (Map<String, Object>) params.get("params");
        String proverbId = (String) proverbParams.get("proverbId");
        Boolean isProfile = (Boolean) proverbParams.get("isProfile");
        String userId = (String) proverbParams.get("id");
        HttpSession session = request.getSession();
        User userGlob = (User) session.getAttribute("user");
        if(isProfile){
            User user = userService.getUserById(userId);
            if(user != null){
                Set<Proverb> proverbs = user.getFavorites();
                for(Proverb proverb : proverbs){
                    if(proverb.get_id().equals(proverbId)){
                        userService.deleteProverb(user, proverb);
                        userService.deleteProverb(userGlob, proverb);
                        return ResponseEntity.status(HttpStatus.OK).body(proverb);
                    }
                }
            }
        }
        else{
            Proverb proverb = proverbService.deleteProverb(proverbId);
            return ResponseEntity.status(HttpStatus.OK).body(proverb);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}