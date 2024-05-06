package cn.l13z.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: StrategyConditionEntity.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-04-16 19:53 <br> Description: 策略状态实体 <br>
 * <p>
 * Modification History: <br> - 2024/4/16 AlfredOrlando 策略状态实体 <br>
 */
@SuppressWarnings("unused")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyConditionEntity {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 策略ID
     */
    private Integer strategyId;
}
