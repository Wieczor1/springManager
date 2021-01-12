package com.manager.appmanager.model;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "UserFiles")
@Table(name = "UserFiles")
@Data
public class UserFile {
    @EmbeddedId
    UserFileKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "id_user")
    User user;

    @ManyToOne
    @MapsId("appId")
    @JoinColumn(name = "id_app")
    App app;
    @Column(name = "filename", nullable = false)
    String filename;
}
