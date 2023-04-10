package cn.edu.fc.service;

import cn.edu.fc.dao.DataDao;
import cn.edu.fc.dao.StaffDao;
import cn.edu.fc.dao.StaffScheduleDao;
import cn.edu.fc.dao.StoreDao;
import cn.edu.fc.dao.bo.*;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.InternalReturnObject;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.fc.javaee.core.model.Constants.MAX_RETURN;

@Service
public class ScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    private DataDao dataDao;

    private RuleDao ruleDao;

    private StoreDao storeDao;

    private StaffDao staffDao;

    private StaffScheduleDao staffScheduleDao;

    private Scheduler scheduler;

    @Autowired
    public ScheduleService(DataDao dataDao, RuleDao ruleDao, StoreDao storeDao, StaffDao staffDao, StaffScheduleDao staffScheduleDao, Scheduler scheduler) {
        this.dataDao = dataDao;
        this.ruleDao = ruleDao;
        this.storeDao = storeDao;
        this.staffDao = staffDao;
        this.staffScheduleDao = staffScheduleDao;
        this.scheduler = scheduler;
    }

    public PageDto<StaffSchedule> retrieveScheduleByDay(Long storeId, LocalDate date) {
        List<StaffSchedule> ret = staffScheduleDao.retrieveByStartGreaterThanEqualAndEndLessThanEqual(date.atStartOfDay(), date.plusDays(1).atStartOfDay(), 0, MAX_RETURN);
        if (null == ret || ret.isEmpty()) {
            logger.info("No schedule found for day {}, generating...", date);
            this.generateSchedule(storeId, date);
            ret = staffScheduleDao.retrieveByStartGreaterThanEqualAndEndLessThanEqual(date.atStartOfDay(), date.plusDays(1).atStartOfDay(), 0, MAX_RETURN);
        }
        return new PageDto<>(ret, 0, ret.size());
    }

    public PageDto<StaffSchedule> retrieveScheduleByDayAndSkill(Long storeId, LocalDate date, String skill) {
        // TODO: implement
        return this.retrieveScheduleByDay(storeId, date);
    }

    public PageDto<StaffSchedule> retrieveScheduleByDayAndPosition(Long storeId, LocalDate date, String position) {
        // TODO: implement
        return this.retrieveScheduleByDay(storeId, date);
    }

    public PageDto<StaffSchedule> retrieveScheduleByDayAndStaff(Long storeId, LocalDate date, Long staffId) {
        List<StaffSchedule> ret = this.retrieveScheduleByDay(storeId, date)
                .getList()
                .stream()
                .filter(schedule -> schedule.getStaffId().equals(staffId))
                .collect(Collectors.toList());
        return new PageDto<>(ret, 0, ret.size());
    }

    public PageDto<StaffSchedule> retrieveScheduleByWeek(Long storeId, LocalDate date) {
        List<StaffSchedule> ret = staffScheduleDao.retrieveByStartGreaterThanEqualAndEndLessThanEqual(date.atStartOfDay(), date.plusDays(7).atStartOfDay(), 0, MAX_RETURN);
        if (null == ret || ret.isEmpty()) {
            logger.info("No schedule found for week begin at {}, generating...", date);
            this.generateSchedule(storeId, date);
            ret = staffScheduleDao.retrieveByStartGreaterThanEqualAndEndLessThanEqual(date.atStartOfDay(), date.plusDays(7).atStartOfDay(), 0, MAX_RETURN);
        }
        return new PageDto<>(ret, 0, ret.size());
    }

    public PageDto<StaffSchedule> retrieveScheduleByWeekAndSkill(Long storeId, LocalDate date, String skill) {
        // TODO: implement
        return this.retrieveScheduleByWeek(storeId, date);
    }

    public PageDto<StaffSchedule> retrieveScheduleByWeekAndPosition(Long storeId, LocalDate date, String position) {
        // TODO: implement
        return this.retrieveScheduleByWeek(storeId, date);
    }

    public PageDto<StaffSchedule> retrieveScheduleByWeekAndStaff(Long storeId, LocalDate date, Long staffId) {
        List<StaffSchedule> ret = this.retrieveScheduleByWeek(storeId, date)
                .getList()
                .stream()
                .filter(schedule -> schedule.getStaffId().equals(staffId))
                .collect(Collectors.toList());
        return new PageDto<>(ret, 0, ret.size());
    }

    private void generateSchedule(Long storeId, LocalDate date) {
        List<Data> data = this.dataDao.retrieveByStoreIdAndDate(storeId, date);
        List<Staff> staffs = this.staffDao.retrieveByShopId(storeId, 0, MAX_RETURN);
        List<String> staffPositions = new ArrayList<>();
        staffPositions.add("门店经理");
        staffPositions.add("副经理");
        staffPositions.add("小组长");
        staffPositions.add("店员（收银）");
        staffPositions.add("店员（导购）");
        staffPositions.add("店员（库房）");

        List<Rule> rules = this.ruleDao.retrieveByStoreId(storeId);
        Integer preparePeople = (int)Math.ceil(storeDao.findById(findRule(rules,"自定义规则_准备工作人数").getStoreId()).getSize()/Double.parseDouble(findRule(rules, "自定义规则_准备工作人数").getValue()));
        Integer endPeople = (int)Math.ceil(storeDao.findById(findRule(rules,"自定义规则_收尾工作人数").getStoreId()).getSize()/Double.parseDouble(findRule(rules, "自定义规则_收尾工作人数").getValue().split(" ")[0]))+Integer.parseInt(findRule(rules, "自定义规则_收尾工作人数").getValue().split(" ")[1]);

        AllRules allRules = AllRules.builder().weekDayOpenRule(Integer.valueOf(findRule(rules, "固定规则_工作日开店规则").getValue()))
                .weekDayCloseRule(Integer.valueOf(findRule(rules, "固定规则_工作日关店规则").getValue()))
                .weekendOpenRule(Integer.valueOf(findRule(rules, "固定规则_周末开店规则").getValue()))
                .weekendCloseRule(Integer.valueOf(findRule(rules, "固定规则_周末关店规则").getValue()))
                .maxHourPerWeek(Integer.valueOf(findRule(rules, "固定规则_员工每周工作时长").getValue()))
                .maxHourPerDay(Integer.valueOf(findRule(rules, "固定规则_员工每天工作时长").getValue()))
                .leastHourPerPeriod(Integer.valueOf(findRule(rules, "固定规则_单班次最短时长").getValue()))
                .maxContinuousWorkTime(Integer.valueOf(findRule(rules, "固定规则_员工最长连续工作时长").getValue()))
                .maxHourPerPeriod(Integer.valueOf(findRule(rules, "固定规则_单班次最长时长").getValue()))
                .lunchBegin(Integer.valueOf(findRule(rules, "固定规则_午餐开始时间").getValue()))
                .lunchEnd(Integer.valueOf(findRule(rules, "固定规则_午餐结束时间").getValue()))
                .dinnerBegin(Integer.valueOf(findRule(rules, "固定规则_晚餐开始时间").getValue()))
                .dinnerEnd(Integer.valueOf(findRule(rules, "固定规则_晚餐结束时间").getValue()))
                .breakTime(LocalTime.parse(findRule(rules, "固定规则_休息时长").getValue()))
                .prepareTime(Integer.valueOf(findRule(rules, "自定义规则_准备工作时长").getValue()))
                .preparePeople(preparePeople)
                .prepareStation(Arrays.asList(findRule(rules,"自定义规则_准备工作职位").getValue().split(" ")))
                .workPeople(Float.valueOf(findRule(rules, "自定义规则_工作店员需求数").getValue()))
                .workStation(Arrays.asList(findRule(rules,"自定义规则_工作职位").getValue().split(" ")))
                .leastPeople(Integer.valueOf(findRule(rules, "自定义规则_无客流量店员数").getValue()))
                .endTime(Integer.valueOf(findRule(rules, "自定义规则_收尾工作时长").getValue()))
                .endPeople(endPeople)
                .endStation(Arrays.asList(findRule(rules,"自定义规则_收尾工作职位").getValue().split(" ")))
                .build();

        ScheduleResult result = scheduler.schedule(data, staffs, staffPositions, date, allRules);
        result.stream().forEach(schedule -> staffScheduleDao.insert(schedule));
    }

    public Rule findRule(List<Rule> rules, String type) {
        for (Rule rule : rules) {
            if (type.equals(rule.getType())) {
                return rule;
            }
        }
        throw new BusinessException(ReturnNo.RULE_NOT_FIND, String.format(ReturnNo.RULE_NOT_FIND.getMessage(), type));
    }

    private <T> T checkResponse(InternalReturnObject<T> resp) {
        if (resp.getErrno() != ReturnNo.OK.getErrNo())
            throw new BusinessException(ReturnNo.getByCode(resp.getErrno()), resp.getErrmsg());
        return resp.getData();
    }
}