package cn.edu.fc.mapper;

import cn.edu.fc.mapper.po.StaffPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffPoMapper extends JpaRepository<StaffPo, Long> {
    Page<StaffPo> findByStoreId(Long storeId, Pageable pageable);

    StaffPo findByNameAndPhone(String name, String phone);

    Page<StaffPo> findByName(String name, Pageable pageable);
}
