package lab.zlren.project.job.config.interceptor;

import lab.zlren.project.job.common.bean.Identity;
import lab.zlren.project.job.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zlren
 * @date 2017-12-25
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws
            Exception {

        // 验证token的有效性
        try {

            String token = request.getHeader("TOKEN");
            log.info("请求的url是：{}", request.getRequestURL());
            log.info("获得的token： {}", token);

            Identity identity = tokenService.parseToken(token);

            // 把identity存入session中(其中包含用户名、角色、过期时间戳等)
            request.getSession().setAttribute("IDENTITY", identity);

            log.info("{}: token有效", identity.getUser());

            return true;
        } catch (Exception e) {

            log.info("TOKEN无效，转到登录界面");
            response.sendRedirect("/user/login_denied");

            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                                Exception e) throws Exception {

    }
}
