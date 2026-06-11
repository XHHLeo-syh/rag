package com.syh.rag.demo1BaseRag.document.splitter;

import com.alibaba.cloud.ai.transformer.splitter.RecursiveCharacterTextSplitter;
import org.springframework.ai.document.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 递归分片（使用RecursiveCharacterTextSplitter默认实现）
 * 可修改了构造函数适配Document
 */
public class RecursiveSplitter {
    // 配置递归分片的参数
    int chunkSize = 2000;
    String[] separators = { "\n\n", "\n"};
    RecursiveCharacterTextSplitter splitter = new RecursiveCharacterTextSplitter(chunkSize, separators);

    public List<Document> splitDocuments(List<Document> documents) {
        List<Document> result = new ArrayList<Document>();
        documents.forEach(doc -> {
            String docText = doc.getText();
            List<String> chunks = splitter.splitText(docText);
            for (int i = 0; i < chunks.size(); i++) {
                // ✅ 复制原始元数据
                Map<String, Object> chunkMetadata = new HashMap<>(doc.getMetadata());
                // ✅ 添加分片特有信息
                chunkMetadata.put("chunk_index", i);
                chunkMetadata.put("total_chunks", chunks.size());
                chunkMetadata.put("parent_document_id", doc.getId());
                // ✅ 创建带元数据的新 Document
                Document chunkDoc = Document.builder()
                                            .text(chunks.get(i))
                                            .metadata(chunkMetadata)
                                            .build();
                result.add(chunkDoc);
            }
        });
        return result;
    }
}
