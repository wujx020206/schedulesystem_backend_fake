package cn.edu.fc.dao.openfeign;

import cn.edu.fc.dao.bo.Staff;
import cn.edu.fc.javaee.core.model.InternalReturnObject;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("staff-service")
public interface StaffDao {
    @GetMapping("/staff/{storeId}/staffs")
    InternalReturnObject<PageDto<Staff>> retrieveAllStaffsByShopId(@PathVariable Long storeId);
    @GetMapping("/staff/positions")
    InternalReturnObject<List<String>> retrieveAllStaffPositions();
}
