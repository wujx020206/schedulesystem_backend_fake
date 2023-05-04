package cn.edu.fc.store.controller;

import cn.edu.fc.StoreApplication;
import cn.edu.fc.dao.StoreDao;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.javaee.core.model.InternalReturnObject;
import cn.edu.fc.javaee.core.util.JwtHelper;
import cn.edu.fc.javaee.core.util.RedisUtil;
import cn.edu.fc.service.StoreService;
import org.junit.Assert;
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

import static cn.edu.fc.javaee.core.model.ReturnNo.RESOURCE_ID_NOTEXIST;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = StoreApplication.class)
@AutoConfigureMockMvc
@Transactional
public class StoreControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

    @Autowired
    private StoreDao storeDao;

    private static String adminToken;

    private static final String RETRIEVE_STORES = "/store/stores";

    private static final String FIND_STORE = "/store/id/{storeId}/store";

    private static final String FIND_STORE_BY_NAME = "/store/name/{storeName}/store";

    private static final String CREATE_STORE = "/store/store";

    private static final String UPDATE_STORE = "/store/{storeId}/store";

    private static final String DELETE_STORE = "/store/{storeId}/store";

    @BeforeAll
    public static void setup(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    @Test
    public void retrieveStores() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_STORES)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].name", is("门店1")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findStoreById1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(FIND_STORE,1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("门店1")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findStoreById2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        try {
            this.mockMvc.perform(MockMvcRequestBuilders.get(FIND_STORE,0)
                            .header("authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .param("page", "1")
                            .param("pageSize", "10"))
                    .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch(NestedServletException e) {
            Assertions.assertArrayEquals(e.getMessage().toCharArray(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 商铺对象(id=0)不存在".toCharArray());
        }
    }

    @Test
    public void findStoreByName1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(FIND_STORE_BY_NAME,"门店1")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("门店1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.address", is("福建省厦门市翔安区")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findStoreByName2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        try {
            this.mockMvc.perform(MockMvcRequestBuilders.get(FIND_STORE_BY_NAME,"门店x")
                            .header("authorization", adminToken)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch(NestedServletException e) {
            Assertions.assertArrayEquals(e.getMessage().toCharArray(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 商铺对象(id=null)不存在".toCharArray());
        }
    }

    @Test
    public void createStore1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String requestJson="{\"name\": \"xicha\", \"address\": \"123\",\"size\": 123.0}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_STORE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(1))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void createStore2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String requestJson="{\"name\": \"门店1\", \"address\": \"福建省厦门市翔安区\",\"size\": 123.3}";

        try {
            this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_STORE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("authorization", adminToken)
                            .content(requestJson))
                    .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(1))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch(NestedServletException e) {
            Assertions.assertArrayEquals(e.getMessage().toCharArray(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 商铺(id=1)已经存在".toCharArray());
        }

    }

    @Test
    public void updateStore1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String requestJson="{\"name\": \"xicha\", \"address\": \"123\",\"size\": 123.0}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_STORE, 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void updateStore2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String requestJson="{\"name\": \"xicha\", \"address\": \"123\",\"size\": 123.0}";

        try {
            this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_STORE, 0)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("authorization", adminToken)
                            .content(requestJson))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch(NestedServletException e) {
            Assertions.assertArrayEquals(e.getMessage().toCharArray(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 商铺对象(id=0)不存在".toCharArray());
        }
    }

    @Test
    public void deleteStore1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_STORE,1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteStore2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        try {
            this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_STORE,0)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("authorization", adminToken))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse().getContentAsString();
        } catch(NestedServletException e) {
            Assertions.assertArrayEquals(e.getMessage().toCharArray(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 商铺对象(id=0)不存在".toCharArray());
        }
    }
}
