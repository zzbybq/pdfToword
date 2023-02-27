package com.example.pdftoword.utils;

import com.spire.doc.Document;

import java.io.File;

public class DocUtil {

    public static void test(String outputDirectory2) {
        //将转换的doc进行合并
        Document document = new Document();
        File[] docfs = new File(outputDirectory2).listFiles();
        for (int i = 0; i < docfs.length; i++) {
            document.insertTextFromFile(outputDirectory2 + "test" + (i + 1) + ".docx", com.spire.doc.FileFormat.Docx_2013);
        }
        document.saveToFile(outputDirectory2 + "test0.docx", com.spire.doc.FileFormat.Docx);
    }
}
