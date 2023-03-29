package cn.edu.fc.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class DataDto {
    private Long storeId;

    private LocalDate date;

    private String beginTime;

    private String endTime;

    private Float num;
}
