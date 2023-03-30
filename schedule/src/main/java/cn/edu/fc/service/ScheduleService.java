package cn.edu.fc.service;

import cn.edu.fc.dao.StaffScheduleDao;
import cn.edu.fc.dao.bo.*;
import cn.edu.fc.dao.openfeign.DataDao;
import cn.edu.fc.dao.openfeign.RuleDao;
import cn.edu.fc.dao.openfeign.StaffDao;
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
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.fc.javaee.core.model.Constants.MAX_RETURN;

@Service
public class ScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    private DataDao dataDao;
    private RuleDao ruleDao;
    private StaffDao staffDao;
    private StaffScheduleDao staffScheduleDao;
    private Scheduler scheduler;

    @Autowired
    public ScheduleService(DataDao dataDao, RuleDao ruleDao, StaffDao staffDao, StaffScheduleDao staffScheduleDao, Scheduler scheduler) {
        this.dataDao = dataDao;
        this.ruleDao = ruleDao;
        this.staffDao = staffDao;
        this.staffScheduleDao = staffScheduleDao;
        this.scheduler = scheduler;
    }

    public PageDto<StaffSchedule> retrieveScheduleByDay(Long storeId, LocalDate date, UserDto user) {
        List<StaffSchedule> ret = staffScheduleDao.retrieveByByStartGreaterThanEqualAndEndLessThanEqual(date.atStartOfDay(), date.plusDays(1).atStartOfDay(), 0, MAX_RETURN);
        if (null == ret || ret.isEmpty()) {
            logger.info("No schedule found for day {}, generating...", date);
            this.generateSchedule(storeId, date, user);
            ret = staffScheduleDao.retrieveByByStartGreaterThanEqualAndEndLessThanEqual(date.atStartOfDay(), date.plusDays(1).atStartOfDay(), 0, MAX_RETURN);
        }
        return new PageDto<>(ret, 0, ret.size());
    }

    public PageDto<StaffSchedule> retrieveScheduleByDayAndSkill(Long storeId, LocalDate date, String skill, UserDto user) {
        // TODO: implement
        return this.retrieveScheduleByDay(storeId, date, user);
    }

    public PageDto<StaffSchedule> retrieveScheduleByDayAndPosition(Long storeId, LocalDate date, String position, UserDto user) {
        // TODO: implement
        return this.retrieveScheduleByDay(storeId, date, user);
    }

    public PageDto<StaffSchedule> retrieveScheduleByDayAndStaff(Long storeId, LocalDate date, Long staffId, UserDto user) {
        List<StaffSchedule> ret = this.retrieveScheduleByDay(storeId, date, user)
                .getList()
                .stream()
                .filter(schedule -> schedule.getStaffId().equals(staffId))
                .collect(Collectors.toList());
        return new PageDto<>(ret, 0, ret.size());
    }

    public PageDto<StaffSchedule> retrieveScheduleByWeek(Long storeId, LocalDate date, UserDto user) {
        List<StaffSchedule> ret = staffScheduleDao.retrieveByByStartGreaterThanEqualAndEndLessThanEqual(date.atStartOfDay(), date.plusDays(7).atStartOfDay(), 0, MAX_RETURN);
        if (null == ret || ret.isEmpty()) {
            logger.info("No schedule found for week begin at {}, generating...", date);
            this.generateSchedule(storeId, date, user);
            ret = staffScheduleDao.retrieveByByStartGreaterThanEqualAndEndLessThanEqual(date.atStartOfDay(), date.plusDays(7).atStartOfDay(), 0, MAX_RETURN);
        }
        return new PageDto<>(ret, 0, ret.size());
    }

    public PageDto<StaffSchedule> retrieveScheduleByWeekAndSkill(Long storeId, LocalDate date, String skill, UserDto user) {
        // TODO: implement
        return this.retrieveScheduleByWeek(storeId, date, user);
    }

    public PageDto<StaffSchedule> retrieveScheduleByWeekAndPosition(Long storeId, LocalDate date, String position, UserDto user) {
        // TODO: implement
        return this.retrieveScheduleByWeek(storeId, date, user);
    }

    public PageDto<StaffSchedule> retrieveScheduleByWeekAndStaff(Long storeId, LocalDate date, Long staffId, UserDto user) {
        List<StaffSchedule> ret = this.retrieveScheduleByWeek(storeId, date, user)
                .getList()
                .stream()
                .filter(schedule -> schedule.getStaffId().equals(staffId))
                .collect(Collectors.toList());
        return new PageDto<>(ret, 0, ret.size());
    }

    private void generateSchedule(Long storeId, LocalDate date, UserDto user) {
        InternalReturnObject<PageDto<Data>> dataResp = dataDao.retrieveWeekData(storeId, date);
        List<Data> data = this.checkResponse(dataResp).getList();
        InternalReturnObject<PageDto<Staff>> staffResp = staffDao.retrieveAllStaffsByShopId(storeId);
        List<Staff> staffs = this.checkResponse(staffResp).getList();
        InternalReturnObject<List<String>> staffPositionResp = staffDao.retrieveAllStaffPositions();
        List<String> staffPositions = this.checkResponse(staffPositionResp);
        InternalReturnObject<AllRules> ruleResp = ruleDao.getAllRulesByStoreId(storeId);
        AllRules rules = this.checkResponse(ruleResp);

        ScheduleResult result = scheduler.schedule(data, staffs, staffPositions, date, rules);
        result.stream().forEach(schedule -> staffScheduleDao.insert(schedule, user));
    }

    private <T> T checkResponse(InternalReturnObject<T> resp) {
        if (resp.getErrno() != ReturnNo.OK.getErrNo())
            throw new BusinessException(ReturnNo.getByCode(resp.getErrno()), resp.getErrmsg());
        return resp.getData();
    }
}