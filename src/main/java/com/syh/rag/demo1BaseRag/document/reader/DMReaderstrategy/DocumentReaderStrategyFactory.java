package com.syh.rag.demo1BaseRag.document.reader.DMReaderstrategy;

import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 文档读取策略工厂
 */
@Component
public class DocumentReaderStrategyFactory {
    @Autowired
    private List<DocumentReaderStrategy> documentReaderStrategies;

    public List<Document> read(MultipartFile file) throws IOException {
        for (DocumentReaderStrategy strategy : documentReaderStrategies) {
            if (strategy.apply(file)) {
                InputStream fileInputStream;
                try {
                    fileInputStream = file.getInputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                InputStreamResource resourceFile = new InputStreamResource(fileInputStream);
                return strategy.read(resourceFile);
            }
        }
        throw new IllegalArgumentException("当前不支持该文件类型，文件：: " + file.getOriginalFilename());
    }
}
