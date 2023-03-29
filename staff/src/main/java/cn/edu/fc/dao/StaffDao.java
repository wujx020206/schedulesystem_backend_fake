package cn.edu.fc.dao;

import cn.edu.fc.dao.bo.Staff;
import cn.edu.fc.dao.openfeign.StoreDao;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.Constants;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.javaee.core.util.RedisUtil;
import cn.edu.fc.mapper.StaffPoMapper;
import cn.edu.fc.mapper.po.StaffPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.fc.javaee.core.model.Constants.MAX_RETURN;
import static cn.edu.fc.javaee.core.util.Common.putGmtFields;
import static cn.edu.fc.javaee.core.util.Common.putUserFields;

@Repository
@RefreshScope
public class StaffDao {
    private final static Logger logger = LoggerFactory.getLogger(StaffDao.class);

    private final static String KEY = "E%s";

    @Value("3600")
    private int timeout;

    private StaffPoMapper staffPoMapper;

    private RedisUtil redisUtil;

    private StoreDao storeDao;

    @Autowired
    public StaffDao(StaffPoMapper staffPoMapper, RedisUtil redisUtil, StoreDao storeDao) {
        this.staffPoMapper = staffPoMapper;
        this.redisUtil = redisUtil;
        this.storeDao = storeDao;
    }

    private Staff getBo(StaffPo po, Optional<String> redisKey) {
        Staff bo = Staff.builder().name(po.getName()).position(po.getPosition()).phone(po.getPhone())
                .email(po.getEmail()).storeId(po.getStoreId()).build();
        this.setBo(bo);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    private void setBo(Staff bo) {
        bo.setStoreDao(storeDao);
    }

    private StaffPo getPo(Staff bo) {
        StaffPo po = StaffPo.builder().id(bo.getId()).name(bo.getName()).position(bo.getPosition()).phone(bo.getPhone()).email(bo.getEmail()).storeId(bo.getStoreId()).build();
        return po;
    }

    public Staff findById(Long id) throws RuntimeException {
        if (null == id) {
            return null;
        }

        String key = String.format(KEY, id);

        if (redisUtil.hasKey(key)) {
            Staff bo = (Staff) redisUtil.get(key);
            this.setBo(bo);
            return bo;
        }

        Optional<StaffPo> po = this.staffPoMapper.findById(id);
        if (po.isPresent()) {
            return this.getBo(po.get(), Optional.of(key));
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "员工偏好", id));
        }
    }

    public List<Staff> retrieveAll(Integer page, Integer pageSize) throws RuntimeException {
        List<StaffPo> retList = this.staffPoMapper.findAll(PageRequest.of(0, MAX_RETURN))
                .stream().collect(Collectors.toList());
        if (null == retList || retList.size() == 0)
            return new ArrayList<>();

        List<Staff> ret = retList.stream().map(po->{
            return getBo(po,Optional.ofNullable(null));
        }).collect(Collectors.toList());
        return ret;
    }

    public List<Staff> retrieveByShopId(Long storeId, Integer page, Integer pageSize) {
        List<StaffPo> retList = this.staffPoMapper.findByStoreId(storeId, PageRequest.of(0, MAX_RETURN))
                .stream().collect(Collectors.toList());
        if (null == retList || retList.size() == 0)
            return new ArrayList<>();

        List<Staff> ret = retList.stream().map(po->{
            return getBo(po,Optional.ofNullable(null));
        }).collect(Collectors.toList());
        return ret;
    }

    public List<Staff> retrieveByName(String name, Integer page, Integer pageSize) {
        List<StaffPo> retList = this.staffPoMapper.findByName(name, PageRequest.of(0, MAX_RETURN))
                .stream().collect(Collectors.toList());
        if (null == retList || retList.size() == 0)
            return new ArrayList<>();

        List<Staff> ret = retList.stream().map(po->{
            return getBo(po,Optional.ofNullable(null));
        }).collect(Collectors.toList());
        return ret;
    }

    public Long insert(Staff staff, UserDto user) throws RuntimeException {
        StaffPo po = this.staffPoMapper.findByNameAndPhone(staff.getName(), staff.getPhone());
        if (null == po) {
            StaffPo staffPo = getPo(staff);
            putUserFields(staffPo, "creator", user);
            putGmtFields(staffPo, "create");
            this.staffPoMapper.save(staffPo);
            return staffPo.getId();
        } else {
            throw new BusinessException(ReturnNo.STAFF_EXIST, String.format(ReturnNo.STAFF_EXIST.getMessage(), staff.getName()));
        }
    }

    public String save(Long staffId, Staff staff, UserDto user) {
        StaffPo po = getPo(staff);
        po.setId(staffId);
        if (null != user) {
            putUserFields(po, "modifier", user);
            putGmtFields(po, "modified");
        }
        this.staffPoMapper.save(po);
        return String.format(KEY, staff.getId());
    }

    public void delete(Long id) {
        this.staffPoMapper.deleteById(id);
    }
}
