package cn.edu.fc.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class StoreVo {
    @NotNull
    private String name;

    @NotNull
    private String address;

    @NotNull
    private Float size;
}
