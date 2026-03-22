package com.askver.sky.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;

    public DocumentService(VectorStore vectorStore, JdbcTemplate jdbcTemplate) {
        this.vectorStore = vectorStore;
        this.jdbcTemplate = jdbcTemplate;
    }

    
    public void uploadPdfs(
            List<MultipartFile> files,
            UUID companyId,
            String department
    ) throws Exception {

        TokenTextSplitter splitter = new TokenTextSplitter();

        for (MultipartFile file : files) {
            PagePdfDocumentReader reader =
                    new PagePdfDocumentReader(file.getResource());

            List<Document> documents = reader.get();
            List<Document> splitDocs = splitter.apply(documents);

            String originalFilename = file.getOriginalFilename() != null
                    ? file.getOriginalFilename()
                    : "unknown";

            for (Document doc : splitDocs) {
                doc.getMetadata().put("companyId", companyId.toString());
                doc.getMetadata().put("department", department.toLowerCase());
                doc.getMetadata().put("fileName", originalFilename);
            }

            vectorStore.add(splitDocs);
        }
    }

    
    public List<String> listDocuments(UUID companyId, String department) {
        String sql = """
                SELECT DISTINCT metadata->>'fileName'
                FROM vector_store
                WHERE metadata->>'companyId' = ?
                  AND metadata->>'department' = ?
                  AND metadata->>'fileName' IS NOT NULL
                """;
        return jdbcTemplate.queryForList(sql, String.class,
                companyId.toString(), department.toLowerCase());
    }

   
    public int deleteDocumentByName(UUID companyId, String department, String fileName) {
        String sql = """
                DELETE FROM vector_store
                WHERE metadata->>'companyId' = ?
                  AND metadata->>'department' = ?
                  AND metadata->>'fileName' = ?
                """;
        return jdbcTemplate.update(sql,
                companyId.toString(), department.toLowerCase(), fileName);
    }
}
