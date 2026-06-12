package com.syh.rag.demo1BaseRag.controller;

import com.syh.rag.demo1BaseRag.document.embedding.EmbeddingService;
import com.syh.rag.demo1BaseRag.document.reader.DMReaderstrategy.DocumentReaderStrategyFactory;
import com.syh.rag.demo1BaseRag.document.reader.tools.DataCleaning;
import com.syh.rag.demo1BaseRag.document.splitter.OverlapParagraphTextSplitter;
import lombok.Data;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文档预处理和向量化存储基础流程
 * 通过运行项目 resource 下的 static 的 upload.html 页面进行文件上传
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
     * 文件上传结果
     */
    @Data
    public static class UploadResult {
        /** 文件名 */
        private String fileName;
        /** 是否成功 */
        private boolean success;
        /** 消息 */
        private String message;

        public UploadResult(String fileName, boolean success, String message) {
            this.fileName = fileName;
            this.success = success;
            this.message = message;
        }
    }

    /**
     * 批量上传文件并进行向量化处理
     * @param fileList 文件列表
     * @return 每个文件的处理结果
     */
    @PostMapping("/upload")
    public List<UploadResult> uploadAndEmbed(@RequestParam("file") MultipartFile[] fileList) {
        List<UploadResult> results = new ArrayList<>();

        if (fileList == null || fileList.length == 0) {
            results.add(new UploadResult("", false, "上传文件为空"));
            return results;
        }

        for (MultipartFile file : fileList) {
            String fileName = file.getOriginalFilename();

            // 检查文件是否为空
            if (file.isEmpty()) {
                results.add(new UploadResult(fileName, false, "文件为空"));
                continue;
            }

            // 检查文件大小（限制 50MB）
            long maxSize = 50 * 1024 * 1024;
            if (file.getSize() > maxSize) {
                results.add(new UploadResult(fileName, false, "文件大小超过 50MB 限制"));
                continue;
            }

            // 检查文件类型
            if (fileName == null) {
                results.add(new UploadResult(fileName, false, "无法获取文件名"));
                continue;
            }
            try {
                // 1、文档读取
                List<Document> documents = documentReaderStrategyFactory.read(file);
                // 2、文档数据清洗
                List<Document> cleanedDocuments = DataCleaning.cleanDocuments(documents);
                // 3、文档分片（使用 overlap 分片器）
                OverlapParagraphTextSplitter overlapParagraphTextSplitter = new OverlapParagraphTextSplitter(2000, 200);
                List<Document> splitDocuments = overlapParagraphTextSplitter.apply(cleanedDocuments);
                // 4、向量化和存储到向量数据库
                embeddingService.embedAndStore(splitDocuments);
                results.add(new UploadResult(fileName, true, "处理成功"));
            } catch (IllegalArgumentException e) {
                results.add(new UploadResult(fileName, false, e.getMessage()));
            } catch (IOException e) {
                results.add(new UploadResult(fileName, false, "文件处理失败 - " + e.getMessage()));
            } catch (Exception e) {
                results.add(new UploadResult(fileName, false, "处理异常 - " + e.getMessage()));
            }
        }
        return results;
    }
}
