package com.cris.sec.registration.services.impl;

import com.cris.sec.registration.services.IGoogleDriveService;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class GoogleDriveServiceImpl implements IGoogleDriveService {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleDriveServiceImpl.class);
    @Value("${logging.file.name}")
    private String logFileName;
    private final Drive drive;

    public GoogleDriveServiceImpl(Drive drive){
        this.drive = drive;
    }

    @Override
    public void uploadFile() {
        try{
            File metadataFile = new File();
            metadataFile.setName(logFileName);

            Path logFile = Path.of("./logs/app.log");
            FileContent fileContent = new FileContent("application/octet-stream", logFile.toFile());
            File uploadedFile = this.drive.files().create(metadataFile, fileContent)
                    .setFields("id")
                    .execute();
            LOG.info("Drive file uploaded id: {} and name: {}", uploadedFile.getId(), uploadedFile.getName());
        } catch (IOException e){
            LOG.error(e.getMessage());
        }
    }
}
