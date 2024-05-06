package cn.l13z.infrastructure.persistent.dao;

import cn.l13z.infrastructure.persistent.po.Strategy;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName:     IStrategyDao.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * <p>
 * Created:  2024-05-04 08:04 <br> Description: StrategyDao <br>
 * <p>
 * Modification History: <br> - 2024/5/4 AlfredOrlando StrategyDao <br>
 */
@Mapper
public interface IStrategyDao {

    List<Strategy> queryStrategyList();
}
