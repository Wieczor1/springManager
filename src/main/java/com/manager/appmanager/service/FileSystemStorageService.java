package com.manager.appmanager.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import com.manager.appmanager.configuration.StorageProperties;
import com.manager.appmanager.controller.FileController;
import com.manager.appmanager.exception.StorageException;
import com.manager.appmanager.exception.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;
	private final Path exportLocation;
	private final Path exportZipLocation;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
		this.exportLocation = Paths.get(properties.getExportLocation());
		this.exportZipLocation = Paths.get(properties.getExportZipLocation());
	}

	@Override
	public void store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}
			Path destinationFile = this.rootLocation.resolve(
					Paths.get(file.getOriginalFilename()))
					.normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new StorageException(
						"Cannot store file outside current directory.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {

				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public Resource exportAsResource() {
		try {
			Path file = exportZipLocation.resolve("export.zip");
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {

				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " );

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void deleteAllExported() {
		FileSystemUtils.deleteRecursively(exportLocation.toFile());
		FileSystemUtils.deleteRecursively(exportZipLocation.toFile());
	}

	@Override
	public void delete(String filename) throws IOException {
		Path path = load(filename);
		Files.delete(path);
	}

	@Override
	public String getDownloadUrl(MultipartFile file) {

				return MvcUriComponentsBuilder.fromMethodName(FileController.class,
				"serveFile",
				file.getOriginalFilename()).build().toString();

	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
			Files.createDirectories(exportLocation);
			Files.createDirectories(exportZipLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
