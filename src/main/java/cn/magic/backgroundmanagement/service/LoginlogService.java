package cn.magic.backgroundmanagement.service;

import cn.magic.backgroundmanagement.entity.LoginlogEntity;
import cn.magic.backgroundmanagement.utils.R;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import com.easy.query.core.api.pagination.EasyPageResult;
import com.easy.query.solon.annotation.Db;
import org.noear.solon.annotation.Component;

import javax.xml.stream.events.StartDocument;

@Component
public class LoginlogService {

    @Db
    private EasyEntityQuery easyEntityQuery;

    public R list(String address,Long startTime,Long endTime,Integer currentPage,Integer pageSize){
        EasyPageResult<LoginlogEntity> pageResult = easyEntityQuery.queryable(LoginlogEntity.class)
                .where(u -> {
                    u.address().like(address);
                    u.timestamp().gt(startTime);
                    u.timestamp().lt(endTime);
                }).toPageResult(currentPage, pageSize);
        return R.ok("查询登录日志成功",pageResult);
    }

}
