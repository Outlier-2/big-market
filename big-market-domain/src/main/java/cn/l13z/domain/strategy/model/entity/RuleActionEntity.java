package cn.l13z.domain.strategy.model.entity;

import cn.l13z.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * ClassName: RuleActionEntity.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-06 21:36 <br> Description: 抽奖规则触发类 <br>
 * <p>
 * Modification History: <br> - 2024/5/6 AlfredOrlando 抽奖规则触发类 <br>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {

    private  String code = RuleLogicCheckTypeVO.ALLOW.getCode();
    private  String info = RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;

    static public class RaffleEntity {

    }

    // 抽奖前触发
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    static public class RaffleBeforeEntity extends RaffleEntity {

        /**
         * 策略ID
         */
        private Long strategyId;
        /**
         * 用于抽奖是的选择权重进行抽奖的key
         */
        private String ruleWeightValueKey;
        /**
         * 奖品ID
         */
        private Integer awardId;

    }

    // 抽奖中触发
    static public class RaffleCenterEntity extends RaffleEntity {

    }

    // 抽奖后触发
    static public class RaffleAfterEntity extends RaffleEntity {

    }

}
