package com.manager.appmanager.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserFileKey implements Serializable {
    @Column(name = "id_app")
    int appId;
    @Column(name = "id_user")
    int userId;
}
