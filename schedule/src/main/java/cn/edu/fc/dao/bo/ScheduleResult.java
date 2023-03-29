package cn.edu.fc.dao.bo;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ScheduleResult {
    @Getter
    private List<StaffSchedule> scheduleList;

    public ScheduleResult() {
        scheduleList = new ArrayList<>();
    }
    public ScheduleResult insertEmpty(LocalDateTime start, LocalDateTime end, int duration) {
        scheduleList.add(new StaffSchedule(null, start, end, duration));
        return this;
    }
    public Stream<StaffSchedule> stream() {
        return scheduleList.stream();
    }
    public ScheduleResult merge(ScheduleResult other) {
        this.scheduleList.addAll(other.scheduleList);
        return this;
    }
}
