package com.manager.appmanager.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	void store(MultipartFile file);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

	Resource exportAsResource();

	void deleteAll();

	void deleteAllExported();

	void delete(String filename) throws IOException;

	String getDownloadUrl(MultipartFile file);

}
