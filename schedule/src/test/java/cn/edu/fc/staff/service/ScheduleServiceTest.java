package cn.edu.fc.staff.service;

import cn.edu.fc.ScheduleApplication;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.service.ScheduleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = ScheduleApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ScheduleServiceTest {
    @Autowired
    private ScheduleService scheduleService;

    @Test
    public void updateSchedule1() throws Exception {
        Assertions.assertDoesNotThrow(()->
                scheduleService.updateStaffSchedule(1L, 1L, "李四"));
    }

    @Test
    public void updateSchedule2() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.updateStaffSchedule(0L, 1L, "李四"));
    }

    @Test
    public void updateSchedule3() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.updateStaffSchedule(-1L, 1L, "李四"));
    }

    @Test
    public void updateSchedule4() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.updateStaffSchedule(4L, 1L, "李四"));
    }

    @Test
    public void updateSchedule5() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.updateStaffSchedule(null, 1L, "李四"));
    }

    @Test
    public void updateSchedule6() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.updateStaffSchedule(1L, 0L, "李四"));
    }

    @Test
    public void updateSchedule7() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.updateStaffSchedule(1L, -1L, "李四"));
    }

    @Test
    public void updateSchedule8() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.updateStaffSchedule(1L, 162L, "李四"));
    }

    @Test
    public void updateSchedule9() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.updateStaffSchedule(1L, null, "李四"));
    }

    @Test
    public void updateSchedule10() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.updateStaffSchedule(1L, 1L, "吴佳熙"));
    }

    @Test
    public void updateSchedule11() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.updateStaffSchedule(1L, 1L, "123"));
    }

    @Test
    public void updateSchedule12() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.updateStaffSchedule(1L, 1L, null));
    }

    @Test
    public void deleteSchedule1() throws Exception {
        Assertions.assertDoesNotThrow(()->
                scheduleService.deleteById(1L, 1L));
    }

    @Test
    public void deleteSchedule2() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.deleteById(0L, 1L));
    }

    @Test
    public void deleteSchedule3() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.deleteById(-1L, 1L));
    }

    @Test
    public void deleteSchedule4() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.deleteById(4L, 1L));
    }

    @Test
    public void deleteSchedule5() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.deleteById(null, 1L));
    }

    @Test
    public void deleteSchedule6() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.deleteById(1L, 0L));
    }

    @Test
    public void deleteSchedule7() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.deleteById(1L, -1L));
    }

    @Test
    public void deleteSchedule8() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.deleteById(1L, 162L));
    }

    @Test
    public void deleteSchedule9() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                scheduleService.deleteById(1L, null));
    }
}
