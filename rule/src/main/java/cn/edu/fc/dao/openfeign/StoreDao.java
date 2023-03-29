package cn.edu.fc.dao.openfeign;

import cn.edu.fc.dao.bo.Store;
import cn.edu.fc.javaee.core.model.InternalReturnObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("store-service")
public interface StoreDao {
    @GetMapping("/{storeId}/store")
    InternalReturnObject<Store> getStoreById(@PathVariable Long id);
}

