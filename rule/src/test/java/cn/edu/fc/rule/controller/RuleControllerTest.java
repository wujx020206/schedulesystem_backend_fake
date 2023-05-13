package cn.edu.fc.rule.controller;

import cn.edu.fc.RuleApplication;
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

@SpringBootTest(classes = RuleApplication.class)
@AutoConfigureMockMvc
@Transactional
public class RuleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

    private static String adminToken;

    private static final String RETRIEVE_RULES = "/rule/rules";

    private static final String RETRIEVE_RULES_BY_STORE = "/rule/{storeId}/rules";

    private static final String RETRIEVE_RULES_BY_STORE_AND_TYPE = "/rule/{storeId}/{type}/rule";

    private static final String UPDATE_RULE = "/rule/{storeId}/{type}/rule";

    @BeforeAll
    public static void setup(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    @Test
    public void retrieveRules() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_RULES)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].firstType", is("固定规则")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveRulesByStore1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_RULES_BY_STORE, 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].firstType", is("固定规则")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[17].firstType", is("自定义规则")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveRulesByStore2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_RULES_BY_STORE, 0)
                                        .header("authorization", adminToken)
                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                        .param("page", "1")
                                        .param("pageSize", "10")));
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_RULES_BY_STORE, 0)
                    .header("authorization", adminToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("page", "1")
                    .param("pageSize", "10"));
        } catch (NestedServletException e) {
            assertEquals(e.getMessage(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店对象(id=0)不存在");
        }
    }

    @Test
    public void retrieveRulesByStoreAndType1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_RULES_BY_STORE_AND_TYPE, 1, "固定规则_工作日开店规则")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstType", is("固定规则")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.secondType", is("工作日开店规则")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveRulesByStoreAndType2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Assertions.assertThrows(NestedServletException.class, ()->
                        this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_RULES_BY_STORE_AND_TYPE, 1, "不存在的规则")
                                .header("authorization", adminToken)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .param("page", "1")
                                .param("pageSize", "10")));
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.get(RETRIEVE_RULES_BY_STORE_AND_TYPE, 1, "不存在的规则")
                    .header("authorization", adminToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .param("page", "1")
                    .param("pageSize", "10"));
        } catch (NestedServletException e) {
            assertEquals(e.getMessage(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店排班规则对象(id=null)不存在");
        }
    }

    @Test
    public void updateRule1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String requestJson="{\"value\": \"9\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_RULE, 1,"固定规则_工作日开店规则")
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
    public void updateRule2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);

        String requestJson="{\"value\": \"9\"}";
        Assertions.assertThrows(NestedServletException.class, ()->
                this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_RULE, 1,"不存在的规则")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("authorization", adminToken)
                        .content(requestJson)));
        try {
            this.mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_RULE, 1,"不存在的规则")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("authorization", adminToken)
                    .content(requestJson));
        } catch (NestedServletException e) {
            assertEquals(e.getMessage(), "Request processing failed; nested exception is cn.edu.fc.javaee.core.exception.BusinessException: 门店排班规则对象(id=null)不存在");
        }
    }
}
