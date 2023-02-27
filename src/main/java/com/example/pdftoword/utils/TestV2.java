package com.example.pdftoword.utils;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import java.io.File;

/**
 * 转换只能转换前3页
 * 思路：将pdf切分成多个pdf,再每个pdf转换成word,最后将所有的word合并成一个大的word
 */
public class TestV2 {

    public static void main(String[] args) {
        String inputFile = "C:\\Users\\zhengzhangbiao-jk\\Desktop\\my\\test3.pdf";

        String outputDirectory = "C:\\Users\\zhengzhangbiao-jk\\Desktop\\my\\test\\";
        String outputDirectory2 = "C:\\Users\\zhengzhangbiao-jk\\Desktop\\my\\test2\\";

        PdfDocument sourceDoc = new PdfDocument(inputFile);

        System.out.println(sourceDoc.getPages().getCount());
        //加载PDF文件
        sourceDoc.loadFromFile(inputFile);
        //将 PDF 拆分为单页 PDF
        sourceDoc.split(outputDirectory + "test{0}.pdf", 1);
        //获取这个路径下所有的单页pdf
        File[] fs = new File(outputDirectory).listFiles();
        //将切分好的pdf,一个个转换成word
        for (int i = 0; i < fs.length; i++) {
            PdfDocument pdf = new PdfDocument();
            pdf.loadFromFile(fs[i].getAbsolutePath());
            pdf.saveToFile(outputDirectory2 + fs[i].getName().substring(0, fs[i].getName().length() - 4) + ".docx", FileFormat.DOCX);

        }

        DocUtil.test(outputDirectory2);


    }
}
