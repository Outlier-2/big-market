package cn.l13z.domain.strategy.model.entity;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: StrategyAwardEntity.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-04-16 19:52 <br> Description: 策略奖品实体 <br>
 * <p>
 * Modification History: <br> - 2024/4/16 AlfredOrlando 策略奖品实体 <br>
 */
@SuppressWarnings("unused")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardEntity {

    /**
     * 抽奖策略ID
     */
    private String strategyId;
    /**
     * 抽奖奖品ID-内部使用
     */
    private Integer awardId;
    /**
     * 奖品库存总量
     */
    private Integer awardCount;
    /**
     * 奖品库存剩余
     */
    private Integer awardCountSurplus;
    /**
     * 奖品中奖概率
     */
    private BigDecimal awardRate;
}
