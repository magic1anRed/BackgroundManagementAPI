package cn.magic.backgroundmanagement.entity;

import cn.magic.backgroundmanagement.entity.proxy.PermsEntityProxy;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.annotation.EntityProxy;
import lombok.experimental.FieldNameConstants;

/**
 * 权限表 实体类。
 *
 * @author easy-query-plugin automatic generation
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Table(value = "perms")
@EntityProxy
public class PermsEntity implements ProxyEntityAvailable<PermsEntity , PermsEntityProxy> {

    /**
     * 权限ID
     */
    @Column(primaryKey = true, value = "id")
    private Integer id;

    /**
     * 父级权限ID
     */
    private Integer parentId;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限标识
     */
    private String identifier;

    /**
     * 权限路由路径
     */
    private String path;

    /**
     * 权限路由组件
     */
    private String component;

    /**
     * 权限类型，0目录/1菜单/2操作
     */
    private Integer type;

    /**
     * 排序ID
     */
    private Integer sortId;

    /**
     * 权限状态，1显示/0隐藏
     */
    private Integer status;

    /**
     * 删除状态，0未删除
     */
    private Long deleted;


}
