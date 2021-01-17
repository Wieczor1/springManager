package com.manager.appmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity(name = "Users")
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = User.class)
public class User implements Exportable, Importable<User> { //TODO walidacja
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //TODO to co jest unique to sprawdzac przy rejestarcji
    private int id;
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @JsonIgnore
    @Column(columnDefinition = "varchar(255) default 'default'")
    private String password;
    @Column(columnDefinition = "varchar(255) default 'ROLE_USER'")
    @JsonIgnore
    private String authorities = "ROLE_USER";

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JsonIgnore
    @JoinTable(name = "AppUsers", joinColumns = @JoinColumn(name = "id_user"), inverseJoinColumns = @JoinColumn(name
            = "id_app"))
    private Set<App> usersApp = new HashSet<>();
    //TODO orphanRemoval

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH,
            CascadeType.REMOVE})
    @JsonIgnore
    private Set<UserFile> userFiles;

    public void addApp(App app) {
        this.usersApp.add(app);
        app.getAppUsers().add(this);
    }

    public void removeApp(App app) {
        this.usersApp.remove(app);
        app.getAppUsers().remove(this);
    }


    @Override
    @JsonIgnore
    public String getCsvString() {
        return id+","+firstName+","+lastName+","+email+","+username+","+password+","+authorities;
    }

    @Override
    @JsonIgnore
    public String getCsvHeader() {
        return "id,first_name,last_name,email,username,password,authorities";
    }

    @Override
    @JsonIgnore
    public CsvToBean<User> getCsvToBeanMapper() {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("first_name", "firstName");
        mapping.put("id", "id");
        mapping.put("last_name", "lastName");
        mapping.put("email", "email");
        mapping.put("username", "username");
        mapping.put("password", "password");
        mapping.put("authorities", "authorities");

        HeaderColumnNameTranslateMappingStrategy<User> strategy =
                new HeaderColumnNameTranslateMappingStrategy<User>();
        strategy.setType(User.class);
        strategy.setColumnMapping(mapping);


        CsvToBean<User> csvToBean = new CsvToBean<User>();
        csvToBean.setMappingStrategy(strategy);
        csvToBean.setIgnoreEmptyLines(true);
        return csvToBean;



    }
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(final String password) {
        this.password = password;
    }
}
