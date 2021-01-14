package com.manager.appmanager.repository;

import com.manager.appmanager.model.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFileRepository extends JpaRepository<UserFile, Integer> {
}
