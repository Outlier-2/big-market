package cn.l13z.domain.strategy.service.rule.impl;

import cn.l13z.domain.strategy.model.entity.RuleActionEntity;
import cn.l13z.domain.strategy.model.entity.RuleActionEntity.RaffleBeforeEntity;
import cn.l13z.domain.strategy.model.entity.RuleMatterEntity;
import cn.l13z.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import cn.l13z.domain.strategy.repository.IStrategyRepository;
import cn.l13z.domain.strategy.service.annotation.LogicStrategy;
import cn.l13z.domain.strategy.service.rule.ILogicFilter;
import cn.l13z.domain.strategy.service.rule.factory.DefaultLogicFactory;
import cn.l13z.types.common.Constants;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ClassName: RuleWeightLogicFilter.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-06 22:26 <br> Description: 权重规则过滤 <br>
 * <p>
 * Modification History: <br> - 2024/5/6 AlfredOrlando 权重规则过滤 <br>
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_WIGHT)
public class RuleWeightLogicFilter implements ILogicFilter<RaffleBeforeEntity> {

    @Resource
    private IStrategyRepository repository;

    public Long userScore = 4500L;
    @Override
    public RuleActionEntity<RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-WeightLogicFactory userId:{} strategyId:{} ruleModel:{}", ruleMatterEntity.getUserId(),
            ruleMatterEntity.getStrategyId(), ruleMatterEntity.getRuleModel());

        String userId = ruleMatterEntity.getUserId();

        log.info("用户ID:{} 策略ID:{} 规则模型:{}", userId, ruleMatterEntity.getStrategyId(),ruleMatterEntity.getRuleModel());

        // 查询规则值配置
        String ruleValue = repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(),
            ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(splitRuleValue[0]);

        log.info("用户ID:{} 策略ID:{} 规则模型:{} 规则值:{}", userId, ruleMatterEntity.getStrategyId(),ruleMatterEntity.getRuleModel(),ruleValue);

        // 过滤其他规则
        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);
        for (String userBlackId : userBlackIds) {
            if (userId.equals(userBlackId)) {
                return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
                    .data(RuleActionEntity.RaffleBeforeEntity.builder()
                        .strategyId(ruleMatterEntity.getStrategyId())
                        .awardId(awardId)
                        .build())
                    .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                    .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                    .build();
            }
        }

        log.info("userBlackIds : {}", (Object) userBlackIds);
        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
            .code(RuleLogicCheckTypeVO.ALLOW.getCode())
            .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
            .build();
    }
}

