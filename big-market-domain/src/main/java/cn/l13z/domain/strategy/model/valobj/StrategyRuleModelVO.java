package cn.l13z.domain.strategy.model.valobj;

import cn.l13z.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import cn.l13z.types.common.Constants;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: StrategyRuleModelVO.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-09 01:52 <br> Description: 抽奖策略规则规则值对象 <br>
 * <p>
 * Modification History: <br> - 2024/5/9 AlfredOrlando 抽奖策略规则规则值对象 <br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StrategyRuleModelVO {

    private String ruleModels;

    public String[] raffleCenterRuleModelList() {

        List<String> ruleModelList = new ArrayList<>();
        String[] ruleModelValues = ruleModels.split(Constants.SPLIT);
        for (String ruleModelValue : ruleModelValues) {
            if (DefaultLogicFactory.LogicModel.isCenter(ruleModelValue)) {
                ruleModelList.add(ruleModelValue);
            }
        }
        return ruleModelList.toArray(new String[0]);
    }

}
