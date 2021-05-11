package com.fulan.server.dao;

import com.fulan.server.model.BackstageWithdrawDepositInfo;
import com.fulan.server.model.ExcelEntity;
import com.fulan.server.model.InsurEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExcelDao {
    @Select("select title,import_peo,import_time,load_time,status,result,operation from excel")
    List<ExcelEntity> select(ExcelEntity excelEntity);
    @Select("select * from testexcel")
    List<BackstageWithdrawDepositInfo> findAllDepositInfo();
}