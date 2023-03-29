package cn.edu.fc.store.dao;

import cn.edu.fc.StoreApplication;
import cn.edu.fc.dao.StoreDao;
import cn.edu.fc.dao.bo.Store;
import cn.edu.fc.javaee.core.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = StoreApplication.class)
@Transactional
public class StoreDaoTest {
    @MockBean
    private RedisUtil redisUtil;

    @Autowired
    private StoreDao storeDao;

    @Test
    public void testFindById(){
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Store store = storeDao.findById(1L);
        assertEquals("门店1",store.getName());
        assertEquals("福建省厦门市翔安区",store.getAddress());
    }
}
