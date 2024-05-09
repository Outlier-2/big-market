package cn.l13z.domain.strategy.service.armory;

import cn.l13z.domain.strategy.model.entity.StrategyAwardEntity;
import cn.l13z.domain.strategy.model.entity.StrategyEntity;
import cn.l13z.domain.strategy.model.entity.StrategyRuleEntity;
import cn.l13z.domain.strategy.repository.IStrategyRepository;
import cn.l13z.types.enums.ResponseCode;
import cn.l13z.types.exception.AppException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ClassName: StrategyArmory.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-04-16 21:13 <br> Description: 策略装配类 <br>
 * <p>
 * Modification History: <br> - 2024/4/16 AlfredOrlando 策略装配类 <br>
 */
@SuppressWarnings("unused")
@Service
@Slf4j
public class StrategyArmoryDispatch implements IStrategyArmory, IStrategyDispatch {

    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        // 1. 查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities = strategyRepository.queryStrategyAward(
            strategyId);

        if (strategyAwardEntities.isEmpty()) {
            log.info("策略ID：{}策略奖品为空，无法装配", strategyId);
            return false;
        }

        assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);

        // 2.权重策略装配 - 如果rule_weight 为空，不做权重策略装配
        StrategyEntity strategyEntity = strategyRepository.queryStrategyEntityByStrategyId(strategyId);
        String ruleWeightValue = strategyEntity.getRuleWeight();
        if (ruleWeightValue == null) {
            return true;
        }
        StrategyRuleEntity strategyRuleEntity = strategyRepository.queryStrategyRule(strategyId, ruleWeightValue);
        if (null == strategyRuleEntity) {
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }
        Map<String, List<Integer>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightMap();
        Set<String> keys = ruleWeightValueMap.keySet();
        for (String key : keys) {
            List<Integer> ruleWeightValues = ruleWeightValueMap.get(key);
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
            strategyAwardEntitiesClone.removeIf(entity -> !ruleWeightValues.contains(entity.getAwardId()));
            assembleLotteryStrategy(String.valueOf(strategyId).concat("_").concat(key), strategyAwardEntitiesClone);
        }
        return true;
    }

    private void assembleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities) {

        // 2. 获取最小概率
        BigDecimal minAwardRate = strategyAwardEntities.stream()
            .map(StrategyAwardEntity::getAwardRate)
            .min(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);

        // 3. 计算概率总和
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
            .map(StrategyAwardEntity::getAwardRate)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. 用 1% 0.0001 获取概率的百分位，千分位，万分位
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);
        // 会OC我们缩小范围
        rateRange = rateRange.divide(BigDecimal.valueOf(1000)).setScale(0, RoundingMode.CEILING);

        // 5. 生成策略奖品概率查询表 【这里指的是list中存放占位，占位越多盖里越高】
        List<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());

        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();
            Integer awardCount = strategyAwardEntity.getAwardCount();

            // 计算出每个概率值需要存放的的数量，循环填充
            for (int i = 0;
                i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue();
                i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }
        // 6. 进行乱序
        Collections.shuffle(strategyAwardSearchRateTables);

        // 7. 生成出Map集合通过概率来获得对应的奖品ID
        LinkedHashMap<Integer, Integer> shuffleStrategyAwardSearchRateTable = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffleStrategyAwardSearchRateTable.put(i, strategyAwardSearchRateTables.get(i));
        }

        // 8. 保存策略奖品概率查询表
        strategyRepository.storeStrategyAwardSearchRate(key,
            shuffleStrategyAwardSearchRateTable.size(), shuffleStrategyAwardSearchRateTable);

    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        log.info("策略ID：{}", strategyId);

        // 分布式下不一定是当前应用做策略装配, 所以这里直接从数据库（redis）中获取
        int rateRange = strategyRepository.getRateRange(strategyId);

        log.info("策略ID：{}的奖品 RandomAwardID：{}", strategyId, rateRange);

        // 生成设计者，来获取奖品查询表的结果
        return strategyRepository.getStrategyAwardAssemble(strategyId,
            new SecureRandom().nextInt(rateRange));

    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        return 0;
    }
}
