package cn.edu.fc.controller;

import cn.edu.fc.controller.vo.CreatePreferenceVo;
import cn.edu.fc.controller.vo.StaffVo;
import cn.edu.fc.controller.vo.UpdatePreferenceVo;
import cn.edu.fc.javaee.core.aop.LoginUser;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.ReturnObject;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.service.PreferenceService;
import cn.edu.fc.service.StaffService;
import cn.edu.fc.service.dto.PreferenceDto;
import cn.edu.fc.service.dto.StaffDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/staff", produces = "application/json;charset=UTF-8")
public class AdminStaffController {
    private final Logger logger = LoggerFactory.getLogger(AdminStaffController.class);

    private final StaffService staffService;

    private final PreferenceService preferenceService;

    @Autowired
    public AdminStaffController(PreferenceService preferenceService, StaffService staffService) {
        this.preferenceService = preferenceService;
        this.staffService = staffService;
    }

    @GetMapping("/staffs")
    public ReturnObject getStaffs(@RequestParam(required = false, defaultValue = "1") Integer page,
                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<StaffDto> ret = this.staffService.retrieveStaffs(page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/{storeId}/staffs")
    public ReturnObject getShopStaffs(@PathVariable Long storeId,
                                      @RequestParam(required = false, defaultValue = "1") Integer page,
                                      @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<StaffDto> ret = this.staffService.retrieveStaffsByStoreId(storeId, page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/{staffId}/staff")
    public ReturnObject findStaffById(@PathVariable Long staffId) {
        StaffDto ret = this.staffService.findStaffById(staffId);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/{staffName}/staff")
    public ReturnObject findStaffByName(@PathVariable String staffName,
                                        @RequestParam(required = false, defaultValue = "1") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<StaffDto> ret = this.staffService.retrieveStaffByName(staffName, page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/{staffName}/staffId")
    public ReturnObject findStaffIdByName(@PathVariable String staffName,
                                          @RequestParam(required = false, defaultValue = "1") Integer page,
                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<Long> ret = this.staffService.retrieveStaffIdByName(staffName, page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @PostMapping("/staff")
    public ReturnObject createStaff(@Valid @RequestBody StaffVo vo) {
        this.staffService.createStaff(vo.getName(), vo.getPosition(), vo.getPhone(), vo.getEmail(),vo.getStoreId());
        return new ReturnObject(ReturnNo.CREATED);
    }

    @PutMapping("/{staffId}/staff")
    public ReturnObject updateStaff(@PathVariable Long staffId,
                                    @Valid @RequestBody StaffVo vo) {
        this.staffService.updateStaff(staffId, vo.getName(), vo.getPosition(), vo.getPhone(), vo.getEmail(),vo.getStoreId());
        return new ReturnObject(ReturnNo.OK);
    }

    @DeleteMapping("/{staffId}/staff")
    public ReturnObject deleteStaff(@PathVariable Long staffId) {
        this.staffService.deleteStaff(staffId);
        return new ReturnObject(ReturnNo.OK);
    }

    @GetMapping("/preferences")
    public ReturnObject getPreferences(@RequestParam(required = false, defaultValue = "1") Integer page,
                                       @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<PreferenceDto> ret = this.preferenceService.retrievePreferences(page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/{staffId}/preferences")
    public ReturnObject getStaffPreferences(@PathVariable Long staffId,
                                            @RequestParam(required = false, defaultValue = "1") Integer page,
                                            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<PreferenceDto> ret = this.preferenceService.retrievePreferencesByStaffId(staffId, page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/preferences/{type}/preferences")
    public ReturnObject getPreferencesByType(@PathVariable Byte type,
                                             @RequestParam(required = false, defaultValue = "1") Integer page,
                                             @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<PreferenceDto> ret = this.preferenceService.retrievePreferencesByType(type, page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/{staffId}/preferences/{type}/preference")
    public ReturnObject getStaffPreferenceByType(@PathVariable Long staffId,
                                                 @PathVariable Byte type) {
        PreferenceDto ret = this.preferenceService.retrievePreferencesByTypeAndStaffId(type, staffId);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @PostMapping("/{staffId}/preference")
    public ReturnObject createStaffPreference(@PathVariable Long staffId,
                                              @Valid @RequestBody CreatePreferenceVo vo) {
        this.preferenceService.createPreference(staffId, vo.getType(), vo.getValue());
        return new ReturnObject(ReturnNo.CREATED);
    }

    @PutMapping("/{staffId}/preferences/{preferenceId}/preference")
    public ReturnObject updateStaffPreference(@PathVariable Long staffId,
                                              @PathVariable Byte type,
                                              @Valid @RequestBody UpdatePreferenceVo vo) {
        this.preferenceService.updatePreference(staffId, type, vo.getValue());
        return new ReturnObject(ReturnNo.OK);
    }

    @DeleteMapping("/{staffId}/preferences/{preferenceId}/preference")
    public ReturnObject deleteStaffPreference(@PathVariable Long staffId,
                                              @PathVariable Byte type) {
        this.preferenceService.deletePreference(type, staffId);
        return new ReturnObject(ReturnNo.OK);
    }
}
