package cn.edu.fc.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StaffScheduleDto {
    private StaffDto staff;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Builder
    public StaffScheduleDto(Long staffId, String staffName, String staffPosition, LocalDateTime startTime, LocalDateTime endTime) {
        staff = new StaffDto(staffId, staffName, staffPosition);
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

