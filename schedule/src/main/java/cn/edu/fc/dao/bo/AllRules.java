package cn.edu.fc.dao.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AllRules {
    private Integer weekDayOpenRule;
    private Integer weekDayCloseRule;
    private Integer weekendOpenRule;
    private Integer weekendCloseRule;
    private Integer maxHourPerWeek;
    private Integer maxHourPerDay;
    private Integer leastHourPerPeriod;
    private Integer maxHourPerPeriod;
    private Integer maxContinuousWorkTime;
    private Integer lunchBegin;
    private Integer lunchEnd;
    private Integer dinnerBegin;
    private Integer dinnerEnd;
    private LocalTime breakTime;
    private Integer prepareTime;
    private Integer preparePeople;
    private List<String> prepareStation;
    private Float workPeople;
    private List<String> workStation;
    private Integer leastPeople;
    private Integer endTime;
    private Integer endPeople;
    private List<String> endStation;
}
