package cn.l13z.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: RuleLogicCheckTypeVO.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-06 21:38 <br> Description: 规则逻辑校验类型值对象 <br>
 * <p>
 * Modification History: <br> - 2024/5/6 AlfredOrlando 规则逻辑校验类型值对象 <br>
 */
@Getter
@AllArgsConstructor
public enum RuleLogicCheckTypeVO {

    ALLOW("0000", "放行执行, 执行后续流程，不受规则引擎的影响"),
    TAKE_OVER("0001", "接管执行, 执行后续流程，并将规则引擎的影响转移给接管的规则引擎"),
    ;

    private final String code;
    private final String info;

}
