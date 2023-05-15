package cn.edu.fc.store.service;

import cn.edu.fc.StoreApplication;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.util.RedisUtil;
import cn.edu.fc.service.StoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = StoreApplication.class)
@AutoConfigureMockMvc
@Transactional
public class StoreServiceTest {
    @Autowired
    private StoreService storeService;

    @Test
    public void createStore1() throws Exception {
        storeService.createStore("新门店", "福建省厦门市", 100F);
        Assertions.assertEquals("新门店", storeService.findStoreByName("新门店").getName());
        Assertions.assertEquals("福建省厦门市", storeService.findStoreByName("新门店").getAddress());
        Assertions.assertEquals(100.0, (double)storeService.findStoreByName("新门店").getSize());
    }

    @Test
    public void createStore2() throws Exception {
        storeService.createStore("", "福建省厦门市", 100F);
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.findStoreByName(""));
    }

    @Test
    public void createStore3() throws Exception {
        storeService.createStore("新门店", "", 100F);
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.findStoreByName("新门店"));
    }

    @Test
    public void createStore4() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.createStore("新门店", "福建省厦门市", null));
    }

    @Test
    public void createStore5() throws Exception {
        storeService.createStore("新门店", "福建省厦门市", -1F);
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.findStoreByName("新门店"));
    }

    @Test
    public void createStore6() throws Exception {
        storeService.createStore("123456", "福建省厦门市", 100F);
        Assertions.assertEquals("123456", storeService.findStoreByName("123456").getName());
        Assertions.assertEquals("福建省厦门市", storeService.findStoreByName("123456").getAddress());
        Assertions.assertEquals(100.0, (double)storeService.findStoreByName("123456").getSize());
    }

    @Test
    public void createStore7() throws Exception {
        storeService.createStore("新门店", "123456", 100F);
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.findStoreByName("新门店"));
    }

    @Test
    public void updateStore1() throws Exception {
        Assertions.assertDoesNotThrow(()->
                storeService.updateStore(1L, "新门店", "福建省厦门市", 100F));
    }

    @Test
    public void updateStore2() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.updateStore(0L, "新门店", "福建省厦门市", 100F));
    }

    @Test
    public void updateStore3() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.updateStore(-1L, "新门店", "福建省厦门市", 100F));
    }

    @Test
    public void updateStore4() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.updateStore(4L, "新门店", "福建省厦门市", 100F));
    }

    @Test
    public void updateStore5() throws Exception {
        storeService.updateStore(1L, "", "福建省厦门市", 100F);
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.findStoreByName(""));
    }

    @Test
    public void updateStore6() throws Exception {
        storeService.updateStore(1L, "新门店", "", 100F);
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.findStoreByName("新门店"));
    }

    @Test
    public void updateStore7() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.updateStore(1L, "新门店", "福建省厦门市", null));
    }

    @Test
    public void updateStore8() throws Exception {
        storeService.updateStore(1L, "新门店", "福建省厦门市", -1F);
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.findStoreByName("新门店"));
    }

    @Test
    public void updateStore9() throws Exception {
        storeService.updateStore(1L, "123456", "福建省厦门市", 100F);
        Assertions.assertEquals("123456", storeService.findStoreByName("123456").getName());
        Assertions.assertEquals("福建省厦门市", storeService.findStoreByName("123456").getAddress());
        Assertions.assertEquals(100.0, (double)storeService.findStoreByName("123456").getSize());
    }

    @Test
    public void updateStore10() throws Exception {
        storeService.updateStore(1L, "新门店", "123456", 100F);
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.findStoreByName("新门店"));
    }

    @Test
    public void deleteStore1() throws Exception {
        storeService.deleteStore(1L);
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.findStoreById(1L));
    }

    @Test
    public void deleteStore2() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.deleteStore(0L));
    }

    @Test
    public void deleteStore3() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.deleteStore(-1L));
    }

    @Test
    public void deleteStore4() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                storeService.deleteStore(4L));
    }
}
