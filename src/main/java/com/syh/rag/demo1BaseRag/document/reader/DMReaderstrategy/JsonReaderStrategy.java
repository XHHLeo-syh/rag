package com.syh.rag.demo1BaseRag.document.reader.DMReaderstrategy;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Json文件读取策略
 */
@Service
public class JsonReaderStrategy implements DocumentReaderStrategy{
    @Override
    public boolean apply(File file) throws IOException {
        return file.getName().toLowerCase().endsWith(".json");
    }

    @Override
    public List<Document> read(File file) throws FileNotFoundException {
        Resource resource = new FileSystemResource(file.getPath());
        JsonReader reader = new JsonReader(resource);
        return reader.read();
    }
}
