package cn.edu.fc.dao;

import cn.edu.fc.dao.bo.Store;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.util.RedisUtil;
import cn.edu.fc.mapper.StorePoMapper;
import cn.edu.fc.mapper.po.StorePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.fc.javaee.core.model.Constants.MAX_RETURN;

@Repository
@RefreshScope
public class StoreDao {
    private final static Logger logger = LoggerFactory.getLogger(StoreDao.class);

    private final static String KEY = "E%d";

    @Value("3600")
    private int timeout;

    private StorePoMapper storePoMapper;

    private RedisUtil redisUtil;

    @Autowired
    public StoreDao(StorePoMapper storePoMapper, RedisUtil redisUtil) {
        this.storePoMapper = storePoMapper;
        this.redisUtil = redisUtil;
    }

    private Store getBo(StorePo po, Optional<String> redisKey) {
        Store bo = Store.builder().id(po.getId()).name(po.getName()).address(po.getAddress()).size(po.getSize()).build();
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    private StorePo getPo(Store bo) {
        StorePo po = StorePo.builder().id(bo.getId()).name(bo.getName()).address(bo.getAddress()).size(bo.getSize()).build();
        return po;
    }

    public Store findById(Long id) throws RuntimeException {
        if (null == id) {
            return null;
        }

        String key = String.format(KEY, id);

        /*if (redisUtil.hasKey(key)) {
            Store bo = (Store) redisUtil.get(key);
            return bo;
        }*/

        Optional<StorePo> po = this.storePoMapper.findById(id);
        if (po.isPresent()) {
            return this.getBo(po.get(), Optional.of(key));
        } else {
            return null;
        }
    }
}
