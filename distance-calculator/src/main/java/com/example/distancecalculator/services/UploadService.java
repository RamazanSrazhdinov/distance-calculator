package com.example.distancecalculator.services;

import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface UploadService {
    void uploadAll(MultipartFile file)  throws IOException, JAXBException;
    void uploadCity(MultipartFile files)  throws IOException, JAXBException ;
    void uploadDistance(MultipartFile files) throws IOException, JAXBException;
}
