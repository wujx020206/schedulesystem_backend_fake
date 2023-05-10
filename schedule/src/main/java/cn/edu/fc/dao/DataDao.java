package cn.edu.fc.dao;

import cn.edu.fc.dao.bo.Data;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.util.RedisUtil;
import cn.edu.fc.mapper.DataPoMapper;
import cn.edu.fc.mapper.po.DataPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.fc.javaee.core.model.Constants.MAX_RETURN;

@Repository
@RefreshScope
public class DataDao {
    private final static Logger logger = LoggerFactory.getLogger(DataDao.class);

    private final static String KEY = "E%d";

    @Value("3600")
    private int timeout;

    private DataPoMapper dataPoMapper;

    private RedisUtil redisUtil;

    private StoreDao storeDao;

    @Autowired
    public DataDao(DataPoMapper dataPoMapper, RedisUtil redisUtil, StoreDao storeDao) {
        this.dataPoMapper = dataPoMapper;
        this.redisUtil = redisUtil;
        this.storeDao = storeDao;
    }

    private Data getBo(DataPo po, Optional<String> redisKey) {
        Data bo = Data.builder().storeId(po.getStoreId()).date(po.getDate()).beginTime(po.getBeginTime()).endTime(po.getEndTime()).num(po.getNum()).build();
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    public List<Data> retrieveByStoreIdAndDate(Long storeId, LocalDate date) {
        List<DataPo> retList = this.dataPoMapper.findByStoreIdAndDate(storeId, date, PageRequest.of(0, MAX_RETURN))
                .stream().collect(Collectors.toList());
        if (null == retList || retList.size() == 0)
            return new ArrayList<>();

        List<Data> ret = retList.stream().map(po->{
            return getBo(po,Optional.ofNullable(null));
        }).collect(Collectors.toList());
        return ret;
    }
}
