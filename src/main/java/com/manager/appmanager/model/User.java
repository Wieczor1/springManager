package com.manager.appmanager.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Users")
@Table(name = "Users")
@Data
public class User { //TODO walidacja
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "AppUsers", joinColumns = @JoinColumn(name = "id_user"), inverseJoinColumns = @JoinColumn(name
            = "id_app"))
    private Set<App> usersApp = new HashSet<>();
//TODO orphanRemoval
    @OneToMany(mappedBy = "user")
    private Set<UserFile> userFiles;

    public void addApp(App app) {
        this.usersApp.add(app);
        app.getAppUsers().add(this);
    }

    public void removeApp(App app) {
        this.usersApp.remove(app);
        app.getAppUsers().remove(this);
    }



}
