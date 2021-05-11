package com.fulan.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.fulan.server.common.util.ExcelUtil;
import com.fulan.server.model.BackstageWithdrawDepositInfo;
import com.fulan.server.model.ExcelEntity;
import com.fulan.server.service.ExcelService;
import com.fulan.server.service.InsurService;
import com.fulan.server.service.impl.ExcelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ExcelController {
    @Autowired
    private ExcelService excelService;
    @Autowired
    private ExcelServiceImpl excelServiceImpl;
    private ExcelUtil excelUtil;
    @Value("${file.upload.url}")
    private String uploadFilePath;

    @RequestMapping("/exportExcel")
    @ResponseBody
    public JSONObject excel(ExcelEntity excelEntity, HttpServletResponse httpServletResponse) throws IOException {
        BackstageWithdrawDepositInfo backstageWithdrawDepositInfo = new BackstageWithdrawDepositInfo();
        JSONObject jsonObject = new JSONObject();
        File file = new File(uploadFilePath + "\\" + "excelTest.xlsx");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

//        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
//        byte[] buff = new byte[1024];
        OutputStream os = httpServletResponse.getOutputStream();
//        int i = 0;
//        while ((i = bis.read(buff)) != -1) {
//            os.write(buff, 0, i);
//            os.flush();
//        }
        String sheetName = "1";
        List<BackstageWithdrawDepositInfo> list = excelServiceImpl.findAllDepositInfo();
        System.out.println("list = " + list);
//        excelUtil.exportExcel(list, sheetName, os);

        jsonObject.put("code", 200);
        jsonObject.put("meg", "上传成功！");
        return jsonObject;
    }

    @RequestMapping("/upload")
    @ResponseBody
    public JSONObject httpUpload(@RequestParam("files") MultipartFile[] files) throws IOException {
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getOriginalFilename();
            String fileName1 = files[i].getName();
            System.out.println("fileName1 = " + fileName1);
            System.out.println("fileName = " + fileName);
            File dest = new File(uploadFilePath + "\\" + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            files[i].transferTo(dest);
        }

        jsonObject.put("code", 200);
        jsonObject.put("meg", "上传成功！");
        return jsonObject;
    }


}
