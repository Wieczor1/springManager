package com.manager.appmanager.model;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "AppLocation")
@Table(name = "AppLocation")
@Getter
@Setter
@NoArgsConstructor
public class Location implements Exportable, Importable<Location> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
    @JoinColumn(name = "id_app")

//    @OnDelete(action = OnDeleteAction.CASCADE) TODO KASKADOWE USUWANIE WSZEDZIE ZEBY DANE NIE WISIALY!!!!!!!!!!!!!! wazne
    private App app;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "street", nullable = false)
    private String street;
    @Column(name = "street_number", nullable = false)
    private int streetNumber;
    @Column(name = "country", nullable = false)
    private String country;


    @Override
    public String getCsvString() {
        return id+","+app.getId()+","+city+","+streetNumber+","+country;
    }

    @Override
    public String getCsvHeader() {
        return "id,id_app.city,street,street_number,country";
    }

    @Override
    public CsvToBean<Location> getCsvToBeanMapper() {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("id", "id");
        mapping.put("id_app", "app");
        mapping.put("street", "street");
        mapping.put("street_number", "streetNumber");
        mapping.put("country", "country");

        HeaderColumnNameTranslateMappingStrategy<Location> strategy =
                new HeaderColumnNameTranslateMappingStrategy<Location>();
        strategy.setType(Location.class);
        strategy.setColumnMapping(mapping);


        CsvToBean<Location> csvToBean = new CsvToBean<Location>();
        csvToBean.setMappingStrategy(strategy);
        csvToBean.setIgnoreEmptyLines(true);
        return csvToBean;
    }
}
