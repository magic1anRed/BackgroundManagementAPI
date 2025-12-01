package cn.magic.backgroundmanagement.entity;

import cn.magic.backgroundmanagement.entity.proxy.DeptEntityProxy;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.annotation.EntityProxy;
import lombok.experimental.FieldNameConstants;

/**
 * 部门表 实体类。
 *
 * @author easy-query-plugin automatic generation
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Table(value = "dept")
@EntityProxy
public class DeptEntity implements ProxyEntityAvailable<DeptEntity , DeptEntityProxy> {

    /**
     * 部门ID
     */
    @Column(primaryKey = true, value = "id")
    private Integer id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 删除状态，0未删除
     */
    private Long deleted;


}
