package com.example.distancecalculator.controllers;

import com.example.distancecalculator.services.UploadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class UploadController {
    private final UploadServiceImpl uploadServiceImpl;

    @Autowired
    public UploadController(UploadServiceImpl uploadServiceImpl) {
        this.uploadServiceImpl = uploadServiceImpl;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity uploadAll(@RequestParam("file") MultipartFile files) throws JAXBException, IOException {
        uploadServiceImpl.uploadAll(files);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/city")
    @ResponseBody
    public ResponseEntity uploadOneCity(@RequestParam("file") MultipartFile files) throws JAXBException, IOException {
        uploadServiceImpl.uploadCity(files);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/distance")
    @ResponseBody
    public ResponseEntity uploadOneDistance(@RequestParam("file") MultipartFile files) throws JAXBException, IOException {
        uploadServiceImpl.uploadDistance(files);
        return ResponseEntity.ok().build();
    }

}
