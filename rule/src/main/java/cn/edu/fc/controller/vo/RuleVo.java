package cn.edu.fc.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class RuleVo {
    @NotNull
    private String value;
}
