package cn.edu.fc.staff.service;

import cn.edu.fc.StaffApplication;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.service.StaffService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = StaffApplication.class)
@AutoConfigureMockMvc
@Transactional
public class StaffServiceTest {
    @Autowired
    private StaffService staffService;

    @Test
    public void createStaff1() throws Exception {
        staffService.createStaff("吴佳熙", "副经理", "12345678910","wjx@mail.com",1L);
        Assertions.assertEquals("副经理", staffService.retrieveStaffByName("吴佳熙", 1, 10).getList().get(0).getPosition());
        Assertions.assertEquals("12345678910", staffService.retrieveStaffByName("吴佳熙", 1, 10).getList().get(0).getPhone());
        Assertions.assertEquals("wjx@mail.com", staffService.retrieveStaffByName("吴佳熙", 1, 10).getList().get(0).getEmail());
        Assertions.assertEquals("门店1", staffService.retrieveStaffByName("吴佳熙", 1, 10).getList().get(0).getShopName());
    }

    @Test
    public void createStaff2() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.createStaff("123", "副经理", "12345678910","wjx@mail.com",1L));
    }

    @Test
    public void createStaff3() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.createStaff(null, "副经理", "12345678910","wjx@mail.com",1L));
    }

    @Test
    public void createStaff4() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.createStaff("吴佳熙", "不存在的职位", "12345678910","wjx@mail.com",1L));
    }

    @Test
    public void createStaff5() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.createStaff("吴佳熙", "123", "12345678910","wjx@mail.com",1L));
    }

    @Test
    public void createStaff6() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.createStaff("吴佳熙", null, "12345678910","wjx@mail.com",1L));
    }

    @Test
    public void createStaff7() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.createStaff("吴佳熙", "副经理", "123","wjx@mail.com",1L));
    }

    @Test
    public void createStaff8() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.createStaff("吴佳熙", "副经理", "1234567891011","wjx@mail.com",1L));
    }

    @Test
    public void createStaff9() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.createStaff("吴佳熙", "副经理", null,"wjx@mail.com",1L));
    }

    @Test
    public void createStaff10() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.createStaff("吴佳熙", "副经理", "吴佳熙的手机号","wjx@mail.com",1L));
    }

    @Test
    public void createStaff11() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.createStaff("吴佳熙", "副经理", "12345678910","wjx",1L));
    }

    @Test
    public void createStaff12() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.createStaff("吴佳熙", "副经理", "12345678910",null,1L));
    }

    @Test
    public void createStaff13() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.createStaff("吴佳熙", "副经理", "12345678910","wjx@mail.com",0L));
    }

    @Test
    public void createStaff14() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.createStaff("吴佳熙", "副经理", "12345678910","wjx@mail.com",-1L));
    }

    @Test
    public void createStaff15() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.createStaff("吴佳熙", "副经理", "12345678910","wjx@mail.com",4L));
    }

    @Test
    public void createStaff16() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.createStaff("吴佳熙", "副经理", "12345678910","wjx@mail.com",null));
    }

    @Test
    public void updateStaff1() throws Exception {
        Assertions.assertDoesNotThrow(()->
                staffService.updateStaff(1L, "吴佳熙", "副经理", "12345678910","wjx@mail.com",1L));
    }

    @Test
    public void updateStaff2() throws Exception {
        Assertions.assertThrows(BusinessException.class, () ->
                staffService.updateStaff(0L, "吴佳熙", "副经理", "12345678910", "wjx@mail.com", 1L));
    }

    @Test
    public void updateStaff3() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(-1L, "吴佳熙", "副经理", "12345678910", "wjx@mail.com", 1L));
    }

    @Test
    public void updateStaff4() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(65L, "吴佳熙", "副经理", "12345678910", "wjx@mail.com", 1L));
    }

    @Test
    public void updateStaff5() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(null, "吴佳熙", "副经理", "12345678910", "wjx@mail.com", 1L));
    }

    @Test
    public void updateStaff6() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(1L, "123", "副经理", "12345678910", "wjx@mail.com", 1L));
    }

    @Test
    public void updateStaff7() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(1L, null, "副经理", "12345678910", "wjx@mail.com", 1L));
    }

    @Test
    public void updateStaff8() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(1L, "吴佳熙", "不存在的职位", "12345678910", "wjx@mail.com", 1L));
    }

    @Test
    public void updateStaff9() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(1L, "吴佳熙", "123", "12345678910", "wjx@mail.com", 1L));
    }

    @Test
    public void updateStaff10() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(1L, "吴佳熙", null, "12345678910", "wjx@mail.com", 1L));
    }

    @Test
    public void updateStaff11() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(1L, "吴佳熙", "副经理", "123", "wjx@mail.com", 1L));
    }

    @Test
    public void updateStaff12() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(1L, "吴佳熙", "副经理", "1234567891011", "wjx@mail.com", 1L));
    }

    @Test
    public void updateStaff13() throws Exception {
        Assertions.assertDoesNotThrow(()->
                staffService.updateStaff(1L, "吴佳熙", "副经理", null, "wjx@mail.com", 1L));
    }

    @Test
    public void updateStaff14() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(1L, "吴佳熙", "副经理", "吴佳熙的手机号", "wjx@mail.com", 1L));
    }

    @Test
    public void updateStaff15() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(1L, "吴佳熙", "副经理", "12345678910", "wjx", 1L));
    }

    @Test
    public void updateStaff16() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(1L, "吴佳熙", "副经理", "12345678910", null, 1L));
    }

    @Test
    public void updateStaff17() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(1L, "吴佳熙", "副经理", "12345678910", "wjx@mail.com", 0L));
    }

    @Test
    public void updateStaff18() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(1L, "吴佳熙", "副经理", "12345678910", "wjx@mail.com", -1L));
    }

    @Test
    public void updateStaff19() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(1L, "吴佳熙", "副经理", "12345678910", "wjx@mail.com", 4L));
    }

    @Test
    public void updateStaff20() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.updateStaff(1L, "吴佳熙", "副经理", "12345678910", "wjx@mail.com", null));
    }

    @Test
    public void deleteStaff1() throws Exception {
        Assertions.assertDoesNotThrow(()->
                staffService.deleteStaff(1L));
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.findStaffById(1L));
    }

    @Test
    public void deleteStaff2() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.deleteStaff(0L));
    }

    @Test
    public void deleteStaff3() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.deleteStaff(-1L));
    }

    @Test
    public void deleteStaff4() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                staffService.deleteStaff(65L));
    }
}
