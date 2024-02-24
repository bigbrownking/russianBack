package org.example.russianlanguage.model;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Document(collection = "proverbs")
public class Proverb {
    @Id
    private String _id;

    @Field("description")
    private String description;

    @Field("meaning")
    private String meaning;

    @Field("category")
    private String category;

    public Proverb(String description, String meaning, String category){
        this.description = description;
        this.meaning = meaning;
        this.category = category;
    }

}
