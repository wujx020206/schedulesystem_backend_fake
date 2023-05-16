package cn.edu.fc.staff.service;

import cn.edu.fc.StaffApplication;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.service.PreferenceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = StaffApplication.class)
@AutoConfigureMockMvc
@Transactional
public class PreferenceServiceTest {
    @Autowired
    private PreferenceService preferenceService;

    @Test
    public void createPreference1() throws Exception {
        preferenceService.deletePreference((byte) 0,1L);
        Assertions.assertDoesNotThrow(()->
                preferenceService.createPreference(1L, (byte) 0, "2"));
        Assertions.assertEquals("2", preferenceService.retrievePreferencesByTypeAndStaffId((byte) 0, 1L).getValue());
    }

    @Test
    public void createPreference2() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.createPreference(0L, (byte) 0, "2"));
    }

    @Test
    public void createPreference3() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.createPreference(-1L, (byte) 0, "2"));
    }

    @Test
    public void createPreference4() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.createPreference(65L, (byte) 0, "2"));
    }

    @Test
    public void createPreference5() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.createPreference(null, (byte) 0, "2"));
    }

    @Test
    public void createPreference6() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.createPreference(1L, (byte) -1, "2"));
    }

    @Test
    public void createPreference7() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.createPreference(1L, (byte) 3, "2"));
    }

    @Test
    public void createPreference8() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.createPreference(1L, null, "2"));
    }

    @Test
    public void createPreference9() throws Exception {
        preferenceService.deletePreference((byte) 0,1L);
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.createPreference(1L, (byte) 0, "123456"));
    }

    @Test
    public void createPreference10() throws Exception {
        preferenceService.deletePreference((byte) 0,1L);
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.createPreference(1L, (byte) 0, "wjx"));
    }

    @Test
    public void createPreference11() throws Exception {
        preferenceService.deletePreference((byte) 0,1L);
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.createPreference(1L, (byte) 0, null));
    }

    @Test
    public void updatePreference1() throws Exception {
        Assertions.assertDoesNotThrow(()->
                preferenceService.updatePreference(1L, (byte) 0, "2"));
    }

    @Test
    public void updatePreference2() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.updatePreference(0L, (byte) 0, "2"));
    }

    @Test
    public void updatePreference3() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.updatePreference(-1L, (byte) 0, "2"));
    }

    @Test
    public void updatePreference4() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.updatePreference(65L, (byte) 0, "2"));
    }

    @Test
    public void updatePreference5() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.updatePreference(null, (byte) 0, "2"));
    }

    @Test
    public void updatePreference6() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.updatePreference(1L, (byte) -1, "2"));
    }

    @Test
    public void updatePreference7() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.updatePreference(1L, (byte) 3, "2"));
    }

    @Test
    public void updatePreference8() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.updatePreference(1L, null, "2"));
    }

    @Test
    public void updatePreference9() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.updatePreference(1L, (byte) 0, "123456"));
    }

    @Test
    public void updatePreference10() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.updatePreference(1L, (byte) 0, "wjx"));
    }

    @Test
    public void updatePreference11() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.updatePreference(1L, (byte) 0, null));
    }

    @Test
    public void deletePreference1() throws Exception {
        Assertions.assertDoesNotThrow(()->
                preferenceService.deletePreference((byte) 0, 1L));
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.findPreferenceIdByTypeAndStaffId((byte) 0, 1L));
    }

    @Test
    public void deletePreference2() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.deletePreference((byte) -1, 1L));
    }

    @Test
    public void deletePreference3() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.deletePreference((byte) 3, 1L));
    }

    @Test
    public void deletePreference4() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.deletePreference(null, 1L));
    }

    @Test
    public void deletePreference5() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.deletePreference((byte) 0, 0L));
    }

    @Test
    public void deletePreference6() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.deletePreference((byte) 0, -1L));
    }

    @Test
    public void deletePreference7() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.deletePreference((byte) 0, 65L));
    }

    @Test
    public void deletePreference8() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                preferenceService.deletePreference((byte) 0, null));
    }
}
