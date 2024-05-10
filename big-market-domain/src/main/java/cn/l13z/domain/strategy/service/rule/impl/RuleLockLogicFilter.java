package cn.l13z.domain.strategy.service.rule.impl;

import cn.l13z.domain.strategy.model.entity.RuleActionEntity;
import cn.l13z.domain.strategy.model.entity.RuleActionEntity.RaffleCenterEntity;
import cn.l13z.domain.strategy.model.entity.RuleMatterEntity;
import cn.l13z.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import cn.l13z.domain.strategy.repository.IStrategyRepository;
import cn.l13z.domain.strategy.service.annotation.LogicStrategy;
import cn.l13z.domain.strategy.service.rule.ILogicFilter;
import cn.l13z.domain.strategy.service.rule.factory.DefaultLogicFactory;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ClassName: RuleLockLogicFilter.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-08 22:38 <br> Description: 用户抽奖n次后，对应的商品可解锁 <br>
 * <p>
 * Modification History: <br> - 2024/5/8 AlfredOrlando 用户抽奖n次后，对应的商品可解锁 <br>
 */

@Slf4j
@Service
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_LOCK)
public class RuleLockLogicFilter implements ILogicFilter<RuleActionEntity.RaffleCenterEntity> {

    private final Long userRaffleCount = 0L;
    @Resource
    private IStrategyRepository repository;

    @Override
    public RuleActionEntity<RaffleCenterEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-次数锁 userId:{} strategyId:{} ruleModel:{}", ruleMatterEntity.getUserId(),
            ruleMatterEntity.getStrategyId(), ruleMatterEntity.getRuleModel());

        String ruleValue = repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(),
            ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        long raffleCount = Long.parseLong(ruleValue);

        if (userRaffleCount >= raffleCount) {
            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
        }

        return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
            .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
            .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
            .build();
    }
}