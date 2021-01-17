package com.manager.appmanager.controller;

import com.manager.appmanager.exception.NotFoundException;
import com.manager.appmanager.model.App;
import com.manager.appmanager.model.User;
import com.manager.appmanager.model.UserFile;
import com.manager.appmanager.repository.AppRepository;
import com.manager.appmanager.repository.UserRepository;
import com.manager.appmanager.service.EmailService;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class UserController {

    private final AppRepository appRepository;
    private final EmailService emailService;

    public UserController(AppRepository appRepository, EmailService emailService, UserRepository repository) {
        this.appRepository = appRepository;
        this.emailService = emailService;
        this.repository = repository;
    }

    private final UserRepository repository;

    @GetMapping("/users")
    List<User> allUsers(){
        return repository.findAll();
    }

    @GetMapping("/users/apps/{appId}")
    List<User> allUsers(@PathVariable int appId) throws NotFoundException {
        App app = appRepository.findById(appId).orElseThrow(() -> new NotFoundException(appId));
        return new ArrayList<>(app.getAppUsers());
    }

    @GetMapping("/users/{userId}/files")
    List<UserFile> allUsersApps(@PathVariable int userId) throws NotFoundException {
        User user = repository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        return new ArrayList<>(user.getUserFiles());
    }

    @GetMapping("/users/{id}")
    User oneUser(@PathVariable int id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    @PostMapping("/users")
    User newUser(@Valid @RequestBody User newUser) throws MessagingException {
        User user = repository.save(newUser);
        emailService.sendMail(newUser.getEmail(), "Registration", "Hello, " + newUser.getUsername() +
                ", you have registered!", false);
        return user;
    }

    @PutMapping("/users/{id}")
    User editUser(@Valid @RequestBody User editedUser, @PathVariable int id) throws NotFoundException {
        return repository.findById(id)
                .map(user -> {
                    user.setFirstName(editedUser.getFirstName());
                    user.setLastName(editedUser.getLastName());
                    user.setEmail(editedUser.getEmail());
                    user.setUsername(editedUser.getUsername());
                    return repository.save(user);
                }).orElseThrow(() -> new NotFoundException(id));
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable int id) {
         repository.deleteById(id);
    }

    @PostMapping("/users/{userId}/app/{appId}")
    void addAppToUser(@PathVariable int userId, @PathVariable int appId) throws NotFoundException {

        User user = repository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        App appToAdd = appRepository.findById(appId).orElseThrow(() -> new NotFoundException(userId));
        user.addApp(appToAdd);
        repository.save(user);
    }


    @DeleteMapping("/users/{userId}/app/{appId}")
    void removeAppFromUser(@PathVariable int userId, @PathVariable int appId) throws NotFoundException {

        User user = repository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
        App appToAdd = appRepository.findById(appId).orElseThrow(() -> new NotFoundException(userId));
        user.removeApp(appToAdd);
        repository.save(user);
    }

}

