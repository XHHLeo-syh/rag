package com.syh.rag.demo1BaseRag.document.reader.DMReaderstrategy;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.List;

/**
 * 文本文件读取策略
 */
@Service
public class TextReaderStrategy implements DocumentReaderStrategy {
    @Override
    public boolean apply(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".txt") || name.endsWith(".tex") || name.endsWith(".text");
    }

    @Override
    public List<Document> read(File file) {
        Resource resource =new FileSystemResource(file);
        TextReader textReader = new TextReader(resource);
        return textReader.get();
    }
}
