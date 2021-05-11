package com.fulan.server.service.impl;

import com.fulan.server.dao.ExcelDao;
import com.fulan.server.model.BackstageWithdrawDepositInfo;
import com.fulan.server.model.ExcelEntity;
import com.fulan.server.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    private ExcelDao excelDao;

    public List<ExcelEntity> select(ExcelEntity excelEntity) {
        return excelDao.select(excelEntity);
    }

    public List<BackstageWithdrawDepositInfo> findAllDepositInfo() {
        return excelDao.findAllDepositInfo();
    }
}
