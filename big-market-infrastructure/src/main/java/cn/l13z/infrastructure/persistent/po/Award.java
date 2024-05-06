package cn.l13z.infrastructure.persistent.po;

import java.util.Date;
import lombok.Data;

/**
 * ClassName: Award.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-04 07:19 <br> Description: 奖品PO <br>
 * <p>
 * Modification History: <br> - 2024/5/4 AlfredOrlando 奖品PO <br>
 */
@Data
public class Award {

    /**
     * 自增ID
     */
    private Long id;

    /**
     * 奖品ID - 内部流转使用
     */
    private Integer awardId;

    /**
     * 奖品对接标识 - 每一个都对应一个发奖的策略
     */
    private String awardKey;

    /**
     * 奖品配置信息
     */
    private String awardConfig;

    /**
     * 奖品描述
     */
    private String awardDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
