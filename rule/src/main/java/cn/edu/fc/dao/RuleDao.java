package cn.edu.fc.dao;

import cn.edu.fc.dao.bo.Rule;
import cn.edu.fc.dao.openfeign.StoreDao;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.Constants;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.dto.UserDto;
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
import static cn.edu.fc.javaee.core.util.Common.putGmtFields;
import static cn.edu.fc.javaee.core.util.Common.putUserFields;

@Repository
@RefreshScope
public class RuleDao {
    private final static Logger logger = LoggerFactory.getLogger(RuleDao.class);

    private final static String KEY = "E%s";

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
        this.setBo(bo);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    private void setBo(Rule bo) {
        bo.setStoreDao(storeDao);
    }

    private RulePo getPo(Rule bo) {
        RulePo po = RulePo.builder().id(bo.getId()).type(bo.getType()).value(bo.getValue()).storeId(bo.getStoreId()).build();
        return po;
    }

    public Rule findById(Long id) throws RuntimeException {
        if (null == id) {
            return null;
        }

        String key = String.format(KEY, id);

        if (redisUtil.hasKey(key)) {
            Rule bo = (Rule) redisUtil.get(key);
            this.setBo(bo);
            return bo;
        }

        Optional<RulePo> po = this.rulePoMapper.findById(id);
        if (po.isPresent()) {
            return this.getBo(po.get(), Optional.of(key));
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "员工偏好", id));
        }
    }

    public List<Rule> retrieveAll(Integer page, Integer pageSize) throws RuntimeException {
        List<RulePo> retList = this.rulePoMapper.findAll(PageRequest.of(0, MAX_RETURN))
                .stream().collect(Collectors.toList());
        if (null == retList || retList.size() == 0)
            return new ArrayList<>();

        List<Rule> ret = retList.stream().map(po->{
            return getBo(po,Optional.ofNullable(null));
        }).collect(Collectors.toList());
        return ret;
    }

    public List<Rule> retrieveByStoreId(Long storeId, Integer page, Integer pageSize) throws RuntimeException {
        List<RulePo> retList = this.rulePoMapper.findByStoreId(storeId, PageRequest.of(0, MAX_RETURN))
                .stream().collect(Collectors.toList());
        if (null == retList || retList.size() == 0)
            return new ArrayList<>();

        List<Rule> ret = retList.stream().map(po->{
            return getBo(po,Optional.ofNullable(null));
        }).collect(Collectors.toList());
        return ret;
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

    public Rule findByTypeAndStoreId(String type, Long storeId) {
        RulePo po = this.rulePoMapper.findByTypeAndStoreId(type, storeId);
        if (null == po) {
            return null;
        }

        return getBo(po, Optional.empty());
    }

    public Long insert(Rule rule, UserDto user) throws RuntimeException {
        RulePo po = this.rulePoMapper.findByTypeAndStoreId(rule.getType(), rule.getStoreId());
        if (null == po) {
            RulePo rulePo = getPo(rule);
            putUserFields(rulePo, "creator", user);
            putGmtFields(rulePo, "create");
            this.rulePoMapper.save(rulePo);
            return rulePo.getId();
        } else {
            throw new BusinessException(ReturnNo.RULE_EXIST, String.format(ReturnNo.RULE_EXIST.getMessage(), rule.getId()));
        }
    }

    public String save(Long ruleId, Rule rule, UserDto user) {
        RulePo po = getPo(rule);
        po.setId(ruleId);
        if (null != user) {
            putUserFields(po, "modifier", user);
            putGmtFields(po, "modified");
        }
        this.rulePoMapper.save(po);
        return String.format(KEY, rule.getId());
    }
}
