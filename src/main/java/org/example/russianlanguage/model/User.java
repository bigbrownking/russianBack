package org.example.russianlanguage.model;

import org.springframework.data.annotation.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Document(collection = "users")
public class User {
    @Id
    private String _id;

    @Field("username")
    private String username;

    @Field("password")
    private String password;

    @Field("isAdmin")
    private boolean isAdmin;

    @Field("isLogin")
    private boolean isLogin;

    @Field("favorites")
    private List<String> favorites;
}
