package com.syh.rag.demo1BaseRag.document.reader.constant;

/**
 * 文件签名类型枚举
 */
public enum FileSignatureConstant {
    PDF(new byte[]{0x25, 0x50, 0x44, 0x46}),
    DOCX(new byte[]{0x50, 0x4B, 0x03, 0x04}),
    DOC(new byte[]{(byte)0xD0, (byte)0xCF, 0x11, (byte)0xE0}),
    PPTX(new byte[]{0x50, 0x4B, 0x03, 0x04}),
    PPT(new byte[]{(byte)0xD0, (byte)0xCF, 0x11, (byte)0xE0}),
    XLSX(new byte[]{0x50, 0x4B, 0x03, 0x04}),
    XLS(new byte[]{(byte)0xD0, (byte)0xCF, 0x11, (byte)0xE0}),
    PNG(new byte[]{(byte)0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A}),
    JPG(new byte[]{(byte)0xFF, (byte)0xD8, (byte)0xFF}),
    GIF(new byte[]{0x47, 0x49, 0x46, 0x38}),
    ZIP(new byte[]{0x50, 0x4B, 0x03, 0x04});

    private final byte[] signature;

    FileSignatureConstant(byte[] signature) {
        this.signature = signature;
    }

    public boolean matches(byte[] fileHeader) {
        // 检查 1: 文件头长度够不够
        if (fileHeader.length < signature.length) return false;
        // 检查 2: 逐个字节对比
        for (int i = 0; i < signature.length; i++) {
            // 有一个不一样就不是
            if (signature[i] != fileHeader[i]) return false;
        }
        // 全部一样，匹配成功
        return true;
    }
}