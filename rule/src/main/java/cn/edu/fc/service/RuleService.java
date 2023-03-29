package cn.edu.fc.service;

import cn.edu.fc.dao.RuleDao;
import cn.edu.fc.dao.bo.Rule;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.service.dto.AllRulesDto;
import cn.edu.fc.service.dto.RuleDto;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RuleService {
    private static final Logger logger = LoggerFactory.getLogger(RuleService.class);

    private final RuleDao ruleDao;

    @Autowired
    public RuleService(RuleDao ruleDao) {
        this.ruleDao = ruleDao;
    }

    public PageDto<RuleDto> retrieveRules(Integer page, Integer pageSize) {
        List<Rule> preferences = this.ruleDao.retrieveAll(page, pageSize);
        List<RuleDto> ret = preferences.stream().map(obj -> {
            String[] arr = obj.getType().split("_");
            RuleDto dto = RuleDto.builder().firstType(arr[0]).secondType(arr[1]).value(obj.getValue()).shopName(obj.getStore().getName()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public PageDto<RuleDto> retrieveRulesByStoreId(Long storeId, Integer page, Integer pageSize) {
        List<Rule> preferences1 = this.ruleDao.retrieveByStoreId(storeId, page, pageSize);
        List<Rule> preferences2 = this.ruleDao.retrieveByStoreId(null, page, pageSize);
        List<Rule> preferences = Lists.newArrayList();
        preferences.addAll(preferences1);
        preferences.addAll(preferences2);

        List<RuleDto> ret = preferences.stream().map(obj -> {
            String[] arr = obj.getType().split("_");
            RuleDto dto = RuleDto.builder().firstType(arr[0]).secondType(arr[1]).value(obj.getValue()).shopName(obj.getStore().getName()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public RuleDto findByStoreIdAndType(Long storeId, String type) {
        Rule bo = this.ruleDao.findByTypeAndStoreId(type, storeId);
        if (null == bo) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "门店排班规则", storeId));
        }

        String[] arr = bo.getType().split("_");
        RuleDto dto = RuleDto.builder().firstType(arr[0]).secondType(arr[1]).value(bo.getValue()).shopName(bo.getStore().getName()).build();
        return dto;
    }

    public void updateRule(String type, Long storeId, String value, UserDto user) {
        Rule bo = this.ruleDao.findByTypeAndStoreId(type, storeId);
        if (null == bo) {
            Rule rule = Rule.builder().type(type).value(value).storeId(storeId).build();
            this.ruleDao.insert(rule, user);
        } else {
            Rule rule = Rule.builder().type(bo.getType()).value(value).storeId(bo.getStoreId()).build();
            this.ruleDao.save(bo.getId(), rule, user);
        }
    }

    public AllRulesDto retrieveAllRules(Long storeId) {
        List<Rule> rules = this.ruleDao.retrieveByStoreId(storeId);
        Long preparePeople = (long)Math.ceil(findRule(rules,"自定义规则_准备工作人数").getStore().getSize()/Long.parseLong(findRule(rules, "自定义规则_准备工作人数").getValue()));
        Float workPeople = (float)Math.ceil(findRule(rules,"自定义规则_工作店员需求数").getStore().getSize()/Long.parseLong(findRule(rules, "自定义规则_工作店员需求数").getValue()));
        Long endPeople = (long)Math.ceil(findRule(rules,"自定义规则_收尾工作人数").getStore().getSize()/Long.parseLong(findRule(rules, "自定义规则_收尾工作人数").getValue().split(" ")[0]))+Long.parseLong(findRule(rules, "自定义规则_收尾工作人数").getValue().split(" ")[1]);

        AllRulesDto allRulesDto = AllRulesDto.builder().weekDayOpenRule(Long.valueOf(findRule(rules, "固定规则_工作日开店规则").getValue()))
                .weekDayCloseRule(Long.valueOf(findRule(rules, "固定规则_工作日关店规则").getValue()))
                .weekendOpenRule(Long.valueOf(findRule(rules, "固定规则_周末开店规则").getValue()))
                .weekendCloseRule(Long.valueOf(findRule(rules, "固定规则_周末关店规则").getValue()))
                .maxHourPerWeek(Long.valueOf(findRule(rules, "固定规则_员工每周工作时长").getValue()))
                .maxHourPerDay(Long.valueOf(findRule(rules, "固定规则_员工每天工作时长").getValue()))
                .leastHourPerPeriod(Long.valueOf(findRule(rules, "固定规则_单班次最短时长").getValue()))
                .maxHourPerPeriod(Long.valueOf(findRule(rules, "固定规则_单班次最长时长").getValue()))
                .lunchBegin(Long.valueOf(findRule(rules, "固定规则_午餐开始时间").getValue()))
                .lunchEnd(Long.valueOf(findRule(rules, "固定规则_午餐结束时间").getValue()))
                .dinnerBegin(Long.valueOf(findRule(rules, "固定规则_晚餐开始时间").getValue()))
                .dinnerEnd(Long.valueOf(findRule(rules, "固定规则_晚餐结束时间").getValue()))
                .breakTime(Long.valueOf(findRule(rules, "固定规则_休息时长").getValue()))
                .prepareTime(Long.valueOf(findRule(rules, "自定义规则_准备工作时长").getValue()))
                .preparePeople(preparePeople)
                .prepareStation(Arrays.asList(findRule(rules,"自定义规则_准备工作职位").getValue().split(" ")))
                .workPeople(workPeople)
                .workStation(Arrays.asList(findRule(rules,"自定义规则_工作职位").getValue().split(" ")))
                .leastPeople(Long.valueOf(findRule(rules, "自定义规则_无客流量店员数").getValue()))
                .endHour(Long.valueOf(findRule(rules, "自定义规则_收尾工作时长").getValue()))
                .endPeople(endPeople)
                .endStation(Arrays.asList(findRule(rules,"自定义规则_收尾工作职位").getValue().split(" ")))
                .build();

        return allRulesDto;
    }

    public Rule findRule(List<Rule> rules, String type) {
        for (Rule rule : rules) {
            if (type == rule.getType()) {
                return rule;
            }
        }
        throw new BusinessException(ReturnNo.RULE_NOT_FIND, String.format(ReturnNo.RULE_NOT_FIND.getMessage(), type));
    }
}
