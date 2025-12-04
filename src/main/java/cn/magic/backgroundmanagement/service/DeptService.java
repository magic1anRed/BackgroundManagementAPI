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
                .where(d -> d.name().like(name))
                .toPageResult(currentPage, pageSize);
        return R.ok("获取部门列表成功！",pageResult);
    }

    public R getDeptById(Integer id){
        DeptEntity deptEntity = easyEntityQuery.queryable(DeptEntity.class)
                .where(d -> d.id().eq(id))
                .firstNotNull();
        return R.ok("获取部门成功！",deptEntity);
    }

    public R getDeptName(){
        List<DeptEntity> deptEntities = easyEntityQuery.queryable(DeptEntity.class)
                .select(d ->d.FETCHER.id().name()).toList();
        return R.ok("获取部门名称成功！",deptEntities);
    }

    public R addDept(String name){
        DeptEntity deptEntity = new DeptEntity();
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
        long l = easyEntityQuery.updatable(deptEntity).where(d -> d.id().eq(id)).executeRows();
        if (l == 0) {
            return R.error("更新部门失败！");
        }
        return R.ok(name,"更新部门成功！");
    }

    public R deleteDept(Integer id){
        long l = easyEntityQuery.deletable(DeptEntity.class)
                .where(d -> d.id().eq(id))
                .executeRows();
        return l == 0 ? R.error("删除部门失败！") : R.ok("删除部门成功！");
    }
}
