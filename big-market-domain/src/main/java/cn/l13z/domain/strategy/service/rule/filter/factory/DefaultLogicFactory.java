package cn.l13z.domain.strategy.service.rule.filter.factory;

import cn.l13z.domain.strategy.model.entity.RuleActionEntity;
import cn.l13z.domain.strategy.service.annotation.LogicStrategy;
import cn.l13z.domain.strategy.service.rule.filter.ILogicFilter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

/**
 * ClassName: DefaultLogicFactory.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-06 21:54 <br> Description: 默认逻辑工厂  <br>
 * <p>
 * Modification History: <br> - 2024/5/6 AlfredOrlando  <br>
 */
@Service
public class DefaultLogicFactory {

    public Map<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();

    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilterList) {

        logicFilterList.forEach(logic -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class);
            if (strategy != null) {
                logicFilterMap.put(strategy.logicMode().getCode(), logic);
            }
        });

    }

    public <T extends RuleActionEntity.RaffleEntity> Map<String, ILogicFilter<T>> openLogicFilter() {
        return (Map<String, ILogicFilter<T>>) (Map<?, ?>) logicFilterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_WIGHT("rule_weight", "【抽奖前规则】根据抽奖权重返回可抽奖范围KEY", "before"),
        RULE_BLACKLIST("rule_blacklist", "【抽奖前规则】黑名单规则过滤，命中黑名单则直接返回", "before"),
        RULE_LOCK("rule_lock", "【抽奖过程中规则】抽奖n次解锁", "center"),
        RULE_LUCK_AWARD("rule_lock_award", "【抽奖后规则】幸运奖励", "after"),
        ;

        private final String code;
        private final String info;
        private final String type;

        public static boolean isCenter(String code) {
            return "center".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }

        public static boolean isAfter(String code) {
            return "center".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }

    }

}
