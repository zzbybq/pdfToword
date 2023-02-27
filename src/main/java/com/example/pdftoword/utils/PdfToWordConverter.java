package com.example.pdftoword.utils;

import java.io.*;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.*;

public class PdfToWordConverter {

    public static void main(String[] args) {
        try {
            //加载PDF
            PdfDocument pdf = new PdfDocument();
            pdf.loadFromFile("C:\\Users\\zhengzhangbiao-jk\\Desktop\\my\\阿里百度美团面试题集合.pdf");
//保存为Word格式
            pdf.saveToFile("ToWord.docx", FileFormat.DOCX);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

