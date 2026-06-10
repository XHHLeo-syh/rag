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
        if (filePath == null || filePath.isBlank()) {
            return "错误：filePath 参数不能为空";
        }
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return "错误：文件不存在或不是有效文件：" + filePath;
        }

        List<Document> documents;
        try {
            documents = documentReaderFactory.read(file);
        } catch (IOException e) {
            return "读取文件失败：" + e.getMessage();
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }

        StringBuilder sb = new StringBuilder();
        for (Document document : documents) {
            sb.append(document.getText()).append("========================");
        }
        return sb.toString();
    }


}
