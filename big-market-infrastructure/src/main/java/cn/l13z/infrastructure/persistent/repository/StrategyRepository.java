package cn.l13z.infrastructure.persistent.repository;

import cn.l13z.domain.strategy.model.entity.StrategyAwardEntity;
import cn.l13z.domain.strategy.repository.IStrategyRepository;
import cn.l13z.infrastructure.persistent.dao.IStrategyAwardDao;
import cn.l13z.infrastructure.persistent.po.StrategyAward;
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
@SuppressWarnings("unused")
@Repository
public class StrategyRepository implements IStrategyRepository {

    private static final Logger log = LoggerFactory.getLogger(StrategyRepository.class);
    @Resource
    private IStrategyAwardDao strategyAwardDao;
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

//        redisService.setValue(cacheKey, strategyAwardEntities);
        if (strategyAwardEntities.isEmpty()) {
            return null;
        }
        return strategyAwardEntities;
    }

    @Override
    public void storeStrategyAwardSearchRate(Long strategyId, Integer rateRange,
        Map<Integer, Integer> strategyAwardSearchRateTable) {
        //  1.存储抽奖此策略的范围值
        redisService.setValue(Constants.RedisKeys.STRATEGY_RATE_RANGE_KEY + strategyId, rateRange);

        // 2.存储抽奖此策略的搜索率
        Map<Integer, Integer> cacheRateTable = redisService.getMap(
            Constants.RedisKeys.STRATEGY_RATE_TABLE_KEY + strategyId);
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
}
