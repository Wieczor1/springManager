package com.manager.appmanager.controller;

import com.manager.appmanager.exception.NotFoundException;
import com.manager.appmanager.model.App;
import com.manager.appmanager.model.User;
import com.manager.appmanager.repository.AppRepository;
import com.manager.appmanager.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class AppController {

    public AppController(AppRepository repository) {
        this.repository = repository;
    }

    private final AppRepository repository;

    @GetMapping("/apps")
    List<App> allUsers(){
        return repository.findAll();
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
    void deleteApp(@PathVariable int id) {
        repository.deleteById(id);
    }
}
