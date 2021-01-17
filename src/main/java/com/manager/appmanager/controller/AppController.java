package com.manager.appmanager.controller;

import com.manager.appmanager.exception.NotFoundException;
import com.manager.appmanager.model.*;
import com.manager.appmanager.repository.AppRepository;
import com.manager.appmanager.repository.ImageDataRepository;
import com.manager.appmanager.repository.LocationRepository;
import com.manager.appmanager.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.channels.NotYetBoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:4200")
public class AppController {

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public AppController(LocationRepository locationRepository, UserRepository userRepository, AppRepository repository, ImageDataRepository imageDataRepository) {
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.repository = repository;
        this.imageDataRepository = imageDataRepository;
    }

    private final AppRepository repository;
    private final ImageDataRepository imageDataRepository;

    @GetMapping("/apps")
    List<App> allUsers(){
        return repository.findAll();
    }

    @GetMapping("/apps/users/{userId}")
    Set<App> allUsers(@PathVariable int userId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        return user.getUsersApp();
    }



    @GetMapping("/apps/{id}")
    App oneApp(@PathVariable int id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    @PostMapping("/apps")
    App newApp(@Valid @RequestBody App newApp){
        return repository.save(newApp);
    }

    @PutMapping("/apps/{id}")
    App editApp(@Valid @RequestBody App editedApp, @PathVariable int id) throws NotFoundException {
        return repository.findById(id)
                .map(app -> {
                    app.setDomain(editedApp.getDomain());
                    app.setName(editedApp.getName());
                    app.setVersion(editedApp.getVersion());
                    return repository.save(app);
                }).orElseThrow(() -> new NotFoundException(id));
    }

    @DeleteMapping("/apps/{id}")
    void deleteApp(@PathVariable int id) throws NotFoundException {
//        App app = repository.findById(id).orElseThrow(() -> new NotFoundException(id));
//        app.getAppUsers().forEach(user -> user.getUsersApp().remove(app));
            repository.deleteById(id);
    }

    @PostMapping("/apps/{id}/location")
    void addLocationToApp(@PathVariable int id, @Valid @RequestBody Location newLocation) throws NotFoundException {
        App app = repository.findById(id).orElseThrow(() -> new NotFoundException(id));
        app.addLocation(newLocation);
        repository.save(app);
    }

    @GetMapping("/apps/{id}/location")
    List<Location> getLocationFromApp(@PathVariable int id) throws NotFoundException {
        App app = repository.findById(id).orElseThrow(() -> new NotFoundException(id));
        return new ArrayList<>(app.getLocations());
    }

    @GetMapping("/apps/location")
    List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @GetMapping("/apps/location/{locId}")
    Location getLocationById(@PathVariable int locId) throws NotFoundException {
        return locationRepository.findById(locId).orElseThrow(() -> new NotFoundException(locId));

    }

    @PutMapping("/apps/location/{locId}")
    void editLocationFromApp(@PathVariable int locId, @Valid @RequestBody Location newLocation) throws NotFoundException {
        locationRepository.findById(locId).map(
                location -> {
                    location.setCity(newLocation.getCity());
                    location.setCountry(newLocation.getCountry());
                    location.setStreet(newLocation.getStreet());
                    location.setStreetNumber(newLocation.getStreetNumber());
                    return locationRepository.save(location);
                }).orElseThrow(() -> new NotFoundException(locId));
    }

    @DeleteMapping("/apps/location/{locId}")
    void removeLocationFromApp(@PathVariable int locId) throws NotFoundException {
        Location location = locationRepository.findById(locId).orElseThrow(() -> new NotFoundException(locId));//TODO exception uzwgledni tez klase ale to moze kiedys
        location.getApp().removeLocation(location);
        locationRepository.delete(location);
    }

    @GetMapping("/apps/{appId}/images")
    List<ImageData> allImagesFromApp(@PathVariable int appId) throws NotFoundException {
        App app = repository.findById(appId).orElseThrow(() -> new NotFoundException(appId));
        return new ArrayList<>(app.getImages());

    }
}
