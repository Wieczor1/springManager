package com.manager.appmanager.model;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

public interface Importable<T> {
     CsvToBean<T> getCsvToBeanMapper();
}
