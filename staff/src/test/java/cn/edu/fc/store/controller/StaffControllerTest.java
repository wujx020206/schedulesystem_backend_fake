package cn.edu.fc.store.controller;

import cn.edu.fc.StaffApplication;
import cn.edu.fc.javaee.core.util.JwtHelper;
import cn.edu.fc.javaee.core.util.RedisUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = StaffApplication.class)
@AutoConfigureMockMvc
@Transactional
public class StaffControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

    private static String adminToken;

    private static final String RETRIEVE_STAFFS = "/staff/staffs";

    private static final String DELETE_STAFF = "/staff/{staffId}/staff";

    @BeforeAll
    public static void setup(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    @Test
    public void retrieveStores() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_STAFFS)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].name", is("wujiaxi")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteStore() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_STAFF,1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }
}
