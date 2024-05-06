package cn.l13z.infrastructure.persistent.dao;

import cn.l13z.infrastructure.persistent.po.Award;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName:     IAwardDao.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * <p>
 * Created:  2024-05-04 08:03 <br> Description: AwardDao <br>
 * <p>
 * Modification History: <br> - 2024/5/4 AlfredOrlando AwardDao <br>
 */
@Mapper
public interface IAwardDao {

    List<Award> queryAwardList();
}
