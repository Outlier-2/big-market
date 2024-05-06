package cn.l13z.domain.strategy.model.entity;

import cn.l13z.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName: StrategyEntity.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-06 09:43 <br> Description:  <br>
 * <p>
 * Modification History: <br> - 2024/5/6 AlfredOrlando  <br>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyEntity {

    private static final Logger log = LoggerFactory.getLogger(StrategyEntity.class);
    private Long strategyId;
    private String strategyDesc;
    private String ruleModels;

    public String[] ruleModels() {
        log.info("ruleModels is {}", ruleModels);
        if (StringUtils.isBlank(ruleModels)) {
            return null;
        }
        return ruleModels.split(Constants.SPLIT);
    }

    public String getRuleWeight() {
        String[] ruleModels = this.ruleModels();
        for (String ruleModel : ruleModels) {
            if (!"rule_weight".equals(ruleModel)) {
                return ruleModel;
            }
        }
        return null;
    }
}
