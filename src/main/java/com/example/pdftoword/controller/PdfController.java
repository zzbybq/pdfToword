package com.example.pdftoword.controller;

import com.example.pdftoword.utils.CommonUtil;
import com.spire.doc.Document;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.UUID;

@Controller
@Slf4j
public class PdfController {

    @PostMapping("/convertToDoc")
    public ResponseEntity<byte[]> convertToDoc(@RequestParam("uploadFile") MultipartFile uploadFile) throws Exception {

        //文件名(包含.pdf)
        String fileName = uploadFile.getOriginalFilename();
        //项目根目录
        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
        //UUID
        String uuid = CommonUtil.getUUID();
        //项目根目录下上传目录
        File upload = new File(path, "static/upload/");
        if (!upload.exists()) {
            upload.mkdirs();
        }
        //保存文件
        String pathName = upload.getAbsolutePath() + File.separator + fileName;
        File file = new File(pathName);
        FileUtils.copyInputStreamToFile(uploadFile.getInputStream(), file);

        //再调用pdf的拆分方法拆分单个pdf
        PdfDocument sourceDoc = new PdfDocument(pathName);
        //加载PDF文件
        sourceDoc.loadFromFile(pathName);
        //将 PDF 拆分为单页 PDF
        String outputDirectory = upload.getAbsolutePath() + File.separator + uuid;
        sourceDoc.split(outputDirectory + File.separator + "test{0}.pdf", 1);
        //再转化doc
        File[] fs = new File(outputDirectory).listFiles();
        for (int i = 0; i < fs.length; i++) {
            PdfDocument pdf = new PdfDocument();
            pdf.loadFromFile(fs[i].getAbsolutePath());
            pdf.saveToFile(outputDirectory + fs[i].getName().substring(0, fs[i].getName().length() - 4) + ".docx", FileFormat.DOCX);
        }

        //将转换的doc进行合并
        Document document = new Document();
        File[] docfs = new File(outputDirectory).listFiles();
        for (int i = 0; i < docfs.length; i++) {
            if (docfs[i].isDirectory()) {
                continue;
            }

            document.insertTextFromFile(outputDirectory + "test" + (i + 1) + ".docx", com.spire.doc.FileFormat.Docx_2013);
        }
        document.saveToFile(outputDirectory + "test0.docx", com.spire.doc.FileFormat.Docx);

        //将转化的doc合并起来

        //删除过程文件

        return null;
    }
}
