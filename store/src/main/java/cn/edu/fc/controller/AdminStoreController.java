package cn.edu.fc.controller;

import cn.edu.fc.controller.vo.StoreVo;
import cn.edu.fc.javaee.core.aop.Audit;
import cn.edu.fc.javaee.core.aop.LoginUser;
import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.ReturnObject;
import cn.edu.fc.javaee.core.model.dto.PageDto;
import cn.edu.fc.javaee.core.model.dto.UserDto;
import cn.edu.fc.service.StoreService;
import cn.edu.fc.service.dto.StoreDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/store", produces = "application/json;charset=UTF-8")
public class AdminStoreController {
    private final Logger logger = LoggerFactory.getLogger(AdminStoreController.class);

    private final StoreService storeService;

    @Autowired
    public AdminStoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/stores")
    //@Audit(departName = "stores")
    public ReturnObject getStores(@RequestParam(required = false, defaultValue = "1") Integer page,
                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageDto<StoreDto> ret = this.storeService.retrieveStores(page, pageSize);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @GetMapping("/id/{storeId}/store")
    //@Audit(departName = "stores")
    public ReturnObject getStore(@PathVariable Long storeId) {
        StoreDto ret = this.storeService.findStoreById(storeId);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @PostMapping("/store")
    public ReturnObject createStore(@Valid @RequestBody StoreVo vo) {
        this.storeService.createStore(vo.getName(), vo.getAddress(), vo.getSize());
        return new ReturnObject(ReturnNo.CREATED);
    }

    @PutMapping("/{storeId}/store")
    public ReturnObject updateStore(@PathVariable Long storeId,
                                    @Valid @RequestBody StoreVo vo) {
        this.storeService.updateStore(storeId, vo.getName(),vo.getAddress(), vo.getSize());
        return new ReturnObject(ReturnNo.OK);
    }

    @GetMapping("/name/{storeName}/store")
    public ReturnObject getStoreByName(@PathVariable String storeName) {
        StoreDto ret = this.storeService.findStoreByName(storeName);
        return new ReturnObject(ReturnNo.OK, ret);
    }

    @DeleteMapping("/{storeId}/store")
    public ReturnObject deleteStore(@PathVariable Long storeId) {
        this.storeService.deleteStore(storeId);
        return new ReturnObject(ReturnNo.OK);
    }
}
