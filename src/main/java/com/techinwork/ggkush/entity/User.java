package com.techinwork.ggkush.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user", schema = "ggkush")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Email
    @Column(name = "email")
    private String email;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Tweet> tweets;
}
