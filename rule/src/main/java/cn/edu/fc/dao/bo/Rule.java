package cn.edu.fc.dao.bo;

import cn.edu.fc.dao.openfeign.StoreDao;
import cn.edu.fc.javaee.core.model.bo.SSObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rule extends SSObject implements Serializable {
    /**
     * 固定规则_工作日开店规则
     *
     * value：x
     * 表示x点开始营业
     */
    public static final String WEEKDAY_OPENRULE = "固定规则_工作日开店规则";

    /**
     * 固定规则_工作日关店规则
     *
     * value：x
     * 表示x点结束营业
     */
    public static final String WEEKDAY_CLOSERULE = "固定规则_工作日关店规则";

    /**
     * 固定规则_周末开店规则
     *
     * value：x
     * 表示x点开始营业
     */
    public static final String WEEKEND_OPENRULE = "固定规则_周末开店规则";

    /**
     * 固定规则_周末关店规则
     *
     * value：x
     * 表示x点结束营业
     */
    public static final String WEEKEND_CLOSERULE = "固定规则_周末关店规则";

    /**
     * 固定规则_员工每周工作时长
     *
     * value：x
     * 表示员工每周最多工作x小时
     */
    public static final String MAXHOUR_PERWEEK = "固定规则_员工每周工作时长";

    /**
     * 固定规则_员工每天工作时长
     *
     * value：x
     * 表示员工每天最多工作x小时
     */
    public static final String MAXHOUR_PERDAY = "固定规则_员工每天工作时长";

    /**
     * 固定规则_单班次最短时长
     *
     * value：x
     * 单个班次最少x小时
     */
    public static final String LEASTHOUR_PERPERIOD = "固定规则_单班次最短时长";

    /**
     * 固定规则_单班次最长时长
     *
     * value：x
     * 单个班次最多x小时
     */
    public static final String MAXHOUR_PERPERIOD = "固定规则_单班次最长时长";

    /**
     * 固定规则_员工最长连续工作时长
     *
     * value：x
     * 员工最长连续x小时
     */
    public static final String MAX_CONTINUOUS_WORK_TIME = "固定规则_员工最长连续工作时长";

    /**
     * 固定规则_午餐开始时间
     *
     * value：x
     * 午餐x点开始
     */
    public static final String LUNCH_BEGIN = "固定规则_午餐开始时间";

    /**
     * 固定规则_午餐结束时间
     *
     * value：x
     * 午餐x点结束
     */
    public static final String LUNCH_END = "固定规则_午餐结束时间";

    /**
     * 固定规则_晚餐开始时间
     *
     * value：x
     * 晚餐x点开始
     */
    public static final String DINNER_BEGIN = "固定规则_晚餐开始时间";

    /**
     * 固定规则_晚餐结束时间
     *
     * value：x
     * 晚餐x点结束
     */
    public static final String DINNER_END = "固定规则_晚餐结束时间";

    /**
     * 固定规则_休息时长
     *
     * value：x
     * 午餐x点结束
     */
    public static final String BREAK_TIME = "固定规则_休息时长";

    /**
     * 自定义规则_准备工作时长
     *
     * value：x 缺省值为1
     * 每天开店之前需要x小时做准备工作
     */
    public static final String PREPARE_TIME = "自定义规则_准备工作时长";

    /**
     * 自定义规则_准备工作人数
     *
     * value：x 缺省值为100
     * 准备工作人数=门店面积/x
     */
    public static final String PREPARE_PEOPLE = "自定义规则_准备工作人数";

    /**
     * 自定义规则_准备工作职位
     *
     * value："x y ... z" 空格隔开，x、y表示不同职位type值
     * 允许执行此类工作的职位
     */
    public static final String PREPARE_STATION = "自定义规则_准备工作职位";

    /**
     * 自定义规则_工作店员需求数
     *
     * value：x 缺省值为3.8
     * 店员需求数=预测客流/x
     */
    public static final String WORK_PEOPLE = "自定义规则_工作店员需求数";

    /**
     * 自定义规则_工作职位
     *
     * value："x y ... z" 空格隔开，x、y表示不同职位type值
     * 允许执行此类工作的职位
     */
    public static final String WORK_STATION = "自定义规则_工作职位";

    /**
     * 自定义规则_无客流量店员数
     *
     * value：x 缺省值为1
     * 没有客流量的时候至少需要x个店员
     */
    public static final String LEAST_PEOPLE = "自定义规则_无客流量店员数";

    /**
     * 自定义规则_收尾工作时长
     *
     * value：x 缺省值为2
     * 每天关店之后需要x小时做收尾工作
     */
    public static final String END_TIME = "自定义规则_收尾工作时长";

    /**
     * 自定义规则_收尾工作人数
     *
     * value：”x y“ x缺省值为80，y缺省值为1
     * 收尾工作人数=y+门店面积/x
     */
    public static final String END_PEOPLE = "自定义规则_收尾工作人数";

    /**
     * 自定义规则_收尾工作职位
     *
     * value："x y ... z" 空格隔开，x、y表示不同职位type值
     * 允许执行此类工作的职位
     */
    public static final String END_STATION = "自定义规则_收尾工作职位";

    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(Rule.class);

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private String value;

    @Getter
    @Setter
    private Long storeId;

    private Store store;

    @ToString.Exclude
    @JsonIgnore
    @Setter
    private StoreDao storeDao;

    public Store getStore() {
        if (null == this.storeId) {
            return null;
        }

        if (null == this.store && null != this.storeDao) {
            this.store = this.storeDao.getStoreById(this.storeId).getData();
        }

        return this.store;
    }

    @Builder
    public Rule(Long id, Long creatorId, Long modifierId, String modifierName, String creatorName, LocalDateTime gmtCreate, LocalDateTime gmtModified,
                String type, String value, Long storeId) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.type = type;
        this.value = value;
        this.storeId = storeId;
    }
}
