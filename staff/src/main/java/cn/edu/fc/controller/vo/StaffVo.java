package cn.edu.fc.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class StaffVo {
    @NotNull
    private String name;

    @NotNull
    private String position;

    @NotNull
    private String phone;

    @NotNull
    private String email;

    @NotNull
    private Long storeId;
}
