package cn.edu.fc.dao;

import cn.edu.fc.dao.bo.Rule;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.util.RedisUtil;
import cn.edu.fc.mapper.RulePoMapper;
import cn.edu.fc.mapper.po.RulePo;
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
public class RuleDao {
    private final static Logger logger = LoggerFactory.getLogger(RuleDao.class);

    private final static String KEY = "E%d";

    @Value("3600")
    private int timeout;

    private RulePoMapper rulePoMapper;

    private RedisUtil redisUtil;

    private StoreDao storeDao;

    @Autowired
    public RuleDao(RulePoMapper rulePoMapper, RedisUtil redisUtil, StoreDao storeDao) {
        this.rulePoMapper = rulePoMapper;
        this.redisUtil = redisUtil;
        this.storeDao = storeDao;
    }

    private Rule getBo(RulePo po, Optional<String> redisKey) {
        Rule bo = Rule.builder().type(po.getType()).value(po.getValue()).storeId(po.getStoreId()).build();
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    public List<Rule> retrieveByStoreId(Long storeId) throws RuntimeException {
        List<RulePo> retList = this.rulePoMapper.findByStoreId(storeId)
                .stream().collect(Collectors.toList());
        if (null == retList || retList.size() == 0)
            return new ArrayList<>();

        List<Rule> ret = retList.stream().map(po->{
            return getBo(po,Optional.ofNullable(null));
        }).collect(Collectors.toList());
        return ret;
    }
}
