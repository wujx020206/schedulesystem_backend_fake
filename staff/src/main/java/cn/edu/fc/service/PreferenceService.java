package cn.edu.fc.service;

import cn.edu.fc.dao.PreferenceDao;
import cn.edu.fc.dao.StaffDao;
import cn.edu.fc.dao.bo.Preference;
import cn.edu.fc.dao.bo.Staff;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.service.dto.PreferenceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PreferenceService {
    private static final Logger logger = LoggerFactory.getLogger(PreferenceService.class);

    private final PreferenceDao preferenceDao;

    private final StaffDao staffDao;

    @Autowired
    public PreferenceService(PreferenceDao preferenceDao, StaffDao staffDao) {
        this.preferenceDao = preferenceDao;
        this.staffDao = staffDao;
    }

    public PageDto<PreferenceDto> retrievePreferences(Integer page, Integer pageSize) {
        List<Preference> preferences = this.preferenceDao.retrieveAll(page, pageSize);
        List<PreferenceDto> ret = preferences.stream().map(obj -> {
            PreferenceDto dto = PreferenceDto.builder().type(obj.getType()).staffName(obj.getStaff().getName()).value(obj.getValue()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public PageDto<PreferenceDto> retrievePreferencesByStaffId(Long staffId, Integer page, Integer pageSize) {
        List<Preference> preferences = this.preferenceDao.retrieveByStaffId(staffId, page, pageSize);
        List<PreferenceDto> ret = preferences.stream().map(obj -> {
            PreferenceDto dto = PreferenceDto.builder().type(obj.getType()).staffName(obj.getStaff().getName()).value(obj.getValue()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public PageDto<PreferenceDto> retrievePreferencesByType(Byte type, Integer page, Integer pageSize) {
        List<Preference> preferences = this.preferenceDao.retrieveByType(type, page, pageSize);
        List<PreferenceDto> ret = preferences.stream().map(obj -> {
            PreferenceDto dto = PreferenceDto.builder().type(obj.getType()).staffName(obj.getStaff().getName()).value(obj.getValue()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public PreferenceDto retrievePreferencesByTypeAndStaffId(Byte type, Long staffId) {
        Preference preference = this.preferenceDao.findByTypeAndStaffId(type, staffId);
        PreferenceDto dto = PreferenceDto.builder().type(preference.getType()).staffName(preference.getStaff().getName()).value(preference.getValue()).build();
        return dto;
    }

    public void createPreference(Long staffId, Byte type, String value, UserDto user) {
        Staff staff = this.staffDao.findById(staffId);
        if (null == staff) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "员工", staffId));
        }

        Preference preference = this.preferenceDao.findByTypeAndStaffId(type, staffId);
        if (null != preference) {
            throw new BusinessException(ReturnNo.STAFF_EXIST, String.format(ReturnNo.STAFF_EXIST.getMessage(), staffId));
        }

        Preference obj = Preference.builder().type(type).staffId(staffId).value(value).build();
        this.preferenceDao.insert(obj);
    }

    public void updatePreference(Long staffId, Byte type, String value) {
        Preference preference = this.preferenceDao.findByTypeAndStaffId(type, staffId);
        if (null == preference) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "员工偏好", staffId));
        }

        preference.setValue(value);
        this.preferenceDao.save(preference);
    }

    public void deletePreference(Byte type, Long staffId) {
        Preference preference = this.preferenceDao.findByTypeAndStaffId(type, staffId);
        if (null == preference) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "员工", staffId));
        }

        this.preferenceDao.delete(type, staffId);
    }
}
