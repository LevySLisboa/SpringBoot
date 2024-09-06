package com.example.api_gateway.controller;

import com.example.api_gateway.data.vo.v1.UploadFileResponseVO;
import com.example.api_gateway.services.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/file/v1")
@Tag(name = "File Endpoint")
public class FIleController {
    private static final Logger log = LoggerFactory.getLogger(FIleController.class);

    @Autowired
    private FileStorageService service;

    @PostMapping("/uploadFile")
    public UploadFileResponseVO uploadFile(@RequestParam("file") MultipartFile file) {
        log.info("Storing file to disk");
        var fileName = service.storeFIle(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().
                path("/api/file/v1/downloadFile/").path(fileName).toUriString();
        return new UploadFileResponseVO(fileName,fileDownloadUri, file.getContentType(), file.getSize());
    }
}
