package com.manager.appmanager.service;

import com.manager.appmanager.configuration.StorageProperties;
import com.manager.appmanager.model.Exportable;
import com.manager.appmanager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Service;
import org.zeroturnaround.zip.ZipUtil;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
public class ExportService {
    private final Path exportLocation;
    private final UserRepository userRepository;
    private final AppRepository appRepository;
    private final LocationRepository locationRepository;
    private final UserFileRepository userFileRepository;
    private final ImageDataRepository imageDataRepository;
    private final Path exportZipLocation;


    @Autowired
    public ExportService(StorageProperties properties, UserRepository userRepository, AppRepository appRepository,
                         LocationRepository locationRepository, UserFileRepository userFileRepository, ImageDataRepository imageDataRepository) throws FileNotFoundException {
        this.exportLocation = Paths.get(properties.getExportLocation());
        this.exportZipLocation = Paths.get(properties.getExportZipLocation());
        this.userRepository = userRepository;
        this.appRepository = appRepository;
        this.locationRepository = locationRepository;
        this.userFileRepository = userFileRepository;
        this.imageDataRepository = imageDataRepository;
    }

    public String exportTableAsCsvString(JpaRepository<? extends Exportable, Integer> repository) throws IOException {
        List<? extends Exportable> exportableList = repository.findAll();
        StringBuilder csvString = new StringBuilder();
        boolean appendedHeaders = false;
        for (Exportable e : exportableList) {
            if (!appendedHeaders) {
                csvString.append(e.getCsvHeader()).append(System.lineSeparator());
                appendedHeaders = true;
            }
            csvString.append(e.getCsvString()).append(System.lineSeparator());
        }
        return csvString.toString();
    }

    public void writeStringToFile(String csvString, String filename) throws IOException {
        FileWriter csvWriter = new FileWriter(exportLocation.toString() + File.separator + filename + ".csv");
        csvWriter.append(csvString);
        csvWriter.flush();
        csvWriter.close();
    }



    public void writeAllTablesToCsvFiles(){
            Map<JpaRepository<? extends Exportable, Integer>, String> reposWithFilenameMap = Map.of(userRepository, "users",
                    appRepository, "apps", imageDataRepository, "appimagedata",
                    locationRepository, "applocation",
                    userFileRepository, "userfiles");

            reposWithFilenameMap.forEach((repo, filename) -> {
                try {
                    writeStringToFile(exportTableAsCsvString(repo), filename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    public void zipAllCsvFiles() throws Exception {
        writeAllTablesToCsvFiles();
        zipFile(exportLocation.toFile(), exportZipLocation.toFile(), "export");

    }

    private void zipFile(File toPack, File destination, String fileName) {
        ZipUtil.pack(toPack, new File(destination + File.separator + fileName + ".zip"));
    }
}



