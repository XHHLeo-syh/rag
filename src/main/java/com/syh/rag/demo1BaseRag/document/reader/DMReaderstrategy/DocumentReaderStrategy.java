package com.syh.rag.demo1BaseRag.document.reader.DMReaderstrategy;


import org.springframework.ai.document.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface DocumentReaderStrategy {

    /**
     * 判断文件是否符合当前策略
     * @param file 文件
     * @return 是否符合
     */
    boolean apply(File file) throws IOException;

    /**
     * 读取文件并返回Document列表
     * @param file 文件
     * @return 文件内容
     */
    List<Document> read(File file) throws FileNotFoundException;
}
