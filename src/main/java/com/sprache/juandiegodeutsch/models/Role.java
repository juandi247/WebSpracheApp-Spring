package com.sprache.juandiegodeutsch.models;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String role_type;

    @OneToMany(mappedBy = "role")
    private List<User> users;
}
