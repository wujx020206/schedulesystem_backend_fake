package cn.edu.fc.dao;

import cn.edu.fc.dao.bo.Rule;
import cn.edu.fc.dao.bo.StaffSchedule;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.javaee.core.util.RedisUtil;
import cn.edu.fc.mapper.StaffSchedulePoMapper;
import cn.edu.fc.mapper.po.RulePo;
import cn.edu.fc.mapper.po.StaffSchedulePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.fc.javaee.core.util.Common.putGmtFields;
import static cn.edu.fc.javaee.core.util.Common.putUserFields;

@Repository
@RefreshScope
public class StaffScheduleDao {
    private static final Logger logger = LoggerFactory.getLogger(StaffScheduleDao.class);

    private static final String KEY = "S%d";

    @Value("3600")
    private int timeout;

    private final StaffSchedulePoMapper staffSchedulePoMapper;

    private final RedisUtil redisUtil;

    private final StaffDao staffDao;

    private final PreferenceDao preferenceDao;

    @Autowired
    public StaffScheduleDao(StaffSchedulePoMapper staffSchedulePoMapper, RedisUtil redisUtil, StaffDao staffDao, PreferenceDao preferenceDao) {
        this.staffSchedulePoMapper = staffSchedulePoMapper;
        this.redisUtil = redisUtil;
        this.staffDao = staffDao;
        this.preferenceDao = preferenceDao;
    }

    private StaffSchedule getBo(StaffSchedulePo po, Optional<String> redisKey) {
        StaffSchedule bo = StaffSchedule.builder().staffId(po.getStaffId()).start(po.getStart()).end(po.getEnd()).build();
        this.setBo(bo);
        //redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    private void setBo(StaffSchedule bo) {
        bo.setStaffDao(staffDao);
        bo.setPreferenceDao(preferenceDao);
    }

    private StaffSchedulePo getPo(StaffSchedule bo) {
        return StaffSchedulePo.builder().id(bo.getId()).staffId(bo.getStaffId()).start(bo.getStart()).end(bo.getEnd()).build();
    }

    public StaffSchedule findById(Long id) throws RuntimeException {
        if (null == id) {
            return null;
        }

        String key = String.format(KEY, id);

        if (redisUtil.hasKey(key)) {
            StaffSchedule bo = (StaffSchedule) redisUtil.get(key);
            return bo;
        }

        Optional<StaffSchedulePo> po = this.staffSchedulePoMapper.findById(id);
        if (po.isPresent()) {
            return this.getBo(po.get(), Optional.of(key));
        } else {
            return null;
        }
    }

    public List<StaffSchedule> retrieveByStartGreaterThanEqualAndEndLessThanEqual(LocalDateTime start, LocalDateTime end, Integer page, Integer size) {
        return this.staffSchedulePoMapper.findAllByStartGreaterThanEqualAndEndLessThanEqual(start, end, PageRequest.of(page, size)).stream()
                .map(po -> this.getBo(po,Optional.ofNullable(null)))
                .collect(Collectors.toList());
    }

    public Long findIdByStaffIdAndStartAndEnd(Long staffId, LocalDateTime start, LocalDateTime end) {
        return this.staffSchedulePoMapper.findByStaffIdAndStartAndEnd(staffId, start, end).getId();
    }

    public Long insert(StaffSchedule staffSchedule) {
        StaffSchedulePo po = this.getPo(staffSchedule);
        this.staffSchedulePoMapper.save(po);
        return po.getId();
    }

    public String save(Long id, StaffSchedule staffSchedule) {
        StaffSchedulePo po = this.getPo(staffSchedule);
        po.setId(id);
        this.staffSchedulePoMapper.save(po);
        return String.format(KEY, staffSchedule.getId());
    }

    public void delete(Long id) {
        this.staffSchedulePoMapper.deleteById(id);
    }
}