package cn.magic.backgroundmanagement.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.annotation.EntityProxy;
import lombok.experimental.FieldNameConstants;

/**
 * 操作日志表 实体类。
 *
 * @author easy-query-plugin automatic generation
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Table(value = "actionlog")
@EntityProxy
public class ActionlogEntity {

    /**
     * 日志ID
     */
    @Column(primaryKey = true, value = "id")
    private Integer id;

    /**
     * 关联用户ID
     */
    private Integer userId;

    /**
     * 操作IP信息
     */
    private String ip;

    /**
     * 操作IP地址
     */
    private String address;

    /**
     * 操作时间戳
     */
    private Long timestamp;

    /**
     * 日志标题
     */
    private String title;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 响应结果
     */
    private String responseResult;


}
