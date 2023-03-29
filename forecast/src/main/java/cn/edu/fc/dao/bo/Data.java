package cn.edu.fc.dao.bo;

import cn.edu.fc.dao.openfeign.StoreDao;
import cn.edu.fc.javaee.core.model.bo.SSObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data extends SSObject implements Serializable {
    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(Data.class);

    @Getter
    @Setter
    private Long storeId;

    @Getter
    @Setter
    private LocalDate date;

    @Getter
    @Setter
    private String beginTime;

    @Getter
    @Setter
    private String endTime;

    @Getter
    @Setter
    private Float num;

    private Store store;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private StoreDao storeDao;

    public Store getStore() {
        if (null == this.storeId) {
            return null;
        }

        if (null == this.store && null != this.storeDao) {
            this.store = this.storeDao.getStoreById(this.storeId).getData();
        }

        return this.store;
    }

    @Builder
    public Data(Long id, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified,
                Long storeId, LocalDate date, String beginTime, String endTime, Float num) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.storeId = storeId;
        this.date = date;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.num = num;
    }
}
