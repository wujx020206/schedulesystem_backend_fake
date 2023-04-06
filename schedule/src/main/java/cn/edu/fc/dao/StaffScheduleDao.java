package cn.edu.fc.dao;

import cn.edu.fc.dao.bo.StaffSchedule;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.javaee.core.util.RedisUtil;
import cn.edu.fc.mapper.StaffSchedulePoMapper;
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

    private static final String KEY = "SS%d";

    @Value("3600")
    private int timeout;
    private StaffSchedulePoMapper staffSchedulePoMapper;
    private RedisUtil redisUtil;
    private StaffDao staffDao;

    @Autowired
    public StaffScheduleDao(StaffSchedulePoMapper staffSchedulePoMapper, RedisUtil redisUtil, StaffDao staffDao) {
        this.staffSchedulePoMapper = staffSchedulePoMapper;
        this.redisUtil = redisUtil;
        this.staffDao = staffDao;
    }

    private StaffSchedule getBo(StaffSchedulePo po, Optional<String> redisKey) {
        StaffSchedule bo = StaffSchedule.builder().start(po.getStart()).end(po.getEnd()).build();
        this.setBo(bo);
        redisKey.ifPresent(key -> redisUtil.set(key, bo, timeout));
        return bo;
    }

    private void setBo(StaffSchedule bo) {
        bo.setStaffDao(staffDao);
    }

    private StaffSchedulePo getPo(StaffSchedule bo) {
        return StaffSchedulePo.builder().id(bo.getId()).staffId(bo.getStaffId()).start(bo.getStart()).end(bo.getEnd()).build();
    }

    public List<StaffSchedule> retrieveByByStartGreaterThanEqualAndEndLessThanEqual(LocalDateTime start, LocalDateTime end, Integer page, Integer size) {
        return this.staffSchedulePoMapper.findAllByStartGreaterThanEqualAndEndLessThanEqual(start, end, PageRequest.of(page, size)).stream()
                .map(po -> this.getBo(po, Optional.of(String.format(KEY, po.getId()))))
                .collect(Collectors.toList());
    }

    public Long insert(StaffSchedule staffSchedule, UserDto user) {
        StaffSchedulePo po = this.getPo(staffSchedule);
        putUserFields(po, "creator", user);
        putGmtFields(po, "create");
        this.staffSchedulePoMapper.save(po);
        return po.getId();
    }
}