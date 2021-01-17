package com.manager.appmanager.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageProperties {

	/**
	 * Folder location for storing files
	 */
	private String location = "upload-dir";
	private String exportLocation = "exportcsv";
	private String exportZipLocation = "export";


	public String getExportZipLocation() {
		return exportZipLocation;
	}

	public String getLocation() {
		return location;
	}

	public String getExportLocation() {
		return exportLocation;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
