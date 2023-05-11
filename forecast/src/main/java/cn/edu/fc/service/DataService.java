package cn.edu.fc.service;

import cn.edu.fc.dao.DataDao;
import cn.edu.fc.dao.StoreDao;
import cn.edu.fc.dao.bo.Data;
import cn.edu.fc.dao.bo.Store;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.service.dto.DataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataService {
    private static final Logger logger = LoggerFactory.getLogger(DataService.class);

    private DataDao dataDao;

    private StoreDao storeDao;

    @Autowired
    public DataService(DataDao dataDao, StoreDao storeDao) {
        this.dataDao = dataDao;
        this.storeDao = storeDao;
    }

    public PageDto<DataDto> retrieveDataByStoreId(Long storeId, Integer page, Integer pageSize) {
        Store store = this.storeDao.findById(storeId);
        if (null == store) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "门店", storeId));
        }

        List<Data> dataList = this.dataDao.retrieveByStoreId(storeId, page, pageSize);
        List<DataDto> ret = dataList.stream().map(obj -> {
            DataDto dto = DataDto.builder().storeName(this.storeDao.findById(obj.getStoreId()).getName()).date(obj.getDate()).beginTime(obj.getBeginTime()).endTime(obj.getEndTime()).num(obj.getNum()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public PageDto<DataDto> retrieveDataByStoreIdAndDateBetween(Long storeId, LocalDate beginDate, LocalDate endDate, Integer page, Integer pageSize) {
        Store store = this.storeDao.findById(storeId);
        if (null == store) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "门店", storeId));
        }

        List<Data> dataList = this.dataDao.retrieveByStoreIdAndDateBetween(storeId, beginDate, endDate, page, pageSize);
        List<DataDto> ret = dataList.stream().map(obj -> {
            DataDto dto = DataDto.builder().storeName(this.storeDao.findById(obj.getStoreId()).getName()).date(obj.getDate()).beginTime(obj.getBeginTime()).endTime(obj.getEndTime()).num(obj.getNum()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public PageDto<DataDto> retrieveDataByStoreIdAndDate(Long storeId, LocalDate date, Integer page, Integer pageSize) {
        Store store = this.storeDao.findById(storeId);
        if (null == store) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "门店", storeId));
        }

        List<Data> dataList = this.dataDao.retrieveByStoreIdAndDate(storeId, date, page, pageSize);
        List<DataDto> ret = dataList.stream().map(obj -> {
            DataDto dto = DataDto.builder().storeName(this.storeDao.findById(obj.getStoreId()).getName()).date(obj.getDate()).beginTime(obj.getBeginTime()).endTime(obj.getEndTime()).num(obj.getNum()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public DataDto findDataStoreIdAndDateAndBeginTimeAndEndTime(Long storeId, LocalDate date, String beginTime, String endTime) {
        Store store = this.storeDao.findById(storeId);
        if (null == store) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "门店", storeId));
        }

        Data data = this.dataDao.findByStoreIdAndDateAndBeginTimeAndEndTime(storeId, date, beginTime, endTime);
        DataDto dto = DataDto.builder().storeName(this.storeDao.findById(data.getStoreId()).getName()).date(data.getDate()).beginTime(data.getBeginTime()).endTime(data.getEndTime()).num(data.getNum()).build();
        return dto;
    }
}
