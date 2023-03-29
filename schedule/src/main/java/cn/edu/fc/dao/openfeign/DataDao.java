package cn.edu.fc.dao.openfeign;

import cn.edu.fc.dao.bo.Data;
import cn.edu.fc.javaee.core.model.InternalReturnObject;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@FeignClient("forecast-service")
public interface DataDao {
    @GetMapping("/forecast/{storeId}/{date}/day")
    InternalReturnObject<PageDto<Data>> retrieveWeekData(@PathVariable Long storeId, @PathVariable LocalDate date);
}
