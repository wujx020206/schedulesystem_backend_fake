package cn.edu.fc.mapper;

import cn.edu.fc.mapper.po.PreferencePo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferencePoMapper extends JpaRepository<PreferencePo, Long> {
    Page<PreferencePo> findByStaffId(Long staffId, Pageable pageable);

    Page<PreferencePo> findByType(Byte type, Pageable pageable);

    PreferencePo findByTypeAndStaffId(Byte type, Long staffId);

    void deleteByTypeAndStaffId(Byte type, Long staffId);
}
