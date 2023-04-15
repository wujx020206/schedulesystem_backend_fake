package cn.edu.fc.dao.bo;

import cn.edu.fc.dao.PreferenceDao;
import cn.edu.fc.dao.StoreDao;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.bo.SSObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.edu.fc.javaee.core.model.InternalReturnObject;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Staff extends SSObject implements Serializable {
    static final Byte PREFERENCE_WORK_DAY = 0;
    static final Byte PREFERENCE_WORK_TIME = 1;
    static final Byte PREFERENCE_WORK_LONG = 2;

    /**
     * 门店经理
     */
    public static final Byte STOREMANAGER = 0;

    /**
     * 副经理
     */
    public static final Byte ASSISTANTMANAGER = 1;

    /**
     * 小组长
     */
    public static final Byte TEAMLEADER = 2;

    /**
     * 店员（收银）
     */
    public static final Byte CASHIER = 3;

    /**
     * 店员（导购）
     */
    public static final Byte GUIDE = 4;

    /**
     * 店员（库房）
     */
    public static final Byte WAREHOUSE = 5;

    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(Staff.class);

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String position;

    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private Long storeId;

    private Store store;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private PreferenceDao preferenceDao;
    private List<Integer> workdayPreference;
    @ToString.Exclude
    @JsonIgnore
    @Setter
    private Pair<Integer, Integer> workTimePreference;

    private Integer dayWorkLongPreference;

    private Integer weekWorkLongPreference;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private StoreDao storeDao;

    public Store getStore() {
        if (null == this.storeId) {
            return null;
        }

        if (null == this.store && null != this.storeDao) {
            this.store = this.storeDao.findById(this.storeId);
        }

        return this.store;
    }

    @Builder
    public Staff(Long id, Long creatorId, Long modifierId, String modifierName, String creatorName, LocalDateTime gmtCreate, LocalDateTime gmtModified,
                 String name, String position, String phone, String email, Long storeId) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.name = name;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.storeId = storeId;
    }

    public List<Integer> getWorkdayPreference() {
        if (workdayPreference != null)
            return workdayPreference;
        Preference preference = getPreference(PREFERENCE_WORK_DAY);
        try {
            if (preference.getValue() == null || preference.getValue().strip().equals(""))
                return workdayPreference = new ArrayList<>();
            workdayPreference = Arrays.stream(preference.getValue().split(" ")).map(Integer::parseInt).collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new BusinessException(ReturnNo.FIELD_NOTVALID, String.format(ReturnNo.FIELD_NOTVALID.getMessage(), "工作日偏好"));
        }
        return workdayPreference;
    }

    public Pair<Integer, Integer> getWorkTimePreference() {
        if (workTimePreference != null)
            return workTimePreference;
        Preference preference = getPreference(PREFERENCE_WORK_TIME);
        try {
            if (preference.getValue() == null || preference.getValue().strip().equals(""))
                return workTimePreference = Pair.of(0, 24);
            String[] time = preference.getValue().split(" ");
            if (time.length != 2)
                throw new BusinessException(ReturnNo.FIELD_NOTVALID, String.format(ReturnNo.FIELD_NOTVALID.getMessage(), "工作时间偏好"));
            workTimePreference = Pair.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]));
        } catch (NumberFormatException | BusinessException e) {
            throw new BusinessException(ReturnNo.FIELD_NOTVALID, String.format(ReturnNo.FIELD_NOTVALID.getMessage(), "工作时间偏好"));
        }
        return workTimePreference;
    }

    public Integer getDayWorkLongPreference() {
        if (dayWorkLongPreference != null)
            return dayWorkLongPreference;
        this.getWorkLongPreference();
        return dayWorkLongPreference;
    }

    public Integer getWeekWorkLongPreference() {
        if (weekWorkLongPreference != null)
            return weekWorkLongPreference;
        this.getWorkLongPreference();
        return weekWorkLongPreference;
    }

    private void getWorkLongPreference() {
        Preference preference = getPreference(PREFERENCE_WORK_LONG);
        try {
            if (preference.getValue() == null || preference.getValue().strip().equals("")) {
                weekWorkLongPreference = 24 * 7;
                dayWorkLongPreference = 24;
                return;
            }
            String[] time = preference.getValue().split(" ");
            if (time.length != 2)
                throw new BusinessException(ReturnNo.FIELD_NOTVALID, String.format(ReturnNo.FIELD_NOTVALID.getMessage(), "班次时长偏好"));
            dayWorkLongPreference = Integer.parseInt(time[0]);
            weekWorkLongPreference = Integer.parseInt(time[1]);
        } catch (NumberFormatException | BusinessException e) {
            throw new BusinessException(ReturnNo.FIELD_NOTVALID, String.format(ReturnNo.FIELD_NOTVALID.getMessage(), "班次时长偏好"));
        }
    }

    private Preference getPreference(Byte type) {
        if (preferenceDao == null)
            throw new BusinessException(ReturnNo.PARAMETER_MISSED, ReturnNo.PARAMETER_MISSED.getMessage());
        Preference preference = preferenceDao.findByTypeAndStaffId(type, id);
        return preference;
    }
}
