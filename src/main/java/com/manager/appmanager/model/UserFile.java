package com.manager.appmanager.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@Entity(name = "UserFiles")
@Table(name = "UserFiles")
@Getter
@Setter
@NoArgsConstructor
public class UserFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_user")

    User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_app")
    App app;
    @Column(name = "filename", nullable = false)
    String filename;
}
