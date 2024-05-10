package cn.l13z.domain.strategy.service.raffle;

import cn.l13z.domain.strategy.model.entity.RaffleAwardEntity;
import cn.l13z.domain.strategy.model.entity.RaffleFactorEntity;
import cn.l13z.domain.strategy.model.entity.RuleActionEntity;
import cn.l13z.domain.strategy.model.entity.StrategyEntity;
import cn.l13z.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import cn.l13z.domain.strategy.model.valobj.StrategyRuleModelVO;
import cn.l13z.domain.strategy.repository.IStrategyRepository;
import cn.l13z.domain.strategy.service.IRaffleStrategy;
import cn.l13z.domain.strategy.service.armory.IStrategyDispatch;
import cn.l13z.domain.strategy.service.rule.factory.DefaultLogicFactory;
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

    protected IStrategyRepository strategyRepository;

    // 抽奖调度器
    protected IStrategyDispatch strategyDispatch;

    public AbstractRaffleStrategy(IStrategyDispatch strategyDispatch, IStrategyRepository strategyRepository) {
        this.strategyDispatch = strategyDispatch;
        this.strategyRepository = strategyRepository;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {

        log.info("抽奖策略开始执行...");
        log.info("抽奖策略参数：{}", raffleFactorEntity);
        // 校验参数
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (null == strategyId || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        // 策略查询
        StrategyEntity strategy = strategyRepository.queryStrategyEntityByStrategyId(strategyId);

        //3. 抽奖前 - 规则过滤
        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionBeforeEntity = this.doCheckRaffleBeforeLogic(
            RaffleFactorEntity.builder().userId(userId).strategyId(strategyId).build(), strategy.ruleModels());

        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionBeforeEntity.getCode())) {
            if (DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleActionBeforeEntity.getRuleModel())) {
                return RaffleAwardEntity.builder()
                    .awardId(ruleActionBeforeEntity.getData().getAwardId())
                    .build();
            } else if (DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode()
                .equals(ruleActionBeforeEntity.getRuleModel())) {
                // 权重根据返回的信息进行抽奖
                RuleActionEntity.RaffleBeforeEntity raffleBeforeEntity = ruleActionBeforeEntity.getData();
                String ruleWeightValueKey = raffleBeforeEntity.getRuleWeightValueKey();
                Integer awardId = strategyDispatch.getRandomAwardId(strategyId, ruleWeightValueKey);
                return RaffleAwardEntity.builder()
                    .awardId(awardId)
                    .build();
            }
        }
        // 4. 默认抽奖流程
        Integer awardId = strategyDispatch.getRandomAwardId(strategyId);

        // 5. 查询奖品规则
        StrategyRuleModelVO strategyRuleModelVO = strategyRepository.queryStrategyAwardRuleModel(strategyId, awardId);
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionCenterEntity = doCheckRaffleCenterLogic(
            RaffleFactorEntity
                .builder()
                .userId(userId)
                .awardId(awardId)
                .strategyId(strategyId)
                .build(), strategyRuleModelVO.raffleCenterRuleModelList());

        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionCenterEntity.getCode())) {
            return RaffleAwardEntity.builder()
                .awardDesc("通过中奖但是走兜底流程；")
                .build();
        }
        return RaffleAwardEntity.builder()
            .awardId(awardId)
            .build();
    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(
        RaffleFactorEntity raffleFactorEntity, String... logics);

    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(
        RaffleFactorEntity raffleFactorEntity, String... logics);
//
//    protected abstract RuleActionEntity<RuleActionEntity.RaffleAfterEntity> doCheckRaffleAfterLogic(
//        RaffleFactorEntity raffleFactorEntity, String... logics);

}



