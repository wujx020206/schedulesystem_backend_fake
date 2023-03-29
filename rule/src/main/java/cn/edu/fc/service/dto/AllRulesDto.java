package cn.edu.fc.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class AllRulesDto {
    private Long weekDayOpenRule;
    private Long weekDayCloseRule;
    private Long weekendOpenRule;
    private Long weekendCloseRule;
    private Long maxHourPerWeek;
    private Long maxHourPerDay;
    private Long leastHourPerPeriod;
    private Long maxHourPerPeriod;
    private Long maxWorkWeek;
    private Long lunchBegin;
    private Long lunchEnd;
    private Long dinnerBegin;
    private Long dinnerEnd;
    private Long breakTime;
    private Long prepareTime;
    private Long preparePeople;
    private List<String> prepareStation;
    private Float workPeople;
    private List<String> workStation;
    private Long leastPeople;
    private Long endHour;
    private Long endPeople;
    private List<String> endStation;

    public void setEndPeople(Long x, Long y) {
        // Todo
        throw new UnsupportedOperationException();
    }
}
