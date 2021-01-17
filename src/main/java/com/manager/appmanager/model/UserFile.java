package com.manager.appmanager.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import lombok.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "UserFiles")
@Table(name = "UserFiles")
@Getter
@Setter
@NoArgsConstructor
public class UserFile implements Exportable, Importable<UserFile> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_user")

    User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_app")
    App app;
    @Column(name = "filename", nullable = false)
    String filename;

    @Override
    public String getCsvString() {
        return id+","+user.getId()+","+app.getId()+","+filename;
    }

    @Override
    public String getCsvHeader() {
        return "id,id_user,id_app,filename";
    }


    @Override
    public CsvToBean<UserFile> getCsvToBeanMapper() {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("id_app", "app");
        mapping.put("id_user", "user");
        mapping.put("filename", "filename");

        HeaderColumnNameTranslateMappingStrategy<UserFile> strategy =
                new HeaderColumnNameTranslateMappingStrategy<UserFile>();
        strategy.setType(UserFile.class);
        strategy.setColumnMapping(mapping);


        CsvToBean<UserFile> csvToBean = new CsvToBean<UserFile>();
        csvToBean.setMappingStrategy(strategy);
        csvToBean.setIgnoreEmptyLines(true);
        return csvToBean;
    }
}
