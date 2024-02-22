package org.example.russianlanguage.service;

import org.example.russianlanguage.model.Proverb;
import org.example.russianlanguage.model.User;
import org.example.russianlanguage.repository.ProverbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Proverb updateProverb(String id, Proverb proverb){
        Proverb proverbToUpdate = proverbRepository.findById(id).get();
        proverbToUpdate.setDescription(proverb.getDescription());
        proverbToUpdate.setMeaning(proverb.getMeaning());
        proverbToUpdate.setCategory(proverb.getCategory());

        proverbRepository.save(proverbToUpdate);
        return proverbToUpdate;
    }

}
