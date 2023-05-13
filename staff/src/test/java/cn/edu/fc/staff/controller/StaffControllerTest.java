package cn.edu.fc.staff.controller;

import cn.edu.fc.StaffApplication;
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

    private static final String RETRIEVE_ID = "/staff/name/{staffName}/staffId";

    private static final String RETRIEVE_STORE_STAFFS = "/staff/{storeId}/staffs";

    private static final String FIND_STAFF_BY_ID = "/staff/{staffId}/staff";

    private static final String FIND_STAFF_BY_NAME = "/staff/name/{staffName}/staff";

    private static final String CREATE_STAFF = "/staff/staff";

    private static final String UPDATE_STAFF = "/staff/{storeId}/staff";

    private static final String DELETE_STAFF = "/staff/{staffId}/staff";

    @BeforeAll
    public static void setup(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    @Test
    public void retrieveStaffs() throws Exception {
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].name", is("张三")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveStaffId() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_ID, "张三")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0]", is(1)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findByStoreId1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_STORE_STAFFS, 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].name", is("张三")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findByStoreId2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_STORE_STAFFS, 0)
                                .header("authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)));
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_STORE_STAFFS, 0)
                    .header("authorization", adminToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE));
        } catch (NestedServletException e) {
            assertEquals(e.getMessage(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
        }
    }

    @Test
    public void findByStaffId() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(FIND_STAFF_BY_ID, 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("张三")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findByStaffName() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(FIND_STAFF_BY_NAME, "李四")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].name", is("李四")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].position", is("副经理")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].phone", is("11111111111")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].email", is("ls@mail.com")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void createStaff1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String requestJson="{\"name\": \"wjx\", \"position\": \"副经理\",\"phone\": \"12345678910\", \"email\":\"123@qq.com\",\"storeId\": 1}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_STAFF)
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
    public void createStaff2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String requestJson="{\"name\": \"wjx\", \"position\": \"副经理\",\"phone\": \"12345678910\", \"email\":\"123@qq.com\",\"storeId\": 0}";

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_STAFF)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("authorization", adminToken)
                                .content(requestJson)));
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_STAFF)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("authorization", adminToken)
                    .content(requestJson));
        } catch (NestedServletException e) {
            assertEquals(e.getMessage(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
        }
    }

    @Test
    public void createStaff3() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String requestJson="{\"name\": \"张三\", \"position\": \"门店经理\",\"phone\": \"13511111111\", \"email\":\"123@qq.com\",\"storeId\": 1}";

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_STAFF)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("authorization", adminToken)
                                .content(requestJson)));
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_STAFF)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("authorization", adminToken)
                    .content(requestJson));
        } catch (NestedServletException e) {
            assertEquals(e.getMessage(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 员工(id=1)已经存在");
        }
    }

    @Test
    public void updateStaff1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String requestJson="{\"name\": \"张三\", \"position\": \"门店经理\",\"phone\": \"13511111111\", \"email\":\"123@qq.com\",\"storeId\": 1}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_STAFF, 1)
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
    public void updateStaff2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String requestJson="{\"name\": \"张三\", \"position\": \"门店经理\",\"phone\": \"13511111111\", \"email\":\"123@qq.com\",\"storeId\": 1}";

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_STAFF, 0)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("authorization", adminToken)
                                .content(requestJson)));
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_STAFF, 0)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("authorization", adminToken)
                    .content(requestJson));
        } catch (NestedServletException e) {
            assertEquals(e.getMessage(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 员工对象(id=0)不存在");
        }
    }

    @Test
    public void deleteStaff1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_STAFF,2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteStaff2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_STAFF,0)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("authorization", adminToken)));
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_STAFF,0)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("authorization", adminToken));
        } catch (NestedServletException e) {
            assertEquals(e.getMessage(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 员工对象(id=0)不存在");
        }
    }
}
