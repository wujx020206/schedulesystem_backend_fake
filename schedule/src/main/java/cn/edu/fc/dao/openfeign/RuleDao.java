package cn.edu.fc.dao.openfeign;

import cn.edu.fc.dao.bo.AllRules;
import cn.edu.fc.javaee.core.model.InternalReturnObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("rule-service")
public interface RuleDao {
    @GetMapping("/rule/{storeId}/rules")
    InternalReturnObject<AllRules> getAllRulesByStoreId(@PathVariable Long storeId);
}
