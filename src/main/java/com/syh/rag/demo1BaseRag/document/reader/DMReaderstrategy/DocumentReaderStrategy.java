package com.syh.rag.demo1BaseRag.document.reader.DMReaderstrategy;


import org.springframework.ai.document.Document;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * 文档读取策略
 */
public interface DocumentReaderStrategy {

    /**
     * 判断文件是否符合当前策略
     * @param file 文件
     * @return 是否符合
     */
    boolean apply(MultipartFile file) throws IOException;

    /**
     * 读取文件并返回Document列表
     * @param resource 文件输入流
     * @return 文件内容
     */
    List<Document> read(InputStreamResource resource) throws FileNotFoundException;
}
