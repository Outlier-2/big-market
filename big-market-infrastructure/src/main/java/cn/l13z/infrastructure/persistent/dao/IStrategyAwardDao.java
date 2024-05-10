package cn.l13z.infrastructure.persistent.dao;

import cn.l13z.infrastructure.persistent.po.StrategyAward;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author AlfredOrlando
 */
@Mapper
public interface IStrategyAwardDao {

    List<StrategyAward> queryStrategyList();

    List<StrategyAward> queryStrategyAwardList();

    String queryStrategyAwardRuleModel(StrategyAward strategyAward);
}
