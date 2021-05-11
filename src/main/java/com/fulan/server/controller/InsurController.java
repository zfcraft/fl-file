package com.fulan.server.controller;

import com.fulan.server.common.util.ExcelUtils;
import com.fulan.server.model.InsurEntity;
import com.fulan.server.service.InsurService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@Controller
@Slf4j
public class InsurController {
    @Autowired
    private InsurService insurService;

    @RequestMapping("/index")
    public String index() {
        return "static/html/index";
    }

    @RequestMapping("/insert")
    public String insert(@RequestBody InsurEntity insurEntity, @RequestParam MultipartFile file) {

        String fileName = file.getOriginalFilename();
        String lastName = fileName.substring(fileName.lastIndexOf("."), fileName.length()).trim();
      String path = "C://Users/Administrator/Desktop/fulan-demo/upload/保单.xlsx";

        List<InsurEntity> parseExcel  = null;
        try {
            parseExcel = ExcelUtils.getInstance().parseExcel(new FileInputStream(new File(path)), InsurEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (InsurEntity entity : parseExcel) {
            insurService.insertInsur(entity);
        }
        return "list";
    }

}

