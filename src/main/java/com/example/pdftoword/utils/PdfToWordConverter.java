package com.example.pdftoword.utils;

import java.io.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.*;

public class PdfToWordConverter {

    public static void main(String[] args) {
        try {
            // 加载PDF文档
            PDDocument pdf = PDDocument.load(new File("input.pdf"));
            // 创建一个新的Word文档
            XWPFDocument word = new XWPFDocument();
            // 获取PDF中的所有页面并迭代
            for (int i = 0; i < pdf.getNumberOfPages(); i++) {
                PDPage page = pdf.getPage(i);
                // 将PDF页面转换为可编辑文本
                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setSortByPosition(true);
                stripper.setStartPage(i + 1);
                stripper.setEndPage(i + 1);
                String text = stripper.getText(pdf);
                // 创建Word段落并将文本写入段落
                XWPFParagraph para = word.createParagraph();
                XWPFRun run = para.createRun();
                run.setText(text);
            }
            // 将Word文档写入输出文件
            FileOutputStream out = new FileOutputStream(new File("output.docx"));
            word.write(out);
            out.close();
            pdf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

