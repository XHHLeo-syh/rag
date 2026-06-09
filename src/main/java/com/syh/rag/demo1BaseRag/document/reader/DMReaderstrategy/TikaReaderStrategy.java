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
        if ((name.endsWith(".doc") || name.endsWith(".docx")) && signatureConstant != null &&
                (signatureConstant.equals(FileSignatureConstant.DOCX) || signatureConstant.equals(FileSignatureConstant.DOC)))
            return true;
        else if (signatureConstant != null && name.endsWith(".pdf") && signatureConstant.equals(FileSignatureConstant.PDF))
            return true;
        else return name.endsWith(".ppt") || name.endsWith(".pptx") && signatureConstant != null &&
                    (signatureConstant.equals(FileSignatureConstant.PPTX) || signatureConstant.equals(FileSignatureConstant.PPT));
    }

    @Override
    public List<Document> read(File file) throws FileNotFoundException {
        Resource resource = new FileSystemResource(file);
        return new TikaDocumentReader(resource).get();
    }
}
