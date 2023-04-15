package cn.edu.fc.dao.bo;

import cn.edu.fc.dao.PreferenceDao;
import cn.edu.fc.dao.StaffDao;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.InternalReturnObject;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.bo.SSObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@ToString(callSuper = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StaffSchedule extends SSObject {
    @Getter
    @Setter
    private Long staffId;
    @ToString.Exclude
    @JsonIgnore
    @Setter
    private StaffDao staffDao;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private PreferenceDao preferenceDao;

    @Setter
    private Staff staff;
    @Getter
    @Setter
    private LocalDateTime start;
    @Getter
    @Setter
    private LocalDateTime end;
    // unit: Half an Hour
    @Getter
    @Setter
    private int duration;

    @Builder
    public StaffSchedule(Long staffId, LocalDateTime start, LocalDateTime end, int duration) {
        this.staffId = staffId;
        this.start = start;
        this.end = end;
        this.staff = null;
        this.duration = duration;
    }

//    @Builder
//    public StaffSchedule(Long staffId, LocalDateTime start, LocalDateTime end) {
//        this.staffId = staffId;
//        this.start = start;
//        this.end = end;
//        this.staff = null;
//    }

//    public StaffSchedule(LocalDateTime start, LocalDateTime end, int duration) {
//        this.start = start;
//        this.end = end;
//        this.duration = duration;
//        this.staff = null;
//    }

    public Staff getStaff() {
        if (staff != null)
            return staff;
        if (staffDao == null || staffId == null)
            throw new BusinessException(ReturnNo.PARAMETER_MISSED, ReturnNo.PARAMETER_MISSED.getMessage());
        Staff staff = this.staffDao.findById(staffId);
        staff.setPreferenceDao(preferenceDao);
        return staff;
    }
}