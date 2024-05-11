package cn.l13z.domain.strategy.service.rule.chain;

/**
 * ClassName:     ILogicChain.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * <p>
 * Created:  2024-05-09 20:22 <br> Description: 责任链接口 <br>
 * <p>
 * Modification History: <br> - 2024/5/9 AlfredOrlando 责任链接口 <br>
 */

public interface ILogicChain {

    /**
     * 执行责任链逻辑
     *
     * @param userId 用户ID
     * @param strategyId 策略ID
     * @return {@link Integer }
     */
    Integer logic(String userId, Long strategyId);

    ILogicChain appendNext(ILogicChain next);

    ILogicChain next();
}
