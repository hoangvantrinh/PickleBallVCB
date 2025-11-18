package com.vcb.master.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TemplateImageController {

    @GetMapping(value = "/templates/back-ground.jpg", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getTemplateBackground() throws IOException {
        Resource img = new ClassPathResource("templates/back-ground.jpg");
        if (!img.exists()) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        return ResponseEntity.ok().headers(headers).contentLength(img.contentLength()).contentType(MediaType.IMAGE_JPEG).body(img);
    }
}
