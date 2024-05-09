package cn.l13z.infrastructure.persistent.dao;

import cn.l13z.infrastructure.persistent.po.StrategyRule;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName:     IStrategyRuleDao.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * <p>
 * Created:  2024-05-04 08:05 <br> Description: StrategyRuleDao <br>
 * <p>
 * Modification History: <br> - 2024/5/4 AlfredOrlando StrategyRuleDao <br>
 */
@Mapper
public interface IStrategyRuleDao {

    List<StrategyRule> queryStrategyRuleList();

    StrategyRule queryStrategyRule(StrategyRule strategyRuleReq);

    String queryStrategyRuleValue(StrategyRule strategyRule);
}
