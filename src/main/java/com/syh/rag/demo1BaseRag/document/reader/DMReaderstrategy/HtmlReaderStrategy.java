package com.syh.rag.demo1BaseRag.document.reader.DMReaderstrategy;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.ai.reader.jsoup.config.JsoupDocumentReaderConfig;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * HTML文件读取策略
 */
@Service
public class HtmlReaderStrategy implements DocumentReaderStrategy{
    @Override
    public boolean apply(File file) {
        return file.getName().endsWith(".html") || file.getName().endsWith(".htm");
    }

    @Override
    public List<Document> read(File file) {
        /*
        配置JsoupDocumentReader的参数，例如选择器、字符编码、是否包含链接URL等
         */
        JsoupDocumentReaderConfig config = JsoupDocumentReaderConfig.builder()
                .selector("body")
                .charset("UTF-8")
                .includeLinkUrls(true)
                .build();
        /*
        创建一个Resource对象，表示要读取的文件
         */
        Resource resource = new FileSystemResource(file);
        return new JsoupDocumentReader(resource, config).get();
    }
}
