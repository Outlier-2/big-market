package cn.l13z.domain.strategy.service;

import cn.l13z.domain.strategy.model.entity.RaffleAwardEntity;
import cn.l13z.domain.strategy.model.entity.RaffleFactorEntity;
import cn.l13z.domain.strategy.model.entity.RuleActionEntity;
import cn.l13z.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import cn.l13z.domain.strategy.model.valobj.StrategyRuleModelVO;
import cn.l13z.domain.strategy.repository.IStrategyRepository;
import cn.l13z.domain.strategy.service.armory.IStrategyDispatch;
import cn.l13z.domain.strategy.service.rule.chain.ILogicChain;
import cn.l13z.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import cn.l13z.types.enums.ResponseCode;
import cn.l13z.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * ClassName: AbstractRaffleStrategy.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-06 11:05 <br> Description: 抽象抽奖策略类 <br>
 * <p>
 * Modification History: <br> - 2024/5/6 AlfredOrlando 抽象抽奖策略类 <br>
 */
@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

    // 抽奖的责任链 -> 从抽奖的规则中，解耦出前置规则为责任链处理
    private final DefaultChainFactory defaultChainFactory;
    protected IStrategyRepository strategyRepository;
    // 抽奖调度器
    protected IStrategyDispatch strategyDispatch;

    public AbstractRaffleStrategy(IStrategyDispatch strategyDispatch, IStrategyRepository strategyRepository,
        DefaultChainFactory defaultChainFactory) {
        this.strategyDispatch = strategyDispatch;
        this.strategyRepository = strategyRepository;
        this.defaultChainFactory = defaultChainFactory;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {

        log.info("抽奖策略开始执行...");
        log.info("抽奖策略参数：{}", raffleFactorEntity);
        // 1. 参数校验
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (null == strategyId || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        // 2. 获取抽奖责任链 - 前置规则的责任链处理
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);
        // 3. 通过责任链获得，奖品ID
        Integer awardId = logicChain.logic(userId, strategyId);

        // 4. 查询奖品规则「抽奖中（拿到奖品ID时，过滤规则）、抽奖后（扣减完奖品库存后过滤，抽奖中拦截和无库存则走兜底）」
        StrategyRuleModelVO strategyAwardRuleModelVO = strategyRepository.queryStrategyAwardRuleModel(strategyId,
            awardId);
        // 5. 抽奖中 - 规则过滤
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionCenterEntity = this.doCheckRaffleCenterLogic(
            RaffleFactorEntity.builder()
                .userId(userId)
                .strategyId(strategyId)
                .awardId(awardId)
                .build(), strategyAwardRuleModelVO.raffleCenterRuleModelList());

        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionCenterEntity.getCode())) {
            log.info("【临时日志】中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。");
            return RaffleAwardEntity.builder()
                .awardDesc("中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。")
                .build();
        }

        return RaffleAwardEntity.builder()
            .awardId(awardId)
            .build();

    }


    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(
        RaffleFactorEntity raffleFactorEntity, String... logics);

}



