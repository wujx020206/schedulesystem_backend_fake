package cn.edu.fc.controller;

import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.model.ReturnObject;
import cn.edu.fc.service.StoreService;
import cn.edu.fc.service.dto.StoreDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/internal", produces = "application/json;charset=UTF-8")
public class InternalStoreController {
    private final Logger logger = LoggerFactory.getLogger(AdminStoreController.class);

    private final StoreService storeService;

    @Autowired
    public InternalStoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/{storeId}/store")
    public ReturnObject getStore(@PathVariable Long storeId) {
        StoreDto ret = this.storeService.findStoreById(storeId);
        return new ReturnObject(ReturnNo.OK, ret);
    }
}
