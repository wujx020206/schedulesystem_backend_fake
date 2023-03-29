package cn.edu.fc.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class RuleDto {
    /**
     * 区分固定规则和自定义规则
     */
    private String firstType;

    /**
     * 区分不同规则的小类
     */
    private String secondType;

    private String value;

    private String shopName;
}
