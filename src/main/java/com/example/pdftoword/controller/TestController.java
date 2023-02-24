package com.example.pdftoword.controller;

import com.example.pdftoword.utils.FileUtil;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
public class TestController {

    @PostMapping("pdfToDoc")
    public void pdfToDoc(@RequestParam("pdfFile") MultipartFile pdfFile, HttpServletResponse response) {

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        File wordFile = null;
        File file = null;
        try {
            file = FileUtil.multipartToFile(pdfFile);

            PdfDocument pdf = new PdfDocument();

            String projectPath = System.getProperty("user.dir");
            String name = file.getName();
            pdf.loadFromFile(projectPath + "/" + name);

            String fileName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".docx";
            pdf.saveToFile(fileName, FileFormat.DOCX);

            wordFile = new File(fileName);
            response.setHeader("content-type","application/octet-stream");
            response.setContentType("application/octet-stream");

            response.setHeader("Content-Dispositon", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            byte[] buffer = new byte[1024];


            fis = new FileInputStream(wordFile);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (bis != null) {
                try {
                    // 结束后关闭文件流
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    // 结束后关闭文件流
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 最后删除转换过程中生成的文件
            if (wordFile != null) {
                wordFile.delete();
            }
            if (file != null) {
                file.delete();
            }
        }

    }
}
