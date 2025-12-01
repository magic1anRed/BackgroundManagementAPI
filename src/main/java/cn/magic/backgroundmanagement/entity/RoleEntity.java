package cn.magic.backgroundmanagement.entity;

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
public class RoleEntity {

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
    private Long deleted;


}
