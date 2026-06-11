package com.syh.rag.demo1BaseRag.document.reader.DMReaderstrategy;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
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
 * Markdown文件读取策略
 */
@Service
public class MarkdownReaderStrategy implements DocumentReaderStrategy{
    @Override
    public boolean apply(MultipartFile file) {
        String name = Objects.requireNonNull(file.getOriginalFilename())
                             .toLowerCase();
        return name.endsWith(".md");
    }

    @Override
    public List<Document> read(InputStreamResource resource) {
        /*
         配置MarkdownDocumentReader的参数，例如是否将水平线创建为文档、是否包含代码块、是否包含块引用等
         */
        MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig
                .builder()
                .withHorizontalRuleCreateDocument(true)
                .withIncludeCodeBlock(false)
                .withIncludeBlockquote(false)
                .build();
        return new MarkdownDocumentReader(resource, config).get();
    }
}
