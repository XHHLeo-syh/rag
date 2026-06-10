package com.syh.rag.demo1BaseRag.document.reader.DMReaderstrategy;

import com.syh.rag.demo1BaseRag.document.reader.constant.FileSignatureConstant;
import com.syh.rag.demo1BaseRag.document.reader.tools.FileSignature;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class TikaReaderStrategy implements DocumentReaderStrategy{
    @Override
    public boolean apply(File file) throws IOException {
        String name = file.getName().toLowerCase();
        FileSignatureConstant signatureConstant = FileSignature.detect(file);
        if (signatureConstant == null) {
            return false;
        }
        // DOC/DOCX
        if ((name.endsWith(".doc") || name.endsWith(".docx")) &&
                (signatureConstant == FileSignatureConstant.DOC || signatureConstant == FileSignatureConstant.DOCX)) {
            return true;
        }
        // PPT/PPTX
        if ((name.endsWith(".ppt") || name.endsWith(".pptx")) &&
                (signatureConstant == FileSignatureConstant.PPT || signatureConstant == FileSignatureConstant.PPTX)) {
            return true;
        }
        // XLS/XLSX (如果需要支持 Excel)
        if ((name.endsWith(".xls") || name.endsWith(".xlsx")) &&
                (signatureConstant == FileSignatureConstant.XLS || signatureConstant == FileSignatureConstant.XLSX)) {
            return true;
        }
        // 兜底：其他 Tika 支持但无专门策略的文件
        return name.endsWith(".doc") || name.endsWith(".docx") ||
               name.endsWith(".ppt") || name.endsWith(".pptx") ||
               name.endsWith(".xls") || name.endsWith(".xlsx");
    }

    @Override
    public List<Document> read(File file) throws FileNotFoundException {
        Resource resource = new FileSystemResource(file);
        return new TikaDocumentReader(resource).get();
    }
}
