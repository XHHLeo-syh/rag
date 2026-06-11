package com.syh.rag.demo1BaseRag.document.reader.tools;

import com.syh.rag.demo1BaseRag.document.reader.constant.FileSignatureConstant;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * 文件签名鉴别文件类型
 */
public class FileSignature {

    public static FileSignatureConstant detect(MultipartFile file) throws IOException {
        // 步骤 1: 准备一个 8 字节的数组，用来装文件头
        byte[] header = new byte[8];
        // 步骤 2: 读取文件的前 8 个字节
        try (InputStream is = file.getInputStream()) {
            int bytesRead = is.read(header);
            if (bytesRead <= 0) {
                return null;
            }
        }
        // 此时 header 里装的是文件的"身份证号"
        // 步骤 3: 拿着这个身份证号，跟枚举里每个格式对比
        for (FileSignatureConstant sig : FileSignatureConstant.values()) {
            if (sig.matches(header)) {
                return sig;
            }
        }
        // 步骤 4: 都没匹配上，返回 null
        return null;
    }
}
