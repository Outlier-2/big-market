package cn.l13z.domain.strategy.model.entity;

import cn.l13z.types.common.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: StrategyRuleEntity.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-06 10:08 <br> Description:  <br>
 * <p>
 * Modification History: <br> - 2024/5/6 AlfredOrlando  <br>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyRuleEntity {

    /**
     * 抽奖策略ID
     */
    private Long strategyId;
    /**
     * 抽奖奖品ID【规则类型为策略，则不需要奖品ID】
     */
    private Integer awardId;
    /**
     * 抽象规则类型；1-策略规则、2-奖品规则
     */
    private Integer ruleType;
    /**
     * 抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)】
     */
    private String ruleModel;
    /**
     * 抽奖规则比值
     */
    private String ruleValue;
    /**
     * 抽奖规则描述
     */
    private String ruleDesc;

    public Map<String, List<Integer>> getRuleWeightMap() {
        if (!"rule_weight".equals(ruleModel)) {
            return null;
        }
        String[] ruleValuesGroups = ruleValue.split(Constants.SPLIT);
        Map<String, List<Integer>> resultMap = new HashMap<String, List<Integer>>();
        for (String ruleValueGroup : ruleValuesGroups) {
            // 判空
            if (ruleValueGroup == null || ruleValueGroup.isEmpty()) {
                return resultMap;
            }

            // 分割字符串获取键值对
            String[] parts = ruleValueGroup.split(Constants.COLON);
            if (parts.length != 2) {
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueGroup);
            }

            // 解析键值对
            String[] valueStrings = parts[1].split(Constants.SPLIT);
            List<Integer> values = new ArrayList<>();
            for (String valueString : valueStrings) {
                values.add(Integer.parseInt(valueString));
            }
            resultMap.put(ruleValueGroup, values);
        }
        return resultMap;
    }
}
