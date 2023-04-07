package cn.edu.fc.dao;

import cn.edu.fc.dao.bo.Preference;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.javaee.core.util.RedisUtil;
import cn.edu.fc.mapper.PreferencePoMapper;
import cn.edu.fc.mapper.po.PreferencePo;
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
public class PreferenceDao {
    private final static Logger logger = LoggerFactory.getLogger(PreferenceDao.class);

    private final static String KEY = "E%s";

    @Value("3600")
    private int timeout;

    private PreferencePoMapper preferencePoMapper;

    private RedisUtil redisUtil;

    private StaffDao staffDao;

    @Autowired
    public PreferenceDao(PreferencePoMapper preferencePoMapper, RedisUtil redisUtil, StaffDao staffDao) {
        this.preferencePoMapper = preferencePoMapper;
        this.redisUtil = redisUtil;
        this.staffDao = staffDao;
    }

    private Preference getBo(PreferencePo po, Optional<String> redisKey) {
        Preference bo = Preference.builder().type(po.getType()).staffId(po.getStaffId()).value(po.getValue()).build();
        this.setBo(bo);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    private void setBo(Preference bo) {
        bo.setStaffDao(bo.getStaffDao());
    }

    private PreferencePo getPo(Preference bo) {
        PreferencePo po = PreferencePo.builder().type(bo.getType()).staffId(bo.getStaffId()).value(bo.getValue()).build();
        return po;
    }

    public List<Preference> retrieveAll(Integer page, Integer pageSize) throws RuntimeException {
        List<PreferencePo> retList = this.preferencePoMapper.findAll(PageRequest.of(0, MAX_RETURN))
                .stream().collect(Collectors.toList());
        if (null == retList || retList.size() == 0)
            return new ArrayList<>();

        List<Preference> ret = retList.stream().map(po->{
            return getBo(po,Optional.ofNullable(null));
        }).collect(Collectors.toList());
        return ret;
    }

    public List<Preference> retrieveByStaffId(Long staffId, Integer page, Integer pageSize) {
        List<PreferencePo> retList = this.preferencePoMapper.findByStaffId(staffId, PageRequest.of(0, MAX_RETURN))
                .stream().collect(Collectors.toList());
        if (null == retList || retList.size() == 0)
            return new ArrayList<>();

        List<Preference> ret = retList.stream().map(po->{
            return getBo(po,Optional.ofNullable(null));
        }).collect(Collectors.toList());
        return ret;
    }

    public List<Preference> retrieveByType(Byte type, Integer page, Integer pageSize) {
        List<PreferencePo> retList = this.preferencePoMapper.findByType(type, PageRequest.of(0, MAX_RETURN))
                .stream().collect(Collectors.toList());
        if (null == retList || retList.size() == 0)
            return new ArrayList<>();

        List<Preference> ret = retList.stream().map(po->{
            return getBo(po,Optional.ofNullable(null));
        }).collect(Collectors.toList());
        return ret;
    }

    public Preference findByTypeAndStaffId(Byte type, Long staffId) {
        PreferencePo preferencePo = this.preferencePoMapper.findByTypeAndStaffId(type, staffId);
        if (null == preferencePo) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "员工偏好", staffId));
        }

        return getBo(preferencePo, Optional.empty());
    }

    public void insert(Preference preference) throws RuntimeException {
        PreferencePo po = this.preferencePoMapper.findByTypeAndStaffId(preference.getType(), preference.getStaffId());

        PreferencePo preferencePo = getPo(preference);
        this.preferencePoMapper.save(preferencePo);
    }

    public void save(Preference preference) {
        PreferencePo po = getPo(preference);
        this.preferencePoMapper.save(po);
    }

    public void delete(Byte type,Long id) {
        PreferencePo po = this.preferencePoMapper.findByTypeAndStaffId(type, id);
        this.preferencePoMapper.delete(po);
    }
}
