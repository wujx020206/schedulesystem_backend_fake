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

@SpringBootTest(classes = StaffApplication.class)
@AutoConfigureMockMvc
@Transactional
public class PreferenceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

    private static String adminToken;

    private static final String RETRIEVE_PREFERENCE = "/staff/preferences";

    private static final String RETRIEVE_PREFERENCE_BY_STAFF = "/staff/{staffId}/preferences";

    private static final String RETRIEVE_PREFERENCE_BY_TYPE = "/staff/preferences/{type}/preferences";

    private static final String RETRIEVE_PREFERENCE_BY_STAFF_AND_TYPE = "/staff/{staffId}/preferences/{type}/preference";

    private static final String FIND_PREFERENCEID_BY_STAFF_AND_TYPE = "/staff/{staffId}/preferences/{type}/preference/id";

    private static final String CREATE_PREFERENCE = "/staff/{staffId}/preference";

    private static final String UPDATE_PREFERENCE = "/staff/{staffId}/preferences/{preferenceId}/preference/{type}";

    private static final String DELETE_PREFERENCE = "/staff/{staffId}/preferences/{type}/preference";

    @BeforeAll
    public static void setup(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    @Test
    public void retrievePreferences() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_PREFERENCE)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].value", is("3 4 5 6")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrievePreferencesByStaffId() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_PREFERENCE_BY_STAFF, 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].value", is("3 4 5 6")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].value", is("8 18")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrievePreferencesByType() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_PREFERENCE_BY_TYPE, 0)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].value", is("3 4 5 6")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].staffName", is("李四")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].staffName", is("王五")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrievePreferencesByStaffAndType() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_PREFERENCE_BY_STAFF_AND_TYPE, 1, 0)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.value", is("3 4 5 6")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findPreferenceIDByStaffAndType1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(FIND_PREFERENCEID_BY_STAFF_AND_TYPE, 1, 0)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", is(1)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findPreferenceIDByStaffAndType2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.get(FIND_PREFERENCEID_BY_STAFF_AND_TYPE, 0, 0)
                                .header("authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 员工偏好对象(id=null)不存在");
    }

    @Test
    public void createPreference1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String requestJson="{\"type\": 2, \"value\": \"4 20\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_PREFERENCE,1, 2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("authorization", adminToken));
        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_PREFERENCE, 1)
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
    public void createPreference2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String requestJson="{\"type\": 0, \"value\": \"1 2 3\"}";

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_PREFERENCE, 1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("authorization", adminToken)
                                .content(requestJson)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 员工偏好(id=1)已经存在");
    }

    @Test
    public void createPreference3() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String requestJson="{\"type\": 0, \"value\": \"1 2 3\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_PREFERENCE,1, 0)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("authorization", adminToken));
        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_PREFERENCE, 0)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("authorization", adminToken)
                                .content(requestJson)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 员工对象(id=0)不存在");
    }

    @Test
    public void updatePreference1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String requestJson="{\"value\": \"2 3 4\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_PREFERENCE, 1, 1, 0)
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
    public void updatePreference2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String requestJson="{\"value\": \"2 3 4\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_PREFERENCE,1, 2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("authorization", adminToken));
        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_PREFERENCE, 1, 3, 2)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("authorization", adminToken)
                                .content(requestJson)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 员工偏好对象(id=null)不存在");
    }

    @Test
    public void deletePreference1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_PREFERENCE,1, 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(0))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deletePreference2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_PREFERENCE,1, 2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("authorization", adminToken));
        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_PREFERENCE,1, 2)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("authorization", adminToken)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 员工偏好对象(id=null)不存在");
    }
}
