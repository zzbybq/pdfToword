package com.example.pdftoword.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static File multipartToFile(MultipartFile multipartFile) throws IOException {
        File file = null;
        String projectPath = System.getProperty("user.dir");
        file = new File(projectPath + "/prefix_" + multipartFile.getOriginalFilename());

        multipartFile.transferTo(file);
        return file;
    }
}
