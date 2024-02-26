package org.example.russianlanguage.service;

import org.example.russianlanguage.model.Proverb;
import org.example.russianlanguage.model.User;
import org.example.russianlanguage.repository.ProverbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProverbService {

    private final ProverbRepository proverbRepository;

    @Autowired
    public ProverbService( ProverbRepository proverbRepository) {
        this.proverbRepository = proverbRepository;
    }

    public Proverb addProverb(Proverb proverb){
        return proverbRepository.save(proverb);
    }
    public List<Proverb> getAllProverbs(){
        return proverbRepository.findAll();
    }
    public Proverb getProverb(String _id){
        Proverb proverb = proverbRepository.findById(_id).get();
        if (proverb == null) {
            throw new RuntimeException("Proverb not found");
        }
        return proverb;
    }
    public Proverb deleteProverb(String _id){
        Proverb proverbToDelete = proverbRepository.findById(_id).get();
        proverbRepository.delete(proverbToDelete);

        return proverbToDelete;
    }
    public List<Proverb> getProverbsByCategory(String category){
        if(category == null){
            return proverbRepository.findAll();
        }else {
            return proverbRepository.getProverbsByCategory(category);
        }
    }
    public List<Proverb> getProverbsByWord(String word){
        List<Proverb> result = new ArrayList<>();
        List<Proverb> allProverbs = proverbRepository.findAll();
        for(Proverb proverb : allProverbs){
            String description = proverb.getDescription();
            if(description.toLowerCase().contains(word)){
                result.add(proverb);
            }
        }
        return result;
    }
    public List<Proverb> getProverbsByWord(List<Proverb> proverbs, String word){
        List<Proverb> result = new ArrayList<>();
        for(Proverb proverb : proverbs){
            String description = proverb.getDescription();
            if(description.toLowerCase().contains(word)){
                result.add(proverb);
            }
        }
        return result;
    }
    public Set<String> getCategories(){
        Set<String> categories = new HashSet<>();
        List<Proverb> proverbs = proverbRepository.findAll();
        for(Proverb proverb : proverbs){
            String category = proverb.getCategory();
            categories.add(category);
        }
        return categories;
    }
    public List<Proverb> sortProverbs(List<Proverb> proverbs){
        Collections.sort(proverbs, Comparator.comparing(Proverb::getDescription));
        return proverbs;
    }

}
