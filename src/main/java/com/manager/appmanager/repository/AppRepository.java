package com.manager.appmanager.repository;

import com.manager.appmanager.model.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends JpaRepository<App, Integer> {
}
