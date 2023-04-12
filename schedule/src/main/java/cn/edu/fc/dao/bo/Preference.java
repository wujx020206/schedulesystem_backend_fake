package cn.edu.fc.dao.bo;

import cn.edu.fc.dao.StaffDao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Preference implements Serializable {
    /**
     * 工作日偏好
     */
    private static final Byte DAY = 0;

    /**
     * 工作时间偏好
     */
    private static final Byte TIME = 1;

    /**
     * 班次时长偏好
     */
    private static final Byte LONG = 2;

    @Getter
    @Setter
    private Byte type;

    @Getter
    @Setter
    private Long staffId;

    @Getter
    @Setter
    private Staff staff;

    @Getter
    @Setter
    private String value;

    @Getter
    @Setter
    private StaffDao staffDao;

    public Staff getStaff() {
        if (null == this.staffId) {
            return null;
        }

        if (null == this.staff && null != this.staffId) {
            this.staff = this.staffDao.findById(this.staffId);
        }

        return this.staff;
    }

    @Builder
    public Preference(Byte type, Long staffId, String value) {
        this.type = type;
        this.staffId = staffId;
        this.value = value;
    }
}
