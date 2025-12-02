package cn.magic.backgroundmanagement.filters;

import cn.magic.backgroundmanagement.utils.R;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.exception.StatusException;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;

import javax.xml.bind.ValidationException;

@Component
public class ExceptionFilter implements Filter {
    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        try {
            chain.doFilter(ctx);
        }catch (ValidationException e){
            ctx.render(R.error(Integer.parseInt(e.getErrorCode()),e.getMessage()));
        } catch (StatusException e){
            ctx.render(R.error(e.getMessage())); //可能的状态码为：4xxx
        } catch (Throwable e) {
            ctx.render(R.error(500,e.getMessage()));
        }
    }
}
