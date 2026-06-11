package com.syh.rag.demo1BaseRag.controller;

import com.syh.rag.demo1BaseRag.document.embedding.EmbeddingService;
import com.syh.rag.demo1BaseRag.document.reader.DMReaderstrategy.DocumentReaderStrategyFactory;
import com.syh.rag.demo1BaseRag.document.reader.tools.DataCleaning;
import com.syh.rag.demo1BaseRag.document.splitter.OverlapParagraphTextSplitter;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

/**
 * 文档预处理和向量化存储基础流程
 * 通过运行项目resource下的static的upload.html页面进行文件上传
 * POST /embedding/upload - 通过文件上传调用
 */
@RestController
@RequestMapping("/embedding")
public class EmbeddingDocumentController {

    @Autowired
    private DocumentReaderStrategyFactory documentReaderStrategyFactory;

    @Autowired
    private EmbeddingService embeddingService;

    /**
     * 通过上传文件进行向量化处理
     * @param file 文件
     * @return 处理结果
     */
    @PostMapping("/upload")
    public String embedding(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "error: 上传文件为空";
        }
        // 检查文件大小（限制 50MB）
        long maxSize = 50 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            return "error: 文件大小超过 50MB 限制";
        }
        // 1、文档读取
        List<Document> documents;
        try {
            documents = documentReaderStrategyFactory.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 2、文档数据清洗
        List<Document> cleanedDocuments = DataCleaning.cleanDocuments(documents);
        // 3、文档分片
        // overlap 分片
        OverlapParagraphTextSplitter overlapParagraphTextSplitter = new OverlapParagraphTextSplitter(2000, 200);
        // 递归分片
        // RecursiveSplitter recursiveSplitter = new RecursiveSplitter();
        List<Document> splitDocuments = overlapParagraphTextSplitter.apply(cleanedDocuments);
        // 4、向量化和存储到向量数据库
        embeddingService.embedAndStore(splitDocuments);
        return "success";
    }
}
