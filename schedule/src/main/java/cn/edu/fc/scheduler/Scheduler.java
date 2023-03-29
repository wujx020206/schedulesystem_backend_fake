package cn.edu.fc.scheduler;

import cn.edu.fc.dao.bo.*;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.ReturnNo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;

@Component
public class Scheduler {
    private final static int MAX_RANDOM_TIMES = 5;
    private final Random random;
    enum WorkTimeType {
        PREPARE, WORK, END
    }

    public Scheduler() {
        random = new Random();
    }

    public ScheduleResult schedule(List<Data> data, List<Staff> staffs, List<String> staffPositions, LocalDate beginDate, AllRules rules) {
        HashMap<String, Queue<StaffScheduleInternal>> staffByPosition = this.convertStaffsToMap(staffs, staffPositions);
        ScheduleResult result = new ScheduleResult();
        for (int i = 0; i < 7; ++i) {
            LocalDate date = beginDate.plusDays(i);
            List<Integer> needStaffs = this.calculateNeedStaffs(data, beginDate.getDayOfWeek().getValue() < 6, rules);
            ScheduleResult emptyResult = this.createEmptySchedule(needStaffs, date, rules);
            ScheduleResult dayResult = this.applyStaffToSchedule(emptyResult, staffByPosition, rules);
            result.merge(dayResult);
        }
        return result;
    }
    // 填充员工
    private ScheduleResult applyStaffToSchedule(ScheduleResult scheduleResult, HashMap<String, Queue<StaffScheduleInternal>> staffs, AllRules rules) {
        scheduleResult.stream().forEach(schedule -> {
            if (schedule.getStaff() != null)
                return;
            WorkTimeType workTimeType = getWorkTimeType(schedule.getStart(), rules);
            StaffScheduleInternal staff = getStaffWithWorkTimeType(staffs, workTimeType, schedule, rules);
            if (staff == null)
                throw new BusinessException(ReturnNo.NO_ENOUGH_STAFF, ReturnNo.NO_ENOUGH_STAFF.getMessage());
            schedule.setStaff(staff.getStaff());
            if (schedule.getStart().getDayOfMonth() != staff.getLastWorkedHourStart().getDayOfMonth())
                staff.setDayWorkedTime(0);
            staff.setDayWorkedTime(staff.getDayWorkedTime() + schedule.getDuration());
            staff.setWeekWorkedTime(staff.getWeekWorkedTime() + schedule.getDuration());
            staff.setLastWorkedHourStart(schedule.getStart());
            staff.setLastWorkedHourEnd(schedule.getEnd());
            this.recycleStaff(staff, staffs);
        });
        return scheduleResult;
    }
    private StaffScheduleInternal getStaffWithWorkTimeType(HashMap<String, Queue<StaffScheduleInternal>> allStaffs, WorkTimeType type, StaffSchedule emptySchedule, AllRules rules) {
        Function<StaffScheduleInternal, Boolean> validate = schedule -> {
            if (schedule.getDayWorkedTime() + emptySchedule.getDuration() > rules.getMaxHourPerDay() * 2)
                return false;
            if (schedule.getWeekWorkedTime() + emptySchedule.getDuration() > rules.getMaxHourPerWeek() * 2)
                return false;
            return schedule.getLastWorkedHourEnd()
                    .plusHours(rules.getBreakTime().getHour())
                    .plusMinutes(rules.getBreakTime().getMinute())
                    .isAfter(emptySchedule.getStart());
        };
        if (type == WorkTimeType.PREPARE)
            return this.getStaffWithPositions(rules.getPrepareStation(), allStaffs, validate);
        else if (type == WorkTimeType.WORK)
            return this.getStaffWithPositions(rules.getWorkStation(), allStaffs, validate);
        else if (type == WorkTimeType.END)
            return this.getStaffWithPositions(rules.getEndStation(), allStaffs, validate);
        return null;
    }
    private WorkTimeType getWorkTimeType(LocalDateTime time, AllRules rules) {
        int beginTime = time.getDayOfWeek().getValue() < 6 ? rules.getWeekDayOpenRule() : rules.getWeekendOpenRule();
        int endTime = time.getDayOfWeek().getValue() < 6 ? rules.getWeekDayCloseRule() : rules.getWeekendCloseRule();
        if (time.getHour() < beginTime)
            return WorkTimeType.PREPARE;
        if (time.getHour() > endTime)
            return WorkTimeType.END;
        return WorkTimeType.WORK;
    }
    // 创建空时间表
    private ScheduleResult createEmptySchedule(List<Integer> needStaffs, LocalDate date, AllRules rules) {
        ScheduleResult scheduleResult = new ScheduleResult();
        while (true) {
            Optional<Integer> begin = getFirstNeedStaff(needStaffs, i -> i > 0);
            if (begin.isEmpty())
                break;
            int end = getLastNeedStaff(needStaffs, i -> i > 0).get();
            for (int i = begin.get(); ; i += rules.getMaxContinuousWorkTime() * 2) {
                int endId = i + rules.getMaxContinuousWorkTime() * 2 - 1;
                if (endId <= end)
                    scheduleResult.insertEmpty(idToTime(i, date), idToTime(endId, date), endId - i + 1);
                if (endId + rules.getMaxContinuousWorkTime() * 2 > end && endId < end) {
                    int to = endId + rules.getLeastHourPerPeriod() * 2;
                    end = Math.max(to, end);
                    scheduleResult.insertEmpty(idToTime(endId + 1, date), idToTime(end, date), end - endId);
                    break;
                }
            }
        }
        return scheduleResult;
    }
    private Optional<Integer> getLastNeedStaff(List<Integer> needStaffs, Function<Integer, Boolean> validate) {
        for (int i = needStaffs.size() - 1; i >= 0; i--) {
            if (validate.apply(needStaffs.get(i)))
                return Optional.of(i);
        }
        return Optional.empty();
    }
    private Optional<Integer> getFirstNeedStaff(List<Integer> needStaffs, Function<Integer, Boolean> validate) {
        for (int i = 0; i < needStaffs.size(); i++) {
            if (validate.apply(needStaffs.get(i)))
                return Optional.of(i);
        }
        return Optional.empty();
    }
    private LocalDateTime idToTime(int id, LocalDate date) {
        return LocalDateTime.of(date, LocalTime.of(id / 2, id % 2 == 0 ? 0 : 30));
    }
    private void recycleStaff(StaffScheduleInternal staff, HashMap<String, Queue<StaffScheduleInternal>> allStaffs) {
        allStaffs.get(staff.getStaff().getPosition()).add(staff);
    }
    private StaffScheduleInternal getStaffWithPositions(List<String> allowedPositions, HashMap<String, Queue<StaffScheduleInternal>> allStaffs, Function<StaffScheduleInternal, Boolean> validate) {
        String randomPosition;
        int cnt = 0;
        do {
            if (cnt++ > MAX_RANDOM_TIMES)
                return null;
            randomPosition = allowedPositions.get(random.nextInt(allowedPositions.size()));
        } while(allStaffs.get(randomPosition).isEmpty() && !validate.apply(allStaffs.get(randomPosition).peek()));
        return allStaffs.get(randomPosition).poll();
    }
    // 将按时间区间分配的人数转换为按时间点分配的人数
    private List<Integer> calculateNeedStaffs(List<Data> data, boolean isWeekDay, AllRules rules) {
        Integer openTime = isWeekDay ? rules.getWeekDayOpenRule() : rules.getWeekendOpenRule();
        Integer closeTime = isWeekDay ? rules.getWeekDayCloseRule() : rules.getWeekendCloseRule();
        List<Integer> needStaffs = new ArrayList<>(Arrays.asList(new Integer[48]));
        for (int i = 0; i < rules.getPrepareTime(); ++i)
            needStaffs.set(this.timeToId(openTime - i - 1, 0), Math.max(rules.getPreparePeople(), rules.getLeastPeople()));
        for (int i = 0; i < rules.getEndTime(); ++i)
            needStaffs.set(this.timeToId(closeTime + i, 0), Math.max(rules.getEndPeople(), rules.getLeastPeople()));
        data.forEach(d -> {
            int beginTime = this.timeToId(d.getBeginTime());
            int endTime = this.timeToId(d.getEndTime());
            int num = (int)Math.ceil(d.getNum() / rules.getWorkPeople());
            num = Math.max(num, rules.getLeastPeople());
            for (int i = beginTime; i < endTime; i++) {
                needStaffs.set(i, num);
            }
        });
        return needStaffs;
    }
    private int timeToId(int hour, int minute) {
        return hour * 2 + (minute == 30 ? 1 : 0);
    }
    private int timeToId(String time) {
        String[] timeArray = time.split(":");
        return Integer.parseInt(timeArray[0]) * 2 + (Integer.parseInt(timeArray[1]) == 30 ? 1 : 0);
    }
    private HashMap<String, Queue<StaffScheduleInternal>> convertStaffsToMap(List<Staff> staffs, List<String> staffPositions) {
        HashMap<String, Queue<StaffScheduleInternal>> staffByPosition = new HashMap<>();
        staffPositions.forEach(position -> staffByPosition.put(position, new LinkedList<>()));
        Collections.shuffle(staffs);
        staffs.stream()
                .map(staff -> new StaffScheduleInternal(staff, null, null, 0, 0))
                .forEach(staff -> staffByPosition.get(staff.getStaff().getPosition()).add(staff));
        return staffByPosition;
    }
}

@lombok.Data
@AllArgsConstructor
class StaffScheduleInternal {
    private Staff staff;
    private LocalDateTime lastWorkedHourStart;
    private LocalDateTime lastWorkedHourEnd;
    // unit: Half an Hour
    private Integer dayWorkedTime;
    // unit: Half an Hour
    private Integer weekWorkedTime;
}
