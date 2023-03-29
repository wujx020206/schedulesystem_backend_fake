package cn.edu.fc.controller;

import cn.edu.fc.controller.vo.RuleVo;
import cn.edu.fc.javaee.core.aop.LoginUser;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.ReturnObject;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.service.RuleService;
import cn.edu.fc.service.dto.RuleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/rule", produces = "application/json;charset=UTF-8")
public class AdminRuleController {
    private final Logger logger = LoggerFactory.getLogger(AdminRuleController.class);

    private final RuleService ruleService;

    @Autowired
    public AdminRuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @GetMapping("/rules")
    public ReturnObject getRules(@RequestParam(required = false, defaultValue = "1") Integer page,
                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<RuleDto> ret = this.ruleService.retrieveRules(page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/{storeId}/rules")
    public ReturnObject getStoreRules(@PathVariable Long storeId,
                                      @RequestParam(required = false, defaultValue = "1") Integer page,
                                      @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<RuleDto> ret = this.ruleService.retrieveRulesByStoreId(storeId, page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/{storeId}/{type}/rule")
    public ReturnObject getStoreRuleByType(@PathVariable Long storeId,
                                           @PathVariable String type) {
        RuleDto ret = this.ruleService.findByStoreIdAndType(storeId, type);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @PutMapping("/{storeId}/{type}/rule")
    public ReturnObject updateStoreRuleByType(@PathVariable Long storeId,
                                              @PathVariable String type,
                                              @Valid @RequestBody RuleVo vo,
                                              @LoginUser UserDto user) {
        this.ruleService.updateRule(type, storeId, vo.getValue(), user);
        return new ReturnObject(ReturnNo.OK);
    }
}
