package cn.edu.fc.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DateVo {
    @NotNull
    private LocalDate beginDate;

    @NotNull
    private LocalDate endDate;
}
