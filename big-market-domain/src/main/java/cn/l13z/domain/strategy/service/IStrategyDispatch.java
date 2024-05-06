package cn.l13z.domain.strategy.service;

/**
 * ClassName: IStrategyDispatch.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-06 09:31 <br> Description: 策略抽奖调度 <br>
 * <p>
 * Modification History: <br> - 2024/5/6 AlfredOrlando 策略抽奖调度 <br>
 */
public interface IStrategyDispatch {

    /**
     * 获取随机奖品ID
     * @param strategyId
     * @return {@link Integer }
     */
    Integer getRandomAwardId(Long strategyId);

    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);
}
