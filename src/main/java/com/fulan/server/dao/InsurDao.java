package com.fulan.server.dao;

import com.fulan.server.model.InsurEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InsurDao {
	@Insert("insert into insurance(car_num,car_frame,car_eng,value,login_time,insur_time) " +
			"values(#{car_num},#{car_frame},#{car_eng},#{value},#{login_time},#{insur_time})")
	Integer insert(InsurEntity insurEntity);
}
