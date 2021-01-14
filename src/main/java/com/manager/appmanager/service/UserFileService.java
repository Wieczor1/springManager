package com.manager.appmanager.service;

import com.manager.appmanager.exception.NotFoundException;
import com.manager.appmanager.model.App;
import com.manager.appmanager.model.User;
import com.manager.appmanager.model.UserFile;
//import com.manager.appmanager.model.UserFileKey;
import com.manager.appmanager.repository.AppRepository;
import com.manager.appmanager.repository.UserFileRepository;
import com.manager.appmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserFileService {
    private final UserRepository userRepository;
    private final AppRepository appRepository;
    private final UserFileRepository userFileRepository;

    public UserFileService(UserRepository userRepository, AppRepository appRepository, UserFileRepository userFileRepository) {
        this.userRepository = userRepository;
        this.appRepository = appRepository;
        this.userFileRepository = userFileRepository;
    }

    public void addUserFile(int userId, int appId, String filename) throws NotFoundException {
        App app = appRepository.findById(appId).orElseThrow(() -> new NotFoundException(appId));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));

        UserFile userFile = new UserFile();
//        userFile.setId(new UserFileKey(app.getId(), user.getId()));
        userFile.setApp(app);
        userFile.setUser(user);
        userFile.setFilename(filename);
        app.getUserFilesInApp().add(userFile);
        user.getUserFiles().add(userFile);
        userFileRepository.save(userFile);
    }

    public void removeUserFile(int userId, int appId, UserFile userFile) throws NotFoundException {
        App app = appRepository.findById(appId).orElseThrow(() -> new NotFoundException(appId));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));

//
//        app.getUserFilesInApp().remove(userFile);
//        user.getUserFiles().remove(userFile);
        userFileRepository.delete(userFile);
    }
}
