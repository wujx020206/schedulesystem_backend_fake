package cn.edu.fc.rule.service;

import cn.edu.fc.RuleApplication;
import cn.edu.fc.javaee.core.exception.BusinessException;
import cn.edu.fc.service.RuleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = RuleApplication.class)
@AutoConfigureMockMvc
@Transactional
public class RuleServiceTest {
    @Autowired
    private RuleService ruleService;

    @Test
    public void updateRule1() throws Exception {
        Assertions.assertDoesNotThrow(()->
                ruleService.updateRule("自定义规则_准备工作时长", 1L, "1"));
    }

    @Test
    public void updateRule2() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                ruleService.updateRule("自定义规则_准备工作时长", 0L, "1"));
    }

    @Test
    public void updateRule3() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                ruleService.updateRule("自定义规则_准备工作时长", -1L, "1"));
    }

    @Test
    public void updateRule4() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                ruleService.updateRule("自定义规则_准备工作时长", 4L, "1"));
    }

    @Test
    public void updateRule5() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                ruleService.updateRule("不存在的规则", 1L, "2"));
    }

    @Test
    public void updateRule6() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                ruleService.updateRule(null, 1L, "2"));
    }

    @Test
    public void updateRule7() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                ruleService.updateRule("12_3", 1L, "2"));
    }

    @Test
    public void updateRule8() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                ruleService.updateRule("自定义规则_准备工作时长", 1L, "1 2"));
    }

    @Test
    public void updateRule9() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                ruleService.updateRule("自定义规则_准备工作时长", 1L, "wjx"));
    }

    @Test
    public void updateRule10() throws Exception {
        Assertions.assertThrows(BusinessException.class, ()->
                ruleService.updateRule("自定义规则_准备工作时长", 1L, null));
    }
}
