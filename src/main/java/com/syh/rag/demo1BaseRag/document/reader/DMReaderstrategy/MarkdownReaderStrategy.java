package com.syh.rag.demo1BaseRag.document.reader.DMReaderstrategy;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Markdown文件读取策略
 */
@Service
public class MarkdownReaderStrategy implements DocumentReaderStrategy{
    @Override
    public boolean apply(File file) throws IOException {
        String name = file.getName().toLowerCase();
        return name.endsWith(".md");
    }

    @Override
    public List<Document> read(File file) throws FileNotFoundException {
        /*
         配置MarkdownDocumentReader的参数，例如是否将水平线创建为文档、是否包含代码块、是否包含块引用等
         */
        MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig
                .builder()
                .withHorizontalRuleCreateDocument(true)
                .withIncludeCodeBlock(false)
                .withIncludeBlockquote(false)
                .build();
        Resource resource = new FileSystemResource(file);
        return new MarkdownDocumentReader(resource, config).get();
    }
}
