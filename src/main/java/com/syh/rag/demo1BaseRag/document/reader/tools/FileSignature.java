package com.syh.rag.demo1BaseRag.document.reader.tools;

import com.syh.rag.demo1BaseRag.document.reader.constant.FileSignatureConstant;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 文件签名鉴别文件类型
 */
public class FileSignature {

    public static FileSignatureConstant detect(File file) throws IOException {
        // 步骤 1: 准备一个 8 字节的数组，用来装文件头
        byte[] header = new byte[8];
        // 步骤 2: 读取文件的前 8 个字节
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.read(header);
        }
        // 此时 header 里装的是文件的"身份证号"
        // 步骤 3: 拿着这个身份证号，跟枚举里每个格式对比
        for (FileSignatureConstant sig : FileSignatureConstant.values()) {
            // 调用 matches 判断是否匹配
            if (sig.matches(header)) {
                // 匹配成功，返回这个格式
                return sig;
            }
        }
        // 步骤 4: 都没匹配上，返回 null
        return null;
    }
}
