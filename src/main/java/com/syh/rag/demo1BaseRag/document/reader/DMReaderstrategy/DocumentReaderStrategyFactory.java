package com.syh.rag.demo1BaseRag.document.reader.DMReaderstrategy;

import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 文档读取策略工厂
 */
@Component
public class DocumentReaderStrategyFactory {
    @Autowired
    private List<DocumentReaderStrategy> documentReaderStrategies;

    public List<Document> read(File file) throws IOException {
        for (DocumentReaderStrategy strategy : documentReaderStrategies) {
            if (strategy.apply(file)) {
                return strategy.read(file);
            }
        }
        throw new IllegalArgumentException("当前不支持该文件类型，文件：: " + file.getName());
    }
}
