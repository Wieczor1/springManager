package com.manager.appmanager.model;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import lombok.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "AppImageData")
@Table(name = "AppImageData")
@Getter
@Setter
@NoArgsConstructor
public class ImageData implements Exportable, Importable<ImageData> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

   @ManyToOne(fetch = FetchType.LAZY, optional = false)//, // cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
    @JoinColumn(name = "id_app")
    private App app;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;


    @Override
    public String getCsvString() {
        return id+","+app.getId()+","+imageUrl;
    }

    @Override
    public String getCsvHeader() {
        return "id,id_app,image_url";
    }

    @Override
    public CsvToBean<ImageData> getCsvToBeanMapper() {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("id", "id");
        mapping.put("id_app", "app");
        mapping.put("image_url", "imageUrl");

        HeaderColumnNameTranslateMappingStrategy<ImageData> strategy =
                new HeaderColumnNameTranslateMappingStrategy<ImageData>();
        strategy.setType(ImageData.class);
        strategy.setColumnMapping(mapping);


        CsvToBean<ImageData> csvToBean = new CsvToBean<ImageData>();
        csvToBean.setMappingStrategy(strategy);
        csvToBean.setIgnoreEmptyLines(true);
        return csvToBean;
    }
}
