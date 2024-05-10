package cn.l13z.domain.strategy.service.rule;

import cn.l13z.domain.strategy.model.entity.RuleMatterEntity;
import cn.l13z.domain.strategy.model.entity.RuleActionEntity;

/**
 * ClassName:     ILogicFilter.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * <p>
 * Created:  2024-05-06 21:55 <br> Description:  <br>
 * <p>
 * Modification History: <br> - 2024/5/6 AlfredOrlando  <br>
 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {

    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);
}
