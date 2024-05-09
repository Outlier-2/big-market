package cn.l13z.domain.strategy.service;

import cn.l13z.domain.strategy.model.entity.RaffleFactorEntity;
import cn.l13z.domain.strategy.model.entity.RaffleAwardEntity;

/**
 * ClassName:     IRaffleStrategy.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * <p>
 * Created:  2024-05-06 11:07 <br> Description: 抽奖策略接口 <br>
 * <p>
 * Modification History: <br> - 2024/5/6 AlfredOrlando 抽奖策略接口 <br>
 */
public interface IRaffleStrategy {

    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);
}
