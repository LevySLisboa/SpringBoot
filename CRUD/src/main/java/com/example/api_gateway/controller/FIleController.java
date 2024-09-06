package com.example.api_gateway.controller;

import com.example.api_gateway.data.vo.v1.UploadFileResponseVO;
import com.example.api_gateway.services.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponseVO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        log.info("Storing files to disk");
        return Arrays.stream(files).map(file -> uploadFile(file)).collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        log.info("Reading a file on disk");
        Resource resource = service.loadFileAsResource(fileName);
        String contentType = "";
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch (Exception e){
            log.info("Could not determine file type");
        }
        if(contentType.isBlank()) contentType = "application/octet-stream";
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION,STR."attachment; fileName=\\\{resource.getFilename()} \\").body(resource);
    }
}
