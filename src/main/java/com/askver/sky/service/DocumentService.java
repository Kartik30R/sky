package com.askver.sky.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final VectorStore vectorStore;

    public DocumentService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void uploadPdf(
            MultipartFile file,
            UUID companyId
    ) throws Exception {

        PagePdfDocumentReader reader =
                new PagePdfDocumentReader(file.getResource());

        List<Document> documents = reader.get();

        TokenTextSplitter splitter =
                new TokenTextSplitter();

        List<Document> splitDocs =
                splitter.apply(documents);

        splitDocs.forEach(doc ->
                doc.getMetadata()
                        .put("companyId",
                                companyId.toString())
        );

        vectorStore.add(splitDocs);
    }
}