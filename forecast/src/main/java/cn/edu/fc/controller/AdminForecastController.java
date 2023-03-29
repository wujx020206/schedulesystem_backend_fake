package cn.edu.fc.controller;

import cn.edu.fc.controller.vo.DateVo;
import cn.edu.fc.controller.vo.TimeVo;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.ReturnObject;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.service.DataService;
import cn.edu.fc.service.dto.DataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/forecast", produces = "application/json;charset=UTF-8")
public class AdminForecastController {
    private final Logger logger = LoggerFactory.getLogger(AdminForecastController.class);

    private final DataService dataService;

    @Autowired
    public AdminForecastController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/{storeId}/data")
    public ReturnObject getDataByStoreId(@PathVariable Long storeId,
                                         @RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<DataDto> ret = this.dataService.retrieveDataByStoreId(storeId, page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/{storeId}/period")
    public ReturnObject getDataByPeriod(@PathVariable Long storeId,
                                        @Validated @RequestBody DateVo dateVo,
                                        @RequestParam(required = false, defaultValue = "1") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<DataDto> ret = this.dataService.retrieveDataByStoreIdAndDateBetween(storeId, dateVo.getBeginDate(), dateVo.getEndDate(), page,pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/{storeId}/{date}/day")
    public ReturnObject getDataByDay(@PathVariable Long storeId,
                                     @PathVariable LocalDate date,
                                     @RequestParam(required = false, defaultValue = "1") Integer page,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<DataDto> ret = this.dataService.retrieveDataByStoreIdAndDate(storeId, date, page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/{storeId}/{date}/{time}/day")
    public ReturnObject getDataByDayAndTime(@PathVariable Long storeId,
                                            @PathVariable LocalDate date,
                                            @Validated @RequestBody TimeVo timeVo) {
        DataDto ret = this.dataService.findDataStoreIdAndDateAndBeginTimeAndEndTime(storeId, date, timeVo.getBeginTime(), timeVo.getEndTime());
        return new ReturnObject(ReturnNo.OK, ret);
    }
}
