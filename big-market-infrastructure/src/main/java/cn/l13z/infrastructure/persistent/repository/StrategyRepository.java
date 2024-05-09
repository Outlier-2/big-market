package cn.l13z.infrastructure.persistent.repository;

import cn.l13z.domain.strategy.model.entity.StrategyAwardEntity;
import cn.l13z.domain.strategy.model.entity.StrategyEntity;
import cn.l13z.domain.strategy.model.entity.StrategyRuleEntity;
import cn.l13z.domain.strategy.repository.IStrategyRepository;
import cn.l13z.infrastructure.persistent.dao.IStrategyAwardDao;
import cn.l13z.infrastructure.persistent.dao.IStrategyDao;
import cn.l13z.infrastructure.persistent.dao.IStrategyRuleDao;
import cn.l13z.infrastructure.persistent.po.Strategy;
import cn.l13z.infrastructure.persistent.po.StrategyAward;
import cn.l13z.infrastructure.persistent.po.StrategyRule;
import cn.l13z.infrastructure.persistent.redis.IRedissonService;
import cn.l13z.types.common.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * ClassName: StrategyRepository.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-04-16 20:38 <br> Description: 策略存储实现类 <br>
 * <p>
 * Modification History: <br> - 2024/4/16 AlfredOrlando 策略存储实现类 <br>
 */
@Repository
public class StrategyRepository implements IStrategyRepository {

    private static final Logger log = LoggerFactory.getLogger(StrategyRepository.class);
    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyRuleDao strategyRuleDao;

    @Resource
    private IRedissonService redisService;

    @Override
    public List<StrategyAwardEntity> queryStrategyAward(Long strategyId) {
        // 优先查询缓存
        String cacheKey = Constants.RedisKeys.STRATEGY_AWARD_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cacheKey);
        if (strategyAwardEntities != null && !strategyAwardEntities.isEmpty()) {
            return strategyAwardEntities;
        }

        // 查数据库数据
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardList();
        strategyAwardEntities = new ArrayList<>(strategyAwards.size());
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity = StrategyAwardEntity.builder()
                .strategyId(strategyAward.getStrategyId())
                .awardId(strategyAward.getAwardId())
                .awardCount(strategyAward.getAwardCount())
                .awardCountSurplus(strategyAward.getAwardCountSurplus())
                .awardRate(strategyAward.getAwardRate())
                .build();
            strategyAwardEntities.add(strategyAwardEntity);
        }

        redisService.setValue(cacheKey, strategyAwardEntities);
        if (strategyAwardEntities.isEmpty()) {
            return null;
        }
        return strategyAwardEntities;
    }

    @Override
    public void storeStrategyAwardSearchRate(String key, Integer rateRange,
        Map<Integer, Integer> strategyAwardSearchRateTable) {
        //  1.存储抽奖此策略的范围值
        redisService.setValue(Constants.RedisKeys.STRATEGY_RATE_RANGE_KEY + key, rateRange);

        // 2.存储抽奖此策略的搜索率
        Map<Integer, Integer> cacheRateTable = redisService.getMap(
            Constants.RedisKeys.STRATEGY_RATE_TABLE_KEY + key);
        cacheRateTable.putAll(strategyAwardSearchRateTable);
    }

    @Override
    public Integer getStrategyAwardAssemble(Long strategyId, Integer rateKey) {
        log.info("getStrategyAwardAssemble key:{}:", Constants.RedisKeys.STRATEGY_RATE_TABLE_KEY + strategyId);
        return redisService.getFromMap(Constants.RedisKeys.STRATEGY_RATE_TABLE_KEY + strategyId,
            rateKey);
    }

    @Override
    public int getRateRange(Long strategyId) {
        log.info("getRateRange key:{}:", Constants.RedisKeys.STRATEGY_RATE_RANGE_KEY + strategyId);
        Integer cacheKey = redisService.getValue(Constants.RedisKeys.STRATEGY_RATE_RANGE_KEY + strategyId);
        log.info("getRateRange value:{}:", cacheKey);
        return redisService.getValue(Constants.RedisKeys.STRATEGY_RATE_RANGE_KEY + strategyId);
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        String cacheKey = Constants.RedisKeys.STRATEGY_KEY + strategyId;
        log.info("queryStrategyEntityByStrategyId key:{}:", cacheKey);
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        log.info("queryStrategyEntityByStrategyId value:{}:", strategyEntity);
        if (strategyEntity != null) {
            return strategyEntity;
        }
        Strategy strategy = strategyDao.queryStrategyByStrategyId(strategyId);
        log.info("strategy is :{}:", strategy);
        strategyEntity = StrategyEntity.builder()
            .strategyId(strategy.getStrategyId())
            .strategyDesc(strategy.getStrategyDesc())
            .ruleModels(strategy.getRuleModels())
            .build();
        redisService.setValue(cacheKey, strategyEntity);
        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel) {
        StrategyRule strategyRuleReq = new StrategyRule();
        strategyRuleReq.setStrategyId(strategyId);
        strategyRuleReq.setRuleModel(ruleModel);
        StrategyRule strategyRules = strategyRuleDao.queryStrategyRule(strategyRuleReq);
        return null;
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel) {
        StrategyRule strategyRule = new StrategyRule();
        strategyRule.setStrategyId(strategyId);
        strategyRule.setAwardId(awardId);
        strategyRule.setRuleModel(ruleModel);

        String queryStrategyRuleValueStrategyValue = strategyRuleDao.queryStrategyRuleValue(strategyRule);
        log.info("queryStrategyRuleValueStrategyValue: {}", queryStrategyRuleValueStrategyValue);
        return queryStrategyRuleValueStrategyValue;
    }
}
