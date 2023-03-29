package cn.edu.fc.mapper;

import cn.edu.fc.mapper.po.DataPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DataPoMapper extends JpaRepository<DataPo, Long> {
    Page<DataPo> findByStoreId(Long storeId, Pageable pageable);

    Page<DataPo> findByStoreIdAndDateBetween(Long storeId, String beginDate, String endDate, Pageable pageable);

    Page<DataPo> findByStoreIdAndDate(Long storeId, String date, Pageable pageable);

    DataPo findByStoreIdAndDateAndBeginTimeAndEndTime(Long storeId, String date, String beginTime, String endTime);
}
