package cn.edu.fc.service;

import cn.edu.fc.dao.PreferenceDao;
import cn.edu.fc.dao.StaffDao;
import cn.edu.fc.dao.StoreDao;
import cn.edu.fc.dao.bo.Preference;
import cn.edu.fc.dao.bo.Store;
import cn.edu.fc.dao.bo.Staff;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.service.dto.StaffDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffService {
    private static final Logger logger = LoggerFactory.getLogger(StaffService.class);

    private final StaffDao staffDao;

    private final PreferenceDao preferenceDao;

    private final StoreDao storeDao;

    @Autowired
    public StaffService(StaffDao staffDao, PreferenceDao preferenceDao, StoreDao storeDao) {
        this.staffDao = staffDao;
        this.preferenceDao = preferenceDao;
        this.storeDao = storeDao;
    }

    public PageDto<StaffDto> retrieveStaffs(Integer page, Integer pageSize) {
        List<Staff> staffs = this.staffDao.retrieveAll(page, pageSize);
        List<StaffDto> ret = staffs.stream().map(obj -> {
            StaffDto dto = StaffDto.builder().id(obj.getId()).name(obj.getName()).position(obj.getPosition()).phone(obj.getPhone()).email(obj.getEmail()).shopName(obj.getStore().getName()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public PageDto<StaffDto> retrieveStaffsByStoreId(Long storeId, Integer page, Integer pageSize) {
        List<Staff> staffs = this.staffDao.retrieveByShopId(storeId, page, pageSize);
        List<StaffDto> ret = staffs.stream().map(obj -> {
            StaffDto dto = StaffDto.builder().id(obj.getId()).name(obj.getName()).position(obj.getPosition()).phone(obj.getPhone()).email(obj.getEmail()).shopName(obj.getStore().getName()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public StaffDto findStaffById(Long staffId) {
        Staff obj = this.staffDao.findById(staffId);
        StaffDto dto = StaffDto.builder().id(obj.getId()).name(obj.getName()).position(obj.getPosition()).phone(obj.getPhone()).email(obj.getEmail()).shopName(obj.getStore().getName()).build();
        return dto;
    }

    public PageDto<StaffDto> retrieveStaffByName(String name, Integer page, Integer pageSize) {
        List<Staff> staffs = this.staffDao.retrieveByName(name, page, pageSize);
        List<StaffDto> ret = staffs.stream().map(obj -> {
            StaffDto dto = StaffDto.builder().id(obj.getId()).name(obj.getName()).position(obj.getPosition()).phone(obj.getPhone()).email(obj.getEmail()).shopName(obj.getStore().getName()).build();
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public PageDto<Long> retrieveStaffIdByName(String name, Integer page, Integer pageSize) {
        List<Staff> staffs = this.staffDao.retrieveByName(name, page, pageSize);
        List<Long> ret = staffs.stream().map(obj -> {
            Long id = obj.getId();
            return id;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    public List<String> retrievePositions() {
        List<String> ret = new ArrayList();
        ret.add("STOREMANAGER");
        ret.add("ASSISTANTMANAGER");
        ret.add("TEAMLEADER");
        ret.add("CASHIER");
        ret.add("GUIDE");
        ret.add("WAREHOUSE");
        return ret;
    }

    public List<String> retrieveSkills() {
        List<String> ret = new ArrayList();
        ret.add("CASHIER");
        ret.add("GUIDE");
        ret.add("WAREHOUSE");
        return ret;
    }

    public void createStaff(String name, String position, String phone, String email, Long shopId) {
        Store shop = this.storeDao.findById(shopId);
        if (null == shop) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "商铺", shopId));
        }

        Staff staff = Staff.builder().name(name).position(position).phone(phone).email(email).storeId(shopId).build();
        Long staffId = this.staffDao.insert(staff);

        Preference pre1 = Preference.builder().type((byte) 0).staffId(staffId).value(null).build();
        this.preferenceDao.insert(pre1);

        Preference pre2 = Preference.builder().type((byte) 1).staffId(staffId).value(null).build();
        this.preferenceDao.insert(pre2);

        Preference pre3 = Preference.builder().type((byte) 2).staffId(staffId).value(null).build();
        this.preferenceDao.insert(pre3);
    }

    public void updateStaff(Long staffId, String name, String position, String phone, String email, Long storeId) {
        Staff staff = this.staffDao.findById(staffId);
        if (null == staff) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "员工", staffId));
        }

        staff.setName(name);
        staff.setPosition(position);
        staff.setPhone(phone);
        staff.setEmail(email);
        staff.setStoreId(storeId);
        this.staffDao.save(staffId, staff);
    }

    public void deleteStaff(Long staffId) {
        Staff staff = this.staffDao.findById(staffId);
        if (null == staff) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "员工", staffId));
        }

        this.preferenceDao.delete((byte) 0, staffId);
        this.preferenceDao.delete((byte) 1, staffId);
        this.preferenceDao.delete((byte) 2, staffId);

        this.staffDao.delete(staffId);
    }
}
