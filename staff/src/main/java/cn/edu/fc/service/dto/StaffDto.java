package cn.edu.fc.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class StaffDto {
    private Long id;

    private String name;

    private String position;

    private String phone;

    private String email;

    private String shopName;
}
