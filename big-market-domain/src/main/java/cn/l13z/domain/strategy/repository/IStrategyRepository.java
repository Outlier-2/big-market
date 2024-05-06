package cn.l13z.domain.strategy.repository;

import cn.l13z.domain.strategy.model.entity.StrategyAwardEntity;
import java.util.List;
import java.util.Map;

/**
 * ClassName:     IStrategyRepository.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * <p>
 * Created:  2024-04-16 20:20 <br> Description: 策略存储接口 <br>
 * <p>
 * Modification History: <br> - 2024/4/16 AlfredOrlando 策略存储接口 <br>
 */
public interface IStrategyRepository {

    /**
     * 查询策略奖励信息
     *
     * @param strategyId 策略ID
     * @return {@link List}<{@link StrategyAwardEntity}>
     */
    List<StrategyAwardEntity> queryStrategyAward(Long strategyId);

    /**
     * 存储策略奖励搜索率
     * @param strategyId 策略ID
     * @param rateRange 搜索率范围
     * @param strategyAwardSearchRateTable 策略奖励搜索率表
     */
    void storeStrategyAwardSearchRate(Long strategyId, Integer rateRange,
        Map<Integer, Integer> strategyAwardSearchRateTable);

    /**
     * 获取指定策略奖励和搜索率对应的奖励组成
     * @param strategyId 策略ID
     * @param rateKey 搜索率键
     * @return 策略奖励组成
     */
    Integer getStrategyAwardAssemble(Long strategyId, Integer rateKey);

    /**
     * 获取策略的搜索率范围
     * @param strategyId 策略ID
     * @return 搜索率范围
     */
    int getRateRange(Long strategyId);

}
