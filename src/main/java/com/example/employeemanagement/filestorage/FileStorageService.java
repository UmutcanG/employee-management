package com.example.employeemanagement.filestorage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    public void storeFile(MultipartFile file, String fileName) throws IOException {
        Path targetLocation = Paths.get(uploadDir).resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    }

    public Path loadFile(String fileName) {
        return Paths.get(uploadDir).resolve(fileName);
    }
}
