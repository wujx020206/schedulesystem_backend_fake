package cn.edu.fc.dao.bo;

import cn.edu.fc.javaee.core.model.bo.SSObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class Store extends SSObject {
    private String name;

    private String address;

    private String size;

    @Builder
    public Store(Long id, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified,
                 String name, String address, String size) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.name = name;
        this.address = address;
        this.size = size;
    }
}

