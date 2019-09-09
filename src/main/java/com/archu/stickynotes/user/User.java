package com.archu.stickynotes.user;

import com.archu.stickynotes.role.Role;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Document
@Data
public class User implements UserDetails {
    //#TODO Unique email and username, @Indexed(unique=true) dont' pass second note for the same user.

    @Id
    private String id;

    @Size(min = 4, max = 15, message = "Not valid username.")
    @NotEmpty(message = "Username is required.")
    @Indexed(unique = true)
    private String username;

    @NotEmpty(message = "Email is required.")
    @Size(min = 7, message = "Not valid email.")
    @Email
    @Indexed(unique = true)
    private String email;

    @NotEmpty(message = "Password is required.")
    @Size(min = 7, message = "Not valid password.")
    private String password;

    private Boolean enabled = false;

    @CreatedDate
    private Date createdAt;

    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getCode())).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
