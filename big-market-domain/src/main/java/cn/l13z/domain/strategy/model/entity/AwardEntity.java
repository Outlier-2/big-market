package cn.l13z.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: AwardEntity.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-04-16 19:51 <br> Description: 奖品实体 <br>
 * <p>
 * Modification History: <br> - 2024/4/16 AlfredOrlando 奖品实体 <br>
 */
@SuppressWarnings("unused")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwardEntity {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 奖品ID
     */
    private String awardId;
}
