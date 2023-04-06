package cn.edu.fc.mapper;

import cn.edu.fc.mapper.po.RulePo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RulePoMapper extends JpaRepository<RulePo, Long> {
    Page<RulePo> findByStoreId(Long storeId, Pageable pageable);

    List<RulePo> findByStoreId(Long storeId);

    RulePo findByTypeAndStoreId(String type, Long storeId);
}
