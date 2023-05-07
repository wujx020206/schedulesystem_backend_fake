package cn.edu.fc.service;

import cn.edu.fc.dao.RuleDao;
import cn.edu.fc.dao.StoreDao;
import cn.edu.fc.dao.bo.Rule;
import cn.edu.fc.dao.bo.Store;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.service.dto.RuleDto;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RuleService {
    private static final Logger logger = LoggerFactory.getLogger(RuleService.class);

    private final RuleDao ruleDao;

    private final StoreDao storeDao;

    @Autowired
    public RuleService(RuleDao ruleDao, StoreDao storeDao) {
        this.ruleDao = ruleDao;
        this.storeDao = storeDao;
    }

    public PageDto<RuleDto> retrieveRules(Integer page, Integer pageSize) {
        List<Rule> preferences = this.ruleDao.retrieveAll(page, pageSize);
        List<RuleDto> ret = preferences.stream().map(obj -> {
            String[] arr = obj.getType().split("_");
            RuleDto dto = RuleDto.builder().firstType(arr[0]).secondType(arr[1]).value(obj.getValue()).shopName(storeDao.findById(obj.getStoreId()).getName()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public PageDto<RuleDto> retrieveRulesByStoreId(Long storeId, Integer page, Integer pageSize) {
        Store store = this.storeDao.findById(storeId);
        if (null == store) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "门店", storeId));
        }

        List<Rule> preferences1 = this.ruleDao.retrieveByStoreId(storeId, page, pageSize);
        List<Rule> preferences2 = this.ruleDao.retrieveByStoreId(null, page, pageSize);
        List<Rule> preferences = Lists.newArrayList();
        preferences.addAll(preferences1);
        preferences.addAll(preferences2);

        List<RuleDto> ret = preferences.stream().map(obj -> {
            String[] arr = obj.getType().split("_");
            RuleDto dto = RuleDto.builder().firstType(arr[0]).secondType(arr[1]).value(obj.getValue()).shopName(storeDao.findById(obj.getStoreId()).getName()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public RuleDto findByStoreIdAndType(Long storeId, String type) {
        Rule bo = this.ruleDao.findByTypeAndStoreId(type, storeId);
        if (null == bo) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "门店排班规则", null));
        }

        String[] arr = bo.getType().split("_");
        RuleDto dto = RuleDto.builder().firstType(arr[0]).secondType(arr[1]).value(bo.getValue()).shopName(storeDao.findById(storeId).getName()).build();
        return dto;
    }

    public void updateRule(String type, Long storeId, String value) {
        Rule bo = this.ruleDao.findByTypeAndStoreId(type, storeId);
        if (null == bo) {
            Rule rule = Rule.builder().type(type).value(value).storeId(storeId).build();
            this.ruleDao.insert(rule);
        } else {
            bo.setValue(value);
            this.ruleDao.save(bo.getId(), bo);
        }
    }
}
