package cn.magic.backgroundmanagement.service;

import cn.magic.backgroundmanagement.entity.DeptEntity;
import cn.magic.backgroundmanagement.entity.proxy.DeptEntityProxy;
import cn.magic.backgroundmanagement.utils.R;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import com.easy.query.core.api.pagination.EasyPageResult;
import com.easy.query.solon.annotation.Db;
import org.noear.solon.annotation.Component;

import java.util.List;

@Component
public class DeptService {

    @Db
    private EasyEntityQuery easyEntityQuery;

    public R deptList(Integer currentPage, Integer pageSize, String name){
        EasyPageResult<DeptEntity> pageResult = easyEntityQuery.queryable(DeptEntity.class)
                .where(d -> d.deleted().eq(0))
                .where(d -> d.name().like(name))
                .toPageResult(currentPage, pageSize);
        return R.ok("获取部门列表成功！",pageResult);
    }

    public R addDept(String name){
        DeptEntity deptEntity = new DeptEntity();
        deptEntity.setDeleted(0);
        deptEntity.setId(null);
        deptEntity.setName(name);
        long l = easyEntityQuery.insertable(deptEntity).executeRows();
        if (l == 0) {
            return R.error("添加部门失败！");
        }
        return R.ok("添加部门成功！");
    }

    public R updateDept(Integer id, String name){
        DeptEntity deptEntity = new DeptEntity();
        deptEntity.setId(id);
        deptEntity.setName(name);
        deptEntity.setDeleted(0);
        long l = easyEntityQuery.updatable(deptEntity).where(d -> d.id().eq(id)).executeRows();
        if (l == 0) {
            return R.error("更新部门失败！");
        }
        return R.ok(name,"更新部门成功！");
    }

    public R deleteDept(Integer id){
        long l = easyEntityQuery.updatable(DeptEntity.class)
                .setColumns(o -> {
                    o.deleted().set(1);
                })
                .where(d -> d.id().eq(id)).executeRows();
        if (l == 0) {
            return R.error("删除部门失败！");
        }
        return R.ok("删除部门成功！");
    }
}
