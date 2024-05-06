package cn.l13z.test.infrastructure;

import cn.l13z.infrastructure.persistent.dao.IAwardDao;
import cn.l13z.infrastructure.persistent.po.Award;
import com.alibaba.fastjson2.JSON;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ClassName: AwardDaoTest.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-04 08:40 <br> Description: 测试奖品类是否正常 <br>
 * <p>
 * Modification History: <br> - 2024/5/4 AlfredOrlando 测试奖品类是否正常 <br>
 */

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class AwardDaoTest {

    @Resource
    private IAwardDao awardDao;

    @Test
    public void test_queryAwardList() {
        List<Award> awards = awardDao.queryAwardList();
        log.info("测试结果：{}", JSON.toJSONString(awards));
    }

}

