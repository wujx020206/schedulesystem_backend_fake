package cn.edu.fc.store.controller;

import cn.edu.fc.DataApplication;
import cn.edu.fc.dao.StoreDao;
import cn.edu.fc.javaee.core.util.JwtHelper;
import cn.edu.fc.javaee.core.util.RedisUtil;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.web.util.NestedServletException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = DataApplication.class)
@AutoConfigureMockMvc
@Transactional
public class DataControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

    private static String adminToken;

    private static final String RETRIEVE_DATA_BY_STORE = "/forecast/{storeId}/data";

    private static final String RETRIEVE_DATA_BY_PERIOD = "/forecast/{storeId}/period";

    private static final String RETRIEVE_DATA_BY_DATE = "/forecast/{storeId}/{date}/day";

    private static final String RETRIEVE_DATA_BY_DATE_AND_TIME = "/forecast/{storeId}/{date}/day/time";

    @BeforeAll
    public static void setup(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    @Test
    public void retrieveDataByStoreId1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_DATA_BY_STORE,1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].date", is("2023-03-27")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].beginTime", is("09:00:00")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveDataByStoreId2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_DATA_BY_STORE,0)
                                .header("authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .param("page", "1")
                                .param("pageSize", "10")));
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_DATA_BY_STORE,0)
                    .header("authorization", adminToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("page", "1")
                    .param("pageSize", "10"));
        } catch (NestedServletException e) {
            assertEquals(e.getMessage(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
        }
    }

    @Test
    public void retrieveDataByPeriod1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        String requestJson="{\"beginDate\": \"2023-04-01\", \"endDate\": \"2023-04-02\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_DATA_BY_PERIOD,1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].date", is("2023-04-01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].storeName", is("门店1")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveDataByPeriod2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        String requestJson="{\"beginDate\": \"2023-04-01\", \"endDate\": \"2023-04-02\"}";

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_DATA_BY_PERIOD,0)
                                .header("authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(requestJson)
                                .param("page", "1")
                                .param("pageSize", "10")));
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_DATA_BY_PERIOD,0)
                    .header("authorization", adminToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(requestJson)
                    .param("page", "1")
                    .param("pageSize", "10"));
        } catch (NestedServletException e) {
            assertEquals(e.getMessage(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
        }
    }

    @Test
    public void retrieveDataByDate1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_DATA_BY_DATE, 1, "2023-04-01")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].beginTime", is("09:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].num", is(0.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].num", is(0.1)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveDataByDate2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_DATA_BY_DATE, 0, "2023-04-01")
                                .header("authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .param("page", "1")
                                .param("pageSize", "10")));
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_DATA_BY_DATE, 0, "2023-04-01")
                    .header("authorization", adminToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("page", "1")
                    .param("pageSize", "10"));
        } catch (NestedServletException e) {
            assertEquals(e.getMessage(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
        }
    }

    @Test
    public void retrieveDataByDateAndTime1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        String requestJson="{\"beginTime\": \"09:00:00\", \"endTime\": \"09:30:00\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_DATA_BY_DATE_AND_TIME, 1, "2023-04-01")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.beginTime", is("09:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.num", is(0.0)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveDataByDateAndTime2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        String requestJson="{\"beginTime\": \"09:00:00\", \"endTime\": \"09:30:00\"}";

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_DATA_BY_DATE_AND_TIME, 0, "2023-04-01")
                                .header("authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(requestJson)));
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_DATA_BY_DATE_AND_TIME, 0, "2023-04-01")
                    .header("authorization", adminToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(requestJson));
        } catch (NestedServletException e) {
            assertEquals(e.getMessage(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
        }
    }
}
