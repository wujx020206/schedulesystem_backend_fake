package cn.edu.fc.controller;

import cn.edu.fc.javaee.core.model.ReturnObject;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/schedule", produces = "application/json;charset=UTF-8")
public class AdminScheduleController {
    private final Logger logger = LoggerFactory.getLogger(AdminScheduleController.class);

    @GetMapping("/day/{date}/day")
    public ReturnObject getScheduleByDay(@RequestParam LocalDate date) {
        throw new NotImplementedException();
    }

    @GetMapping("/day/{date}/skill/{skill}/day")
    public ReturnObject getScheduleByDayAndSkill(@RequestParam LocalDate date,
                                                 @RequestParam String skill) {
        throw new NotImplementedException();
    }

    @GetMapping("/day/{date}/position/{position}/day")
    public ReturnObject getScheduleByDayAndPosition(@RequestParam LocalDate date,
                                                    @RequestParam String position) {
        throw new NotImplementedException();
    }

    @GetMapping("/day/{date}/skill/{staffId}/day")
    public ReturnObject getScheduleByDayAndStaff(@RequestParam LocalDate date,
                                                 @RequestParam Long staffId) {
        throw new NotImplementedException();
    }

    @GetMapping("/week/{date}/week")
    public ReturnObject getScheduleByWeek(@RequestParam LocalDate date) {
        throw new NotImplementedException();
    }
}

