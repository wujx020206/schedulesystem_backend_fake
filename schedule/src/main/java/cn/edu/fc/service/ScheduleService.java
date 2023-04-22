package cn.edu.fc.service;

import cn.edu.fc.dao.*;
import cn.edu.fc.dao.bo.*;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.InternalReturnObject;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.scheduler.Scheduler;
import cn.edu.fc.service.dto.StaffDto;
import cn.edu.fc.service.dto.StaffScheduleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.fc.javaee.core.model.Constants.MAX_RETURN;

@Service
public class ScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    private final DataDao dataDao;

    private final RuleDao ruleDao;

    private final StoreDao storeDao;

    private final StaffDao staffDao;

    private final StaffScheduleDao staffScheduleDao;

    private final Scheduler scheduler;

    @Autowired
    public ScheduleService(DataDao dataDao, RuleDao ruleDao, StoreDao storeDao, StaffDao staffDao, StaffScheduleDao staffScheduleDao, Scheduler scheduler) {
        this.dataDao = dataDao;
        this.ruleDao = ruleDao;
        this.storeDao = storeDao;
        this.staffDao = staffDao;
        this.staffScheduleDao = staffScheduleDao;
        this.scheduler = scheduler;
    }

    private StaffScheduleDto getDto(StaffSchedule bo) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StaffDto staffDto = StaffDto.builder()
                .id(bo.getStaffId() == null ? null : bo.getStaffId())
                .name(bo.getStaffId() == null ? null : this.staffDao.findById(bo.getStaffId()).getName())
                .position(bo.getStaffId() == null ? null : this.staffDao.findById(bo.getStaffId()).getPosition())
                .build();
        return StaffScheduleDto.builder()
                .staff(staffDto)
