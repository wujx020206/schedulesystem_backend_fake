package cn.edu.fc.controller;

import cn.edu.fc.javaee.core.aop.LoginUser;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.ReturnObject;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/schedule/{storeId}", produces = "application/json;charset=UTF-8")
public class AdminScheduleController {
    private final Logger logger = LoggerFactory.getLogger(AdminScheduleController.class);

    private ScheduleService scheduleService;

    @Autowired
    public AdminScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/day/{date}/day")
    public ReturnObject getScheduleByDay(@PathVariable Long storeId,
                                         @PathVariable LocalDate date,
                                         @LoginUser UserDto user) {
        return new ReturnObject(ReturnNo.OK, scheduleService.retrieveScheduleByDay(storeId, date, user));
    }

    @GetMapping("/day/{date}/skill/{skill}/day")
    public ReturnObject getScheduleByDayAndSkill(@PathVariable Long storeId,
                                                 @PathVariable LocalDate date,
                                                 @PathVariable String skill,
                                                 @LoginUser UserDto user) {
        return new ReturnObject(ReturnNo.OK, scheduleService.retrieveScheduleByDayAndSkill(storeId, date, skill, user));
    }

    @GetMapping("/day/{date}/position/{position}/day")
    public ReturnObject getScheduleByDayAndPosition(@PathVariable Long storeId,
                                                    @PathVariable LocalDate date,
                                                    @PathVariable String position,
                                                    @LoginUser UserDto user) {
        return new ReturnObject(ReturnNo.OK, scheduleService.retrieveScheduleByDayAndPosition(storeId, date, position, user));
    }

    @GetMapping("/day/{date}/skill/{staffId}/day")
    public ReturnObject getScheduleByDayAndStaff(@PathVariable Long storeId,
                                                 @PathVariable LocalDate date,
                                                 @PathVariable Long staffId,
                                                 @LoginUser UserDto user) {
        return new ReturnObject(ReturnNo.OK, scheduleService.retrieveScheduleByDayAndStaff(storeId, date, staffId, user));
    }

    @GetMapping("/week/{date}/week")
    public ReturnObject getScheduleByWeek(@PathVariable Long storeId,
                                          @PathVariable LocalDate date,
                                          @LoginUser UserDto user) {
        return new ReturnObject(ReturnNo.OK, scheduleService.retrieveScheduleByWeek(storeId, date, user));
    }

    @GetMapping("/week/{date}/skill/{skill}/week")
    public ReturnObject getScheduleByWeekAndSkill(@PathVariable Long storeId,
                                                  @PathVariable LocalDate date,
                                                  @PathVariable String skill,
                                                  @LoginUser UserDto user) {
        return new ReturnObject(ReturnNo.OK, scheduleService.retrieveScheduleByWeekAndSkill(storeId, date, skill, user));
    }

    @GetMapping("/week/{date}/position/{position}/week")
    public ReturnObject getScheduleByWeekAndPosition(@PathVariable Long storeId,
                                                     @PathVariable LocalDate date,
                                                     @PathVariable String position,
                                                     @LoginUser UserDto user) {
        return new ReturnObject(ReturnNo.OK, scheduleService.retrieveScheduleByWeekAndPosition(storeId, date, position, user));
    }

    @GetMapping("/week/{date}/staff/{staffId}/week")
    public ReturnObject getScheduleByWeekAndStaff(@PathVariable Long storeId,
                                                  @PathVariable LocalDate date,
                                                  @PathVariable Long staffId,
                                                  @LoginUser UserDto user) {
        return new ReturnObject(ReturnNo.OK, scheduleService.retrieveScheduleByWeekAndStaff(storeId, date, staffId, user));
    }
}