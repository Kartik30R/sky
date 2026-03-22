package com.askver.sky.controller;

import com.askver.sky.service.DocumentService;
import com.askver.sky.userdetail.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

   
    @PostMapping("/upload/{department}")
    public ResponseEntity<String> upload(
            @PathVariable String department,
            @RequestParam("files") List<MultipartFile> files,
            Authentication authentication
    ) throws Exception {

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();
        UUID companyId = user.getCompanyId();

        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body("No files provided.");
        }

        service.uploadPdfs(files, companyId, department);

        return ResponseEntity.ok(
                "Uploaded & indexed " + files.size() + " file(s) to department: " + department
        );
    }

  
    @GetMapping("/{department}")
    public ResponseEntity<List<String>> listDocuments(
            @PathVariable String department,
            Authentication authentication
    ) {
        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();
        UUID companyId = user.getCompanyId();

        List<String> documents = service.listDocuments(companyId, department);
        return ResponseEntity.ok(documents);
    }

  
    @DeleteMapping("/{department}/{fileName}")
    public ResponseEntity<String> deleteDocument(
            @PathVariable String department,
            @PathVariable String fileName,
            Authentication authentication
    ) {
        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();
        UUID companyId = user.getCompanyId();

        int deleted = service.deleteDocumentByName(companyId, department, fileName);

        if (deleted == 0) {
            return ResponseEntity.ok("No document found with name: " + fileName);
        }
        return ResponseEntity.ok("Deleted " + deleted + " chunk(s) for document: " + fileName);
    }
}
