package com.archu.stickynotes.role;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "roles")
@Data
public class Role {

    @Id
    private String id;

    @NotNull
    private String code;

    @NotNull
    private String label;

}
