package cn.edu.fc.service;

import cn.edu.fc.dao.DataDao;
import cn.edu.fc.dao.bo.Data;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.service.dto.DataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataService {
    private static final Logger logger = LoggerFactory.getLogger(DataService.class);

    private DataDao dataDao;

    @Autowired
    public DataService(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    public PageDto<DataDto> retrieveDataByStoreId(Long storeId, Integer page, Integer pageSize) {
        List<Data> dataList = this.dataDao.retrieveByStoreId(storeId, page, pageSize);
        List<DataDto> ret = dataList.stream().map(obj -> {
            DataDto dto = DataDto.builder().storeId(obj.getStoreId()).date(obj.getDate()).beginTime(obj.getBeginTime()).endTime(obj.getEndTime()).num(obj.getNum()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public PageDto<DataDto> retrieveDataByStoreIdAndDateBetween(Long storeId, LocalDate beginDate, LocalDate endDate, Integer page, Integer pageSize) {
        List<Data> dataList = this.dataDao.retrieveByStoreIdAndDateBetween(storeId, beginDate, endDate, page, pageSize);
        List<DataDto> ret = dataList.stream().map(obj -> {
            DataDto dto = DataDto.builder().storeId(obj.getStoreId()).date(obj.getDate()).beginTime(obj.getBeginTime()).endTime(obj.getEndTime()).num(obj.getNum()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public PageDto<DataDto> retrieveDataByStoreIdAndDate(Long storeId, LocalDate date, Integer page, Integer pageSize) {
        List<Data> dataList = this.dataDao.retrieveByStoreIdAndDate(storeId, date, page, pageSize);
        List<DataDto> ret = dataList.stream().map(obj -> {
            DataDto dto = DataDto.builder().storeId(obj.getStoreId()).date(obj.getDate()).beginTime(obj.getBeginTime()).endTime(obj.getEndTime()).num(obj.getNum()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public DataDto findDataStoreIdAndDateAndBeginTimeAndEndTime(Long storeId, LocalDate date, String beginTime, String endTime) {
        Data data = this.dataDao.findByStoreIdAndDateAndBeginTimeAndEndTime(storeId, date, beginTime, endTime);
        DataDto dto = DataDto.builder().storeId(data.getStoreId()).date(data.getDate()).beginTime(data.getBeginTime()).endTime(data.getEndTime()).num(data.getNum()).build();
        return dto;
    }
}
