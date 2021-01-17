package com.manager.appmanager.service;

import com.manager.appmanager.model.*;
import com.manager.appmanager.repository.*;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ImportService {
    private final UserRepository userRepository;
    private final AppRepository appRepository;
    private final LocationRepository locationRepository;
    private final UserFileRepository userFileRepository;
    private final ImageDataRepository imageDataRepository;

    public ImportService(UserRepository userRepository, AppRepository appRepository, LocationRepository locationRepository,
                         UserFileRepository userFileRepository, ImageDataRepository imageDataRepository) {
        this.userRepository = userRepository;
        this.appRepository = appRepository;
        this.locationRepository = locationRepository;
        this.userFileRepository = userFileRepository;
        this.imageDataRepository = imageDataRepository;
    }

    public void determineRepositoryAndEntityClassToUseAndImportCsvToDb(MultipartFile file) throws IOException { //TODO tu jest pole do poprawy
        BufferedReader br;
        List<String> result = new ArrayList<>();
        String header = null;
        try {
            InputStream is = file.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            header = br.readLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        System.out.println(header);
        List<String> headersList = List.of(new User().getCsvHeader(), new App().getCsvHeader(),
                new Location().getCsvHeader(), new UserFile().getCsvHeader(), new ImageData().getCsvHeader());


        List<CsvToBean<? extends Exportable>> mappersList = List.of(new User().getCsvToBeanMapper());//, new App().getCsvToBeanMapper(),
//                new Location().getCsvToBeanMapper(), new UserFile().getCsvToBeanMapper(), new ImageData().getCsvToBeanMapper());

        List<JpaRepository<? extends Exportable, Integer>> repositoryList = List.of(userRepository, appRepository,
                 locationRepository, userFileRepository, imageDataRepository);
        System.out.println(repositoryList);

        int index = headersList.indexOf(header);
        if (index < 0) throw new IllegalArgumentException();
        JpaRepository<?, Integer> repository = repositoryList.get(index);
        CsvToBean<?> csvMapper = mappersList.get(index);
        saveFromCsvToDb(file, repository, csvMapper);



    }
    @Transactional
    public void saveFromCsvToDb(MultipartFile file, JpaRepository repository, CsvToBean<?> csvToBean) throws IOException {
        CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));
        csvToBean.setCsvReader(csvReader);
        repository.saveAll(csvToBean.parse());




    }
}
