package cn.l13z.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: RaffleFactorEntity.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-06 11:09 <br> Description:  <br>
 * <p>
 * Modification History: <br> - 2024/5/6 AlfredOrlando  <br>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleFactorEntity {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 策略ID
     */
    private Long strategyId;
    /**
     * 奖品ID
     */
    private Integer awardId;

}
