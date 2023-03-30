package cn.edu.fc.mapper;

import cn.edu.fc.mapper.po.StaffSchedulePo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface StaffSchedulePoMapper extends JpaRepository<StaffSchedulePo, Long> {
    Page<StaffSchedulePo> findAllByStartGreaterThanEqualAndEndLessThanEqual(LocalDateTime start, LocalDateTime end, Pageable pageable);
}