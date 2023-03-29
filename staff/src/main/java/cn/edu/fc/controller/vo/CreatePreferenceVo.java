package cn.edu.fc.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CreatePreferenceVo {
    @NotNull
    private Byte type;

    @NotNull
    private String value;
}
