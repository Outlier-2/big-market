package cn.l13z.domain.strategy.service.rule.chain.impl;

import cn.l13z.domain.strategy.service.armory.IStrategyDispatch;
import cn.l13z.domain.strategy.service.rule.chain.AbstractLogicChain;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ClassName: DefaultLogicChain.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-09 20:36 <br> Description: 默认过滤器链 <br>
 * <p>
 * Modification History: <br> - 2024/5/9 AlfredOrlando 默认过滤器链 <br>
 */
@Slf4j
@Component("default")
public class DefaultLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyDispatch strategyDispatch;

    @Override
    public Integer logic(String userId, Long strategyId) {
        Integer awardId = strategyDispatch.getRandomAwardId(strategyId);
        log.info("抽奖责任链-默认处理 userId:{}, strategyId:{}, awardId:{}", userId, strategyId, awardId);
        return awardId;
    }

    @Override
    protected String ruleModel() {
        return "default";
    }
}
