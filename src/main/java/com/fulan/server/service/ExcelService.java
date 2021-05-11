package com.fulan.server.service;


import com.fulan.server.model.ExcelEntity;

import java.util.List;

public interface ExcelService {

    List<ExcelEntity> select(ExcelEntity excelEntity);

}
