package com.manager.appmanager.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Apps")
@Table(name = "Apps")
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id", scope = App.class)
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

    @ManyToMany(mappedBy = "usersApp", cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<User> appUsers = new HashSet<>();

    @OneToMany(mappedBy = "app", cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
    private Set<UserFile> userFilesInApp = new HashSet<>();

    @OneToMany(mappedBy="app", cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Location> locations = new HashSet<>();

    @OneToMany(mappedBy="app", cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE}, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<ImageData> images = new HashSet<>();

    @PreRemove
    private void removeAppFromUsers(){
        appUsers.forEach(user -> user.getUsersApp().remove(this));
    }

    public void addLocation(Location location) {
        this.locations.add(location);
        location.setApp(this);
    }

    public void removeLocation(Location location) {
        this.locations.remove(location);
        location.setApp(null);
    }

    public void addUser(User user) {
        this.appUsers.add(user);
        user.getUsersApp().add(this);
    }

    public void removeUser(User user) {
        this.appUsers.remove(user);
        user.getUsersApp().remove(this);
    }



}
