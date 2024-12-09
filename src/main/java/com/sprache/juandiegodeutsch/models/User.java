package com.sprache.juandiegodeutsch.models;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String email;


    private String username;

    private String password;

    private int streak;

    private LocalDateTime creation_date;


    //Relations

    @OneToMany(mappedBy = "user")
    private List<Deck> decks;


    @OneToMany(mappedBy = "user")
    private List<Progress> progresses;


    /*
    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;

*/
}
