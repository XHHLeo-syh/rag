package com.syh.rag.demo1BaseRag.document.reader.DMReaderstrategy;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Objects;

/**
 * 文本文件读取策略
 */
@Service
public class TextReaderStrategy implements DocumentReaderStrategy {
    @Override
    public boolean apply(MultipartFile file) {
        String name = Objects.requireNonNull(file.getOriginalFilename())
                             .toLowerCase();
        return name.endsWith(".txt") || name.endsWith(".tex") || name.endsWith(".text");
    }

    @Override
    public List<Document> read(InputStreamResource resource) {
        TextReader textReader = new TextReader(resource);
        return textReader.get();
    }
}
