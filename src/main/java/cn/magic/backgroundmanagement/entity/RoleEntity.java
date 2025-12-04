package cn.magic.backgroundmanagement.entity;

import cn.magic.backgroundmanagement.entity.proxy.RoleEntityProxy;
import com.easy.query.core.annotation.LogicDelete;
import com.easy.query.core.basic.extension.logicdel.LogicDeleteStrategyEnum;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.annotation.EntityProxy;
import lombok.experimental.FieldNameConstants;

/**
 * 角色表 实体类。
 *
 * @author easy-query-plugin automatic generation
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Table(value = "role")
@EntityProxy
public class RoleEntity implements ProxyEntityAvailable<RoleEntity , RoleEntityProxy> {

    /**
     * 角色ID
     */
    @Column(primaryKey = true, value = "id")
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 授权的权限菜单列表
     */
    private String perms;

    /**
     * 角色备注
     */
    private String remarks;

    /**
     * 删除状态，0未删除
     */
    @LogicDelete(strategy = LogicDeleteStrategyEnum.DELETE_LONG_TIMESTAMP)
    private Long deleted;


}
