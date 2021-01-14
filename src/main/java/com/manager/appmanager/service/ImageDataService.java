package com.manager.appmanager.service;

import com.manager.appmanager.exception.NotFoundException;
import com.manager.appmanager.model.App;
import com.manager.appmanager.model.ImageData;
import com.manager.appmanager.repository.AppRepository;
import com.manager.appmanager.repository.ImageDataRepository;
import org.springframework.stereotype.Service;

@Service
public class ImageDataService {
    private final AppRepository appRepository;
    private final ImageDataRepository imageDataRepository;

    public ImageDataService(AppRepository appRepository, ImageDataRepository imageDataRepository) {
        this.appRepository = appRepository;
        this.imageDataRepository = imageDataRepository;
    }

    public void addImageToApp(int appId, String url) throws NotFoundException {
        App app = appRepository.findById(appId).orElseThrow(() -> new NotFoundException(appId));
        ImageData imageData = new ImageData();
        imageData.setApp(app);
        imageData.setImageUrl(url);

        app.getImages().add(imageData);
        imageDataRepository.save(imageData);

    }    public void editImage(int imageId, String url) throws NotFoundException {
        ImageData imageData = imageDataRepository.findById(imageId).orElseThrow(() -> new NotFoundException(imageId));
        imageData.setImageUrl(url);
        imageDataRepository.save(imageData);

    }
}
