package com.syh.rag.demo1BaseRag.controller;

import com.syh.rag.demo1BaseRag.document.reader.DMReaderstrategy.DocumentReaderStrategyFactory;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rag")
public class DocumentReaderController {

    @Autowired
    private DocumentReaderStrategyFactory documentReaderFactory;

    @GetMapping("/document/reader")
    public String documentReader(String filePath) {
        List<Document> documents;
        try {
            documents = documentReaderFactory.read(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StringBuilder sb = new StringBuilder();
        for (Document document : documents) {
            sb.append(document.getText());
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
            System.out.println("========");
            sb.append("========================");
        }
        return sb.toString();
    }


}
