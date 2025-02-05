package com.cris.sec.registration.services.impl;

import com.cris.sec.registration.services.IGoogleDriveService;
import com.cris.sec.registration.services.ILogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements ILogService {

    private static final Logger LOG = LoggerFactory.getLogger(LogServiceImpl.class);

    private final IGoogleDriveService googleDriveService;

    public LogServiceImpl(IGoogleDriveService googleDriveService){
        this.googleDriveService = googleDriveService;
    }
    @Override
    @Scheduled(fixedRate = 30000)
    public void uploadLogs() {
        LOG.info("[LogServiceImpl] Start uploadLogs");
        try {
            this.googleDriveService.uploadFile();
        } catch (Exception e){
            LOG.error("Exception: {}", e.getMessage());
        }
    }
}
