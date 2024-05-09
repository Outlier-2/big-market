package cn.l13z.domain.strategy.service.rule.impl;

import cn.l13z.domain.strategy.model.entity.RuleActionEntity;
import cn.l13z.domain.strategy.model.entity.RuleActionEntity.RaffleBeforeEntity;
import cn.l13z.domain.strategy.model.entity.RuleMatterEntity;
import cn.l13z.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import cn.l13z.domain.strategy.repository.IStrategyRepository;
import cn.l13z.domain.strategy.service.annotation.LogicStrategy;
import cn.l13z.domain.strategy.service.rule.ILogicFilter;
import cn.l13z.domain.strategy.service.rule.factory.DefaultLogicFactory;
import cn.l13z.domain.strategy.service.rule.factory.DefaultLogicFactory.LogicModel;
import cn.l13z.types.common.Constants;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ClassName: RuleBackListLogicFilter.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-06 22:06 <br> Description: 黑名单规则过滤 <br>
 * <p>
 * Modification History: <br> - 2024/5/6 AlfredOrlando 黑名单规则过滤 <br>
 */
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_BLACKLIST)
public class RuleBackListLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {

    private static final Logger log = LoggerFactory.getLogger(RuleBackListLogicFilter.class);
    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public RuleActionEntity<RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-黑名单规则过滤");

        String userId = ruleMatterEntity.getUserId();
        log.info("userId:{} ruleMatterEntity:{}", userId, ruleMatterEntity);

        // 查询规则值的配置
        String ruleValue = strategyRepository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(),
            ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        log.info("queryRuleValue:{}", ruleValue);
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(splitRuleValue[0]);

        log.info("ruleValue:{}, splitRuleValue:{}, awardId:{}", ruleValue, splitRuleValue, awardId);

        // 过滤其他规则
        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);
        for (String userBlackId : userBlackIds) {

            log.info("userBlackId:{}", userBlackId);
            if (userId.equals(userBlackId)) {
                RuleActionEntity<RaffleBeforeEntity>  RuleBack = RuleActionEntity.<RaffleBeforeEntity>builder()
                    .ruleModel(LogicModel.RULE_BLACKLIST.getCode())
                    .data(RaffleBeforeEntity.builder()
                        .strategyId(ruleMatterEntity.getStrategyId())
                        .awardId(awardId)
                        .build())
                    .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                    .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                    .build();
                log.info("RuleBack:{}", RuleBack);
                return RuleBack;
            }
        }

        RuleActionEntity<RaffleBeforeEntity> RuleBlack = RuleActionEntity.<RaffleBeforeEntity>builder()
            .code(RuleLogicCheckTypeVO.ALLOW.getCode())
            .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
            .build();
        return RuleBlack;
    }
}
