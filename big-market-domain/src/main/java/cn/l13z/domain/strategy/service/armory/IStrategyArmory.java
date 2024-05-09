package cn.l13z.domain.strategy.service.armory;

/**
 * ClassName:     IStrategyArmory.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * <p>
 * Created:  2024-04-16 20:32 <br> Description: 策略装配厂接口 <br>
 * <p>
 * Modification History: <br> - 2024/4/16 AlfredOrlando 策略装配厂 接口<br>
 */
public interface IStrategyArmory {

    /**
     * 装配抽奖策略配置 「触发的时机可以为活动审核通过后进行调用」
     * @param strategyId 策略ID
     * @return 装配结果
     */
    boolean assembleLotteryStrategy(Long strategyId);


}
