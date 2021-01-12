package com.manager.appmanager.model;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity(name = "AppLocation")
@Table(name = "AppLocation")
@Data
public class Location {
    @Id
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_id")
//    @OnDelete(action = OnDeleteAction.CASCADE) TODO
    private App app;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "street", nullable = false)
    private String street;
    @Column(name = "street_number", nullable = false)
    private int streetNumber;
    @Column(name = "country", nullable = false)
    private String country;


}
