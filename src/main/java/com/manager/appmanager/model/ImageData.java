package com.manager.appmanager.model;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "AppImageData")
@Table(name = "AppImageData")
@Data
public class ImageData {
    @Id
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_id")
    private App app;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;


}
