package cn.edu.fc.mapper;

import cn.edu.fc.mapper.po.DataPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface DataPoMapper extends JpaRepository<DataPo, Long> {
    Page<DataPo> findByStoreId(Long storeId, Pageable pageable);

    Page<DataPo> findByStoreIdAndDateBetween(Long storeId, LocalDate beginDate, LocalDate endDate, Pageable pageable);

    Page<DataPo> findByStoreIdAndDate(Long storeId, LocalDate date, Pageable pageable);

    DataPo findByStoreIdAndDateAndBeginTimeAndEndTime(Long storeId, LocalDate date, LocalTime beginTime, LocalTime endTime);
}
