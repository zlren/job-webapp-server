package lab.zlren.project.job.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.annotation.JsonView;
import lab.zlren.project.job.common.response.CommonResponse;
import lab.zlren.project.job.entity.User;
import lab.zlren.project.job.service.ResponseService;
import lab.zlren.project.job.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zlren
 * @date 2017-12-20
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseService responseService;

    /**
     * 用户注册
     *
     * @return 注册后的用户信息
     */
    @PostMapping("register")
    @JsonView(User.VoView.class)
    public CommonResponse register(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {

        // 注册的时候提交的数据包括user、pwd和type
        List<User> userList = this.userService.selectList(new EntityWrapper<>(new User().setUser(user.getUser())));

        if (userList.size() > 0) {
            // 用户名重复
            log.info("用户名重复：{}", user.getUser());
            return responseService.failure("用户名已存在");
        }

        // 插入完了以后user会自动填充id域
        boolean insert = this.userService.insert(user);

        if (insert) {

            // 注册完了设置cookie
            this.setCookie(request, response, "userid", String.valueOf(user.getId()));

            return responseService.success("注册成功", user);
        } else {
            return responseService.failure("服务器出错了");
        }
    }

    /**
     * 登录
     *
     * @param user     请求参数
     * @param request  req
     * @param response res
     * @return 用户信息
     */
    @PostMapping("login")
    @JsonView(User.VoView.class)
    public CommonResponse login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {

        // 登录的参数只有user和pwd
        User oneByParams = this.userService.selectOne(new EntityWrapper<>(user));

        if (oneByParams == null) {
            return responseService.failure("用户名或密码错误");
        }

        // 登录完了设置cookie
        setCookie(request, response, "userid", String.valueOf(oneByParams.getId()));

        return responseService.success(oneByParams);
    }

    /**
     * 查询当前用户信息
     *
     * @param userid cookie
     * @return 当前用户
     */
    @GetMapping("info")
    @JsonView(User.VoView.class)
    public CommonResponse userInfo(@CookieValue(required = false) String userid) {

        log.info("cookie的值是：{}", userid);

        if (userid != null && userid.length() > 0) {
            User user = this.userService.selectById(Integer.valueOf(userid));
            log.info("已经登录的用户：{}", user);
            return responseService.success("已登录的用户", user);
        }

        return responseService.failure("没有登录信息");
    }

    /**
     * 查询所有用户列表
     *
     * @return 用户列表
     */
    @GetMapping("list")
    @JsonView(User.VoView.class)
    public List<User> userList() {
        return this.userService.selectList(null);
    }


    /**
     * 设置cookie
     * 如果原来有就更新，没有就新增
     *
     * @param request  req
     * @param response res
     * @param key      "userid"
     * @param value    userid
     */
    private void setCookie(HttpServletRequest request, HttpServletResponse response, String key, String value) {

        Cookie[] cookies = request.getCookies();
        boolean findFlag = false;

        for (Cookie cookie : cookies) {
            // 更新的操作
            if (key.equals(cookie.getName())) {
                findFlag = true;
                cookie.setValue(String.valueOf(value));
                cookie.setPath("/");
                cookie.setMaxAge(30 * 60);
                response.addCookie(cookie);
                break;
            }
        }

        if (!findFlag) {
            // 设置cookie
            Cookie newCookie = new Cookie(key, value);
            newCookie.setPath("/");
            newCookie.setMaxAge(30 * 60);
            response.addCookie(newCookie);
        }
    }

}
