package cn.edu.fc.staff.controller;

import cn.edu.fc.ScheduleApplication;
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
import org.springframework.util.Assert;
import org.springframework.web.util.NestedServletException;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.*;

@SpringBootTest(classes = ScheduleApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ScheduleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

    private static String adminToken;

    private static final String RETRIEVE_SCHEDULE_BY_DAY = "/schedule/{storeId}/day/{date}/day";

    private static final String RETRIEVE_SCHEDULE_BY_SKILL = "/schedule/{storeId}/day/{date}/skill/{skill}/day";

    private static final String RETRIEVE_SCHEDULE_BY_POSITION = "/schedule/{storeId}/day/{date}/position/{position}/day";

    private static final String RETRIEVE_SCHEDULE_BY_STAFF = "/schedule/{storeId}/day/{date}/staff/{staffId}/day";

    private static final String RETRIEVE_SCHEDULE_BY_WEEK = "/schedule/{storeId}/week/{date}/week";

    private static final String RETRIEVE_SCHEDULE_BY_WEEK_AND_SKILL = "/schedule/{storeId}/week/{date}/skill/{skill}/week";

    private static final String RETRIEVE_SCHEDULE_BY_WEEK_AND_POSITION = "/schedule/{storeId}/week/{date}/position/{position}/week";

    private static final String RETRIEVE_SCHEDULE_BY_WEEK_AND_STAFF = "/schedule/{storeId}/week/{date}/staff/{staffId}/week";

    private static final String FIND_ID_BY_STAFF_AND_PERIOD = "/schedule/{storeId}/id/{staffId}/{start}/{end}/period";

    private static final String UPDATE_SCHEDULE = "/schedule/{storeId}/{id}/{name}/period";

    private static final String DELETE_SCHEDULE = "/schedule/{storeId}/{id}/id";

    @BeforeAll
    public static void setup(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    @Test
    public void retrieveScheduleByDay1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_DAY, 1, "2023-04-01")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].staff.name", notNullValue()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveScheduleByDay2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_DAY, 0, "2023-04-06")
                    .header("authorization", adminToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
    }

    @Test
    public void retrieveScheduleByDay3() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_DAY, 1, "2023-04-17")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].staff.name", notNullValue()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveScheduleBySkill1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_SKILL, 1, "2023-04-01", "导购")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].staff.name", is("郑卓")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].staff.name", is("唐凤")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveScheduleBySkill2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_SKILL, 0, "2023-04-01", "导购")
                                .header("authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
    }

    @Test
    public void retrieveScheduleByPosition1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_POSITION, 1, "2023-04-01", "副经理")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].staff.name", is("王五")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].startTime", is("2023-04-01 12:30:00")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveScheduleByPosition2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_POSITION, 0, "2023-04-01", "副经理")
                                .header("authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
    }

    @Test
    public void retrieveScheduleByStaff1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_STAFF, 1, "2023-04-01", 3)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].staff.name", is("王五")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].startTime", is("2023-04-01 12:30:00")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveScheduleByStaff2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_STAFF, 0, "2023-04-01", 3)
                                .header("authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
    }

    @Test
    public void retrieveScheduleByWeek1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_WEEK, 1, "2023-03-27")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].staff.name", notNullValue()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveScheduleByWeek2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_WEEK, 1, "2023-04-10")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].staff.name", notNullValue()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveScheduleByWeek3() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_WEEK, 0, "2023-03-27")
                                .header("authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
    }

    @Test
    public void retrieveScheduleBySkillAndWeek1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_WEEK_AND_SKILL, 1, "2023-03-27", "导购")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].staff.name", is("唐凤")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].startTime", is("2023-03-27 08:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].staff.name", is("范月")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].startTime", is("2023-03-27 12:00:00")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveScheduleBySkillAndWeek2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_WEEK_AND_SKILL, 0, "2023-03-27", "导购")
                                .header("authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
    }

    @Test
    public void retrieveScheduleByPositionAndWeek1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_WEEK_AND_POSITION, 1, "2023-03-27", "副经理")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].staff.name", is("易元")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].startTime", is("2023-03-27 11:00:00")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveScheduleByPositionAndWeek2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_WEEK_AND_POSITION, 0, "2023-03-27", "副经理")
                                .header("authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
    }

    @Test
    public void retrieveScheduleByStaffAndWeek1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_WEEK_AND_STAFF, 1, "2023-03-27", 3)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].staff.name", is("王五")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].startTime", is("2023-03-27 13:30:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].startTime", is("2023-03-27 20:00:00")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveScheduleByStaffAndWeek2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_SCHEDULE_BY_WEEK_AND_STAFF, 0, "2023-03-27", 3)
                                .header("authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
    }

    @Test
    public void findIdByStaffAndPeriod1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(FIND_ID_BY_STAFF_AND_PERIOD, 1, 1, "2023-03-29 12:30:00", "2023-03-29 16:30:00")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", is(53)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findIdByStaffAndPeriod2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.get(FIND_ID_BY_STAFF_AND_PERIOD, 1, 1, "2023-03-29 10:30:00", "2023-03-29 16:30:00")
                                .header("authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 排班结果对象(id=0)不存在");
    }

    @Test
    public void updateStaff1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_SCHEDULE, 1,1,"李四")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken))
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

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_SCHEDULE, 0,1,"李四")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("authorization", adminToken)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 员工对象(id=2)不存在在门店对象(id=2)中");
    }

    @Test
    public void updateStaff3() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_SCHEDULE, 1,1,"不存在的员工")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("authorization", adminToken)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 员工对象(id=1)不存在");
    }

    @Test
    public void updateStaff4() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_SCHEDULE, 2,1,"李四")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("authorization", adminToken)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
    }

    @Test
    public void updateStaff5() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_SCHEDULE, 2,10000,"李四")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("authorization", adminToken)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
    }

    @Test
    public void deleteStaff1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_SCHEDULE, 1,1)
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
                        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_SCHEDULE, 0,1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("authorization", adminToken)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
    }

    @Test
    public void deleteStaff3() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_SCHEDULE, 1,0)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .header("authorization", adminToken)),
                "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 排班结果对象(id=0)不存在");
    }
}
