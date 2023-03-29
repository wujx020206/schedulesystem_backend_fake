package cn.edu.fc.dao.bo;

import cn.edu.fc.dao.bo.Staff;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StaffSchedule {
    private Staff staff;
    private LocalDateTime start;
    private LocalDateTime end;
    // unit: Half an Hour
    private int duration;
}
