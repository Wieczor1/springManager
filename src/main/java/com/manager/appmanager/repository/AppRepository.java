package com.manager.appmanager.repository;

import com.manager.appmanager.model.App;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRepository extends JpaRepository<App, Integer> {
}
