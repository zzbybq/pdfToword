package com.example.pdftoword.service.impl;

import com.example.pdftoword.service.DocOperateService;
import com.spire.doc.Document;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import java.io.File;
import static com.example.pdftoword.domain.Constants.SPLIT;
import static com.example.pdftoword.domain.FileTypeEnum.DOC;

@Service
@Slf4j
public class DocOperateServiceImpl implements DocOperateService {


    @Override
    public byte[] mergeDocs(String docFilePath, ByteArrayOutputStream outputStream) {
        //将转换的doc进行合并
        long startTime = System.currentTimeMillis();
        log.info("合并doc开始,startTime={}", startTime);
        Document document = new Document();
        File[] docfs = new File(docFilePath).listFiles();
        if (docfs == null) {
            return null;
        }
        for (int i = 0; i < docfs.length; i++) {
            document.insertTextFromFile(docFilePath + SPLIT + (i + 1) + DOC.getSuffix(), com.spire.doc.FileFormat.Docx_2013);
        }
        document.saveToStream(outputStream, com.spire.doc.FileFormat.Docx);
        log.info("合并doc结束,耗时={}s", (System.currentTimeMillis() - startTime) / 1000);
        return outputStream.toByteArray();
    }
}
