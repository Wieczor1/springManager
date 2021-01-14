package com.manager.appmanager.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "AppImageData")
@Table(name = "AppImageData")
@Getter
@Setter
@NoArgsConstructor
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_id")

    private App app;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;


}
