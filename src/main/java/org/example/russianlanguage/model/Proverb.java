package org.example.russianlanguage.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Document(collection = "proverb")
public class Proverb {
    @Id
    private Integer _id;

    @Field("description")
    private String description;

    @Field("meaning")
    private String meaning;

    @Field("category")
    private String category;

}
