package com.cris.sec.registration.configuration;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class GoogleDriveConfig {

    @Value("${spring.security.google.credentials.path}")
    private Resource credentialsResource;

    @Bean
    public Drive drive() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = new GsonFactory();

        GoogleCredentials credentials = GoogleCredentials.
                fromStream(new FileInputStream(credentialsResource.getFile()))
                .createScoped(DriveScopes.DRIVE);

        return new Drive.Builder(httpTransport, jsonFactory, new HttpCredentialsAdapter(credentials))
                .setApplicationName("SpringBoot Registration Project")
                .build();

    }
}
