package com.askver.sky.controller;

import com.askver.sky.service.DocumentService;
import com.askver.sky.userdetail.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public String upload(
            @RequestParam MultipartFile file,
            Authentication authentication
    ) throws Exception {

        CustomUserDetails user =
                (CustomUserDetails)
                        authentication.getPrincipal();

        UUID companyId = user.getCompanyId();

        service.uploadPdf(file, companyId);

        return "Uploaded & Indexed Successfully";
    }
}