//                .staffId(bo.getStaffId() == null ? null : bo.getStaffId())
//                .staffName(bo.getStaffId() == null ? null : bo.getStaff().getName())
//                .staffPosition(bo.getStaffId() == null ? null : bo.getStaff().getPosition())
                .startTime(df.format(bo.getStart()))
                .endTime(df.format(bo.getEnd()))
                .build();
    }

    //@JsonIgnore
    public PageDto<StaffScheduleDto> retrieveScheduleByDay(Long storeId, LocalDate date) {
        List<StaffSchedule> ret = staffScheduleDao.retrieveByStartGreaterThanEqualAndEndLessThanEqual(date.atStartOfDay(), date.plusDays(1).atStartOfDay(), 0, MAX_RETURN);
        if (null == ret || ret.isEmpty()) {
            logger.info("No schedule found for day {}, generating...", date);
            this.generateSchedule(storeId, date);
            ret = staffScheduleDao.retrieveByStartGreaterThanEqualAndEndLessThanEqual(date.atStartOfDay(), date.plusDays(1).atStartOfDay(), 0, MAX_RETURN);
        }
        List<StaffScheduleDto> dtos = ret.stream().map(this::getDto).collect(Collectors.toList());
        return new PageDto<>(dtos, 0, MAX_RETURN);
    }

    public PageDto<StaffScheduleDto> retrieveScheduleByDayAndSkill(Long storeId, LocalDate date, String skill) {
        List<StaffScheduleDto> ret = this.retrieveScheduleByDay(storeId, date)
                .getList()
                .stream()
                .filter(staff -> staff.getStaff() != null && staff.getStaff().getPosition().contains(skill))
                .collect(Collectors.toList());
        return new PageDto<>(ret, 0, ret.size());
    }

    public PageDto<StaffScheduleDto> retrieveScheduleByDayAndPosition(Long storeId, LocalDate date, String position) {
        List<StaffScheduleDto> ret = this.retrieveScheduleByDay(storeId, date)
                .getList()
                .stream()
                .filter(staff -> staff.getStaff() != null && staff.getStaff().getPosition().contains(position))
                .collect(Collectors.toList());
        return new PageDto<>(ret, 0, ret.size());
    }

    public PageDto<StaffScheduleDto> retrieveScheduleByDayAndStaff(Long storeId, LocalDate date, Long staffId) {
        List<StaffScheduleDto> ret = this.retrieveScheduleByDay(storeId, date)
                .getList()
                .stream()
                .filter(schedule -> schedule.getStaff() != null && schedule.getStaff().getId().equals(staffId))
                .collect(Collectors.toList());
        return new PageDto<>(ret, 0, ret.size());
    }

    public PageDto<StaffScheduleDto> retrieveScheduleByWeek(Long storeId, LocalDate date) {
        List<StaffSchedule> ret = staffScheduleDao.retrieveByStartGreaterThanEqualAndEndLessThanEqual(date.atStartOfDay(), date.plusDays(7).atStartOfDay(), 0, MAX_RETURN);
        if (null == ret || ret.isEmpty()) {
            logger.info("No schedule found for week begin at {}, generating...", date);
            this.generateSchedule(storeId, date);
            ret = staffScheduleDao.retrieveByStartGreaterThanEqualAndEndLessThanEqual(date.atStartOfDay(), date.plusDays(7).atStartOfDay(), 0, MAX_RETURN);
        }
        List<StaffScheduleDto> dtos = ret.stream().map(this::getDto).collect(Collectors.toList());
        return new PageDto<>(dtos, 0, dtos.size());
    }

    public PageDto<StaffScheduleDto> retrieveScheduleByWeekAndSkill(Long storeId, LocalDate date, String skill) {
        List<StaffScheduleDto> ret = this.retrieveScheduleByWeek(storeId, date)
                .getList()
                .stream()
                .filter(staff -> staff.getStaff() != null && staff.getStaff().getPosition().contains(skill))
                .collect(Collectors.toList());
        return new PageDto<>(ret, 0, ret.size());
    }

    public PageDto<StaffScheduleDto> retrieveScheduleByWeekAndPosition(Long storeId, LocalDate date, String position) {
        List<StaffScheduleDto> ret = this.retrieveScheduleByDay(storeId, date)
                .getList()
                .stream()
                .filter(staff -> staff.getStaff() != null && staff.getStaff().getPosition().contains(position))
                .collect(Collectors.toList());
        return new PageDto<>(ret, 0, ret.size());
    }

    public PageDto<StaffScheduleDto> retrieveScheduleByWeekAndStaff(Long storeId, LocalDate date, Long staffId) {
        List<StaffScheduleDto> ret = this.retrieveScheduleByWeek(storeId, date)
                .getList()
                .stream()
                .filter(schedule -> schedule.getStaff() != null && schedule.getStaff().getId().equals(staffId))
                .collect(Collectors.toList());
        return new PageDto<>(ret, 0, ret.size());
    }

    public void updateStaffSchedule(Long storeId, Long id, String name) {
        Staff staff = this.staffDao.retrieveByName(name, 0, MAX_RETURN).get(0);
//        if (storeId != staff.getStoreId()) {
//            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "员工", staff.getId()));
//        }
        StaffSchedule bo = this.staffScheduleDao.findById(id);
        if (null != staff) {
            StaffSchedule staffSchedule = StaffSchedule.builder().staffId(staff.getId()).start(bo.getStart()).end(bo.getEnd()).build();
            this.staffScheduleDao.save(id, staffSchedule);
        }
    }

    public void deleteById(Long id) {
        StaffSchedule bo = this.staffScheduleDao.findById(id);
        if (null == bo) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "排版安排", id));
        }

        this.staffScheduleDao.delete(id);
    }

    public Long findIdByStaffIdAndStartAndEnd(Long staffId, LocalDateTime start, LocalDateTime end) {
        Long ret = this.staffScheduleDao.findIdByStaffIdAndStartAndEnd(staffId, start, end);
        return ret;
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