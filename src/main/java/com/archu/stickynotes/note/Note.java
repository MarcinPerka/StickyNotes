package com.archu.stickynotes.note;

import com.archu.stickynotes.user.User;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Document
@Data
public class Note {

    @Id
    private String id;


    @NotEmpty(message = "Title is required.")
    @Size(min = 7, message = "Not valid title.")
    private String title;

    @NotEmpty(message = "Note is required.")
    @Size(min = 7, message = "Not valid note.")
    private String note;

    @DBRef
    private User user;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date modifiedAt;

}
