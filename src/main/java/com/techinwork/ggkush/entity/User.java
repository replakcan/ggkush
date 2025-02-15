package com.techinwork.ggkush.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user", schema = "ggkush")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 45)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 45)
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 45)
    @Column(name = "nick_name")
    private String nickName;

    @NotNull
    @Min(value = 18)
    @Column(name = "age")
    private Integer age;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", schema = "ggkush",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Tweet> tweets;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }
}
