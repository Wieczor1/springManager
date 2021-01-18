package com.manager.appmanager.controller;

import com.manager.appmanager.exception.NotFoundException;
import com.manager.appmanager.model.ImageData;
import com.manager.appmanager.model.UserFile;
import com.manager.appmanager.repository.ImageDataRepository;
import com.manager.appmanager.repository.UserFileRepository;
import com.manager.appmanager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class FileController {

    private final StorageService storageService;
    private final UserFileService userFileService;
    private final ImageDataService imageDataService;
    private final ImageDataRepository imageDataRepository;
    private final UserFileRepository userFileRepository;
    private final ExportService exportService;
    private final ImportService importService;

    @Autowired
    public FileController(StorageService storageService, UserFileService userFileService, ImageDataService imageDataService, ImageDataRepository imageDataRepository, UserFileRepository userFileRepository, ExportService exportService, ImportService importService) {
        this.storageService = storageService;
        this.userFileService = userFileService;
        this.imageDataService = imageDataService;
        this.imageDataRepository = imageDataRepository;
        this.userFileRepository = userFileRepository;
        this.exportService = exportService;
        this.importService = importService;
    }


    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/files/user/{userId}/app/{appId}")
    public void uploadFileToAppBelongingToUser(@PathVariable int userId, @PathVariable int appId, @RequestParam("file") MultipartFile file) throws NotFoundException {
        storageService.store(file);
        userFileService.addUserFile(userId, appId, storageService.getDownloadUrl(file));


    }

    @GetMapping("/files/export")
    public ResponseEntity<Resource>  exportString() throws Exception {
         exportService.zipAllCsvFiles();
        Resource file = storageService.exportAsResource();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);

    }

    @PostMapping(value = "/files/import", consumes = "multipart/form-data")
    public void uploadMultipart(@RequestParam("file") MultipartFile file) throws IOException {
        importService.determineRepositoryAndEntityClassToUseAndImportCsvToDb(file);
    }


    @DeleteMapping("/files/user/{userId}/app/{appId}/file/{fileId}")
    public void uploadFileToAppBelongingToUser(@PathVariable int userId, @PathVariable int appId, @PathVariable int fileId) throws NotFoundException, IOException {
        UserFile file = userFileRepository.findById(fileId).orElseThrow(() -> new NotFoundException(fileId));
        try{
            storageService.delete(file.getFilename().substring(file.getFilename().lastIndexOf('/')+1));
        }
        catch (NoSuchFileException e){
            e.printStackTrace();

        }
        userFileService.removeUserFile(userId, appId, file);


    }

    @PostMapping("/files/images/app/{appId}")
    public void uploadImageToApp(@PathVariable int appId, @RequestParam("file") MultipartFile file) throws NotFoundException {
        storageService.store(file);
        imageDataService.addImageToApp(appId, storageService.getDownloadUrl(file));

    }

    @PutMapping("/files/images/{imageId}")
    public void editImage(@PathVariable int imageId,  @RequestParam("file") MultipartFile file) throws NotFoundException {
        storageService.store(file);
        imageDataService.editImage(imageId, storageService.getDownloadUrl(file));

    }

    @DeleteMapping("/files/images/{imageId}")
    public void deleteImage(@PathVariable int imageId) throws NotFoundException, IOException {
        ImageData imageData = imageDataRepository.findById(imageId).orElseThrow(() -> new NotFoundException(imageId));
        try{
            storageService.delete(imageData.getImageUrl().substring(imageData.getImageUrl().lastIndexOf('/')+1));

        }
        catch (NoSuchFileException e){
            e.printStackTrace();

        }
        imageDataRepository.deleteById(imageId);
    }

    @GetMapping("/files")
    List<UserFile> getFiles(){
        return userFileRepository.findAll();
    }
}
