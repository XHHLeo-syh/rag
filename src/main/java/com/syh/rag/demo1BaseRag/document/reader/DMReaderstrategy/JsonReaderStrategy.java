package com.syh.rag.demo1BaseRag.document.reader.DMReaderstrategy;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Json文件读取策略
 */
@Service
public class JsonReaderStrategy implements DocumentReaderStrategy{
    @Override
    public boolean apply(MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename())
                      .toLowerCase().endsWith(".json");
    }

    @Override
    public List<Document> read(InputStreamResource resource) {
        JsonReader reader = new JsonReader(resource);
        return reader.read();
    }
}
