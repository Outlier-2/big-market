package cn.l13z.infrastructure.persistent.po;

import java.util.Date;
import lombok.Data;

/**
 * ClassName: Strategy.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-04 07:02 <br> Description: 策略PO <br>
 * <p>
 * Modification History: <br> - 2024/5/4 AlfredOrlando 策略PO <br>
 */
@Data
public class Strategy {

    /**
     * 自增ID
     */
    private Long id;
    /**
     * 抽奖策略ID
     */
    private Long strategyId;
    /**
     * 抽奖策略描述
     */
    private String strategyDesc;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /** 抽奖规则模型 */
    private String ruleModels;

}
