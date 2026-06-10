package com.syh.rag.demo1BaseRag.document.reader.DMReaderstrategy;

import com.syh.rag.demo1BaseRag.document.reader.constant.FileSignatureConstant;
import com.syh.rag.demo1BaseRag.document.reader.tools.FileSignature;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * PDF文件读取策略
 */
@Service
public class PdfReaderStrategy implements DocumentReaderStrategy {
    @Override
    public boolean apply(File file) throws IOException {
        String name = file.getName().toLowerCase();
        if (!name.endsWith(".pdf")) {
            return false;
        }
        FileSignatureConstant signature = FileSignature.detect(file);
        return signature == FileSignatureConstant.PDF;
    }

    @Override
    public List<Document> read(File file) {
        Resource resource = new FileSystemResource(file);

        // 读取配置
        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                                                                .withPageTopMargin(50)         // 忽略顶部50个单位的页眉
                                                                .withPageBottomMargin(50)      // 忽略底部50个单位的页脚
                                                                .withPagesPerDocument(1)       // 每一页作为一个 Document
                                                                .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().withNumberOfTopTextLinesToDelete(0) // 每页再额外删掉前0行
                                                                                                                                    .build())
                                                                .build();
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource, config);
        return pdfReader.read();
    }
}