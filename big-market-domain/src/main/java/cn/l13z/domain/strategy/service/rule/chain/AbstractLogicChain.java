package cn.l13z.domain.strategy.service.rule.chain;

/**
 * ClassName: AbstractLogicChain.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-09 20:27 <br> Description: 抽象逻辑链 <br>
 * <p>
 * Modification History: <br> - 2024/5/9 AlfredOrlando 抽象逻辑链 <br>
 */
public abstract class AbstractLogicChain implements ILogicChain {

    private ILogicChain next;

    @Override
    public ILogicChain appendNext(ILogicChain next) {
        this.next = next;
        return next;
    }

    @Override
    public ILogicChain next() {
        return next;
    }

    protected abstract String ruleModel();

}
