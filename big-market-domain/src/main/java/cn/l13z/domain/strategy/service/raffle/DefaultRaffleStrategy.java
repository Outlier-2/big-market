package cn.l13z.domain.strategy.service.raffle;

import cn.l13z.domain.strategy.model.entity.RaffleFactorEntity;
import cn.l13z.domain.strategy.model.entity.RuleActionEntity;
import cn.l13z.domain.strategy.model.entity.RuleActionEntity.RaffleBeforeEntity;
import cn.l13z.domain.strategy.model.entity.RuleActionEntity.RaffleCenterEntity;
import cn.l13z.domain.strategy.model.entity.RuleMatterEntity;
import cn.l13z.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import cn.l13z.domain.strategy.repository.IStrategyRepository;
import cn.l13z.domain.strategy.service.armory.IStrategyDispatch;
import cn.l13z.domain.strategy.service.rule.ILogicFilter;
import cn.l13z.domain.strategy.service.rule.factory.DefaultLogicFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * ClassName: DefaultRaffleStrategy.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-06 11:04 <br> Description: 默认抽奖策略 <br>
 * <p>
 * Modification History: <br> - 2024/5/6 AlfredOrlando 默认抽奖策略 <br>
 */
@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy {

    @Resource
    private DefaultLogicFactory logicFactory;

    public DefaultRaffleStrategy(IStrategyDispatch strategyDispatch, IStrategyRepository strategyRepository) {
        super(strategyDispatch, strategyRepository);
    }

    @Override
    protected RuleActionEntity<RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity,
        String... logics) {

        // 黑名单规则优先过滤
        String ruleBackList = Arrays.stream(logics)
            .filter(str -> str.contains(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())).findFirst()
            .orElse(null);

        Map<String, ILogicFilter<RaffleBeforeEntity>> logicFilterGroup = logicFactory.openLogicFilter();

        if (StringUtils.isNotBlank(ruleBackList)) {
            ILogicFilter<RaffleBeforeEntity> logicFilter = logicFilterGroup.get(
                DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());
            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
            ruleMatterEntity.setUserId(raffleFactorEntity.getUserId());
            ruleMatterEntity.setAwardId(ruleMatterEntity.getAwardId());
            ruleMatterEntity.setStrategyId(raffleFactorEntity.getStrategyId());
            ruleMatterEntity.setRuleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());
            RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = logicFilter.filter(
                ruleMatterEntity);
            if (!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) {
                return ruleActionEntity;
            }

        }
        // 顺序过滤剩余规则
        List<String> ruleList = Arrays.stream(logics)
            .filter(s -> !s.equals(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
            .collect(Collectors.toList());

        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = null;
        for (String ruleModel : ruleList) {
            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> logicFilter = logicFilterGroup.get(ruleModel);
            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
            ruleMatterEntity.setUserId(raffleFactorEntity.getUserId());
            ruleMatterEntity.setAwardId(ruleMatterEntity.getAwardId());
            ruleMatterEntity.setStrategyId(raffleFactorEntity.getStrategyId());
            ruleMatterEntity.setRuleModel(ruleModel);
            ruleActionEntity = logicFilter.filter(ruleMatterEntity);
            // 非放行结果则顺序过滤
            log.info("抽奖前规则过滤 userId: {} ruleModel: {} code: {} info: {}", raffleFactorEntity.getUserId(),
                ruleModel, ruleActionEntity.getCode(), ruleActionEntity.getInfo());
            if (!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) {
                return ruleActionEntity;
            }
        }

        return ruleActionEntity;
    }

    @Override
    protected RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(
        RaffleFactorEntity raffleFactorEntity,
        String... logics) {
        if (logics == null || logics.length == 0) {
            return RuleActionEntity.<RaffleCenterEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
        }
        Map<String, ILogicFilter<RuleActionEntity.RaffleCenterEntity>> logicFilterGroup = logicFactory.openLogicFilter();

        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionEntity = null;
        for (String ruleModel : logics) {
            ILogicFilter<RuleActionEntity.RaffleCenterEntity> logicFilter = logicFilterGroup.get(ruleModel);
            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
            ruleMatterEntity.setUserId(raffleFactorEntity.getUserId());
            ruleMatterEntity.setAwardId(ruleMatterEntity.getAwardId());
            ruleMatterEntity.setStrategyId(raffleFactorEntity.getStrategyId());
            ruleMatterEntity.setRuleModel(ruleModel);
            ruleActionEntity = logicFilter.filter(ruleMatterEntity);
            // 非放行结果则顺序过滤
            log.info("抽奖前规则过滤 userId: {} ruleModel: {} code: {} info: {}", raffleFactorEntity.getUserId(),
                ruleModel, ruleActionEntity.getCode(), ruleActionEntity.getInfo());
            if (!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) {
                return ruleActionEntity;
            }
        }

        return ruleActionEntity;

    }

//    @Override
//    protected RuleActionEntity<RaffleBeforeEntity> doCheckRaffleAfterLogic(RaffleFactorEntity raffleFactorEntity,
//        String... logics) {
//        return null;
//    }
}




