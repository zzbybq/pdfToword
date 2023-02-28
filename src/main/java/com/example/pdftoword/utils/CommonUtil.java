package com.example.pdftoword.utils;

import java.util.UUID;

public class CommonUtil {

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
