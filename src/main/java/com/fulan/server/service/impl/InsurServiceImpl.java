package com.fulan.server.service.impl;

import com.fulan.server.dao.InsurDao;
import com.fulan.server.model.InsurEntity;
import com.fulan.server.service.InsurService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class InsurServiceImpl implements InsurService, Job {


    @Resource
    private InsurDao insurDao;

    public Integer insertInsur(InsurEntity insurEntity) {
        return insurDao.insert(insurEntity);
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}


