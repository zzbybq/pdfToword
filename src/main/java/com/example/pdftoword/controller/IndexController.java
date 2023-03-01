package com.example.pdftoword.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class IndexController {



        @RequestMapping("/index")
        public String index() {
            return "index";
        }


        @RequestMapping("/convert")
        public String convert() {
            return "convert";
        }

        @RequestMapping("/merge")
        public String merge(){return "mergePdf";}

}
