package com.manager.appmanager.model;


import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Apps")
@Table(name = "Apps")
@Data
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "domain", nullable = false)
    private String domain;
    @Column(name = "version", nullable = false)
    private String version;

    @ManyToMany(mappedBy = "usersApp")
    private Set<User> appUsers = new HashSet<>();

    @OneToMany(mappedBy = "app")
    private Set<UserFile> userFilesInApp = new HashSet<>();

    @OneToMany(mappedBy="app")
    private Set<Location> locations = new HashSet<>();

    @OneToMany(mappedBy="app")
    private Set<ImageData> images = new HashSet<>();

}
