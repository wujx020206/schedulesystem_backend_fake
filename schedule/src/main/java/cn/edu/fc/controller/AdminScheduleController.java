package cn.edu.fc.controller;

import cn.edu.fc.javaee.core.aop.LoginUser;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.ReturnObject;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
                                         @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new ReturnObject(ReturnNo.OK, scheduleService.retrieveScheduleByDay(storeId, localDate));
    }

    @GetMapping("/day/{date}/skill/{skill}/day")
    public ReturnObject getScheduleByDayAndSkill(@PathVariable Long storeId,
                                                 @PathVariable String date,
                                                 @PathVariable String skill) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new ReturnObject(ReturnNo.OK, scheduleService.retrieveScheduleByDayAndSkill(storeId, localDate, skill));
    }

    @GetMapping("/day/{date}/position/{position}/day")
    public ReturnObject getScheduleByDayAndPosition(@PathVariable Long storeId,
                                                    @PathVariable String date,
                                                    @PathVariable String position) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new ReturnObject(ReturnNo.OK, scheduleService.retrieveScheduleByDayAndPosition(storeId, localDate, position));
    }

    @GetMapping("/day/{date}/staff/{staffId}/day")
    public ReturnObject getScheduleByDayAndStaff(@PathVariable Long storeId,
                                                 @PathVariable String date,
                                                 @PathVariable Long staffId) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new ReturnObject(ReturnNo.OK, scheduleService.retrieveScheduleByDayAndStaff(storeId, localDate, staffId));
    }

    @GetMapping("/week/{date}/week")
    public ReturnObject getScheduleByWeek(@PathVariable Long storeId,
                                          @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new ReturnObject(ReturnNo.OK, scheduleService.retrieveScheduleByWeek(storeId, localDate));
    }

    @GetMapping("/week/{date}/skill/{skill}/week")
    public ReturnObject getScheduleByWeekAndSkill(@PathVariable Long storeId,
                                                  @PathVariable String date,
                                                  @PathVariable String skill) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new ReturnObject(ReturnNo.OK, scheduleService.retrieveScheduleByWeekAndSkill(storeId, localDate, skill));
    }

    @GetMapping("/week/{date}/position/{position}/week")
    public ReturnObject getScheduleByWeekAndPosition(@PathVariable Long storeId,
                                                     @PathVariable String date,
                                                     @PathVariable String position) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new ReturnObject(ReturnNo.OK, scheduleService.retrieveScheduleByWeekAndPosition(storeId, localDate, position));
    }

    @GetMapping("/week/{date}/staff/{staffId}/week")
    public ReturnObject getScheduleByWeekAndStaff(@PathVariable Long storeId,
                                                  @PathVariable String date,
                                                  @PathVariable Long staffId) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new ReturnObject(ReturnNo.OK, scheduleService.retrieveScheduleByWeekAndStaff(storeId, localDate, staffId));
    }

    @PutMapping("/{id}/{name}/period")
    public ReturnObject updateStaffScheduleById(@PathVariable Long storeId,
                                                @PathVariable Long id,
                                                @PathVariable String name) {
        this.scheduleService.updateStaffSchedule(storeId, id, name);
        return new ReturnObject(ReturnNo.OK);
    }

    @GetMapping("/id/{staffId}/{start}/{end}/period")
    public ReturnObject getIdByStaffIdAndStartAndEnd(@PathVariable Long storeId,
                                                     @PathVariable Long staffId,
                                                     @PathVariable String start,
                                                     @PathVariable String end) {
        LocalDateTime startTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return new ReturnObject(ReturnNo.OK, scheduleService.findIdByStaffIdAndStartAndEnd(staffId, startTime, endTime));
    }

    @DeleteMapping("/{id}/id")
    public ReturnObject deleteById(@PathVariable Long storeId,
                                   @PathVariable Long id) {
        this.scheduleService.deleteById(storeId, id);
        return new ReturnObject(ReturnNo.OK);
    }
}