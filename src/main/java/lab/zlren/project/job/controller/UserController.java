package lab.zlren.project.job.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.annotation.JsonView;
import lab.zlren.project.job.common.bean.Identity;
import lab.zlren.project.job.common.response.CommonResponse;
import lab.zlren.project.job.entity.User;
import lab.zlren.project.job.service.ResponseService;
import lab.zlren.project.job.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
     * 注册
     *
     * @return 注册后的用户信息
     */
    @PostMapping("register")
    @JsonView(User.VoView.class)
    public CommonResponse register(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {

        // 注册的时候提交的数据包括user、pwd和type
        List<User> userList = userService.selectList(new EntityWrapper<>(new User().setUser(user.getUser())));

        if (userList.size() > 0) {
            // 用户名重复
            log.info("用户名重复：{}", user.getUser());
            return responseService.failure("用户名已存在");
        }

        // 插入完了以后user会自动填充id域
        boolean insert = this.userService.insert(user);

        if (insert) {
            return responseService.success("注册成功", user);
        } else {
            return responseService.failure("服务器出错了");
        }
    }

    /**
     * 登录
     *
     * @param user 请求参数
     * @return 用户信息
     */
    @PostMapping("login")
    @JsonView(User.VoView.class)
    public CommonResponse login(@RequestBody User user) {

        log.info("请求登录，参数：{}, {}", user.getUser(), user.getPwd());

        // 登录的参数只有user和pwd
        User oneByParams = userService.selectOne(new EntityWrapper<>(user));

        if (oneByParams == null) {
            return responseService.failure("用户名或密码错误");
        }

        return responseService.success(userService.generateToken(oneByParams));
    }

    /**
     * 更新个人信息
     *
     * @param userid userid
     * @param user   包含的域有avatar、company、desc、money、title
     * @return 更新后的结果
     */
    @PostMapping("update")
    @JsonView(User.VoView.class)
    public CommonResponse update(@CookieValue(required = false) String userid, @RequestBody User user) {

        if (userid == null || userid.length() <= 0) {
            return responseService.failure("校验出错");
        }

        User userByCookie = userService.selectById(Integer.valueOf(userid));

        if (userByCookie == null) {
            return responseService.failure("校验出错");
        }

        userByCookie
                .setAvatar(user.getAvatar())
                .setCompany(user.getCompany())
                .setDesc(user.getDesc())
                .setMoney(user.getMoney())
                .setTitle(user.getTitle());

        boolean b = userService.updateById(userByCookie);

        if (b) {
            return responseService.success(userByCookie);
        } else {
            return responseService.failure("后端出错");
        }
    }


    /**
     * 查询当前用户信息
     *
     * @param httpSession
     * @return
     */
    @GetMapping("info")
    @JsonView(User.VoView.class)
    public CommonResponse userInfo(HttpSession httpSession) {

        Identity identity = (Identity) httpSession.getAttribute("IDENTITY");

        User user = userService.selectById(identity.getId());
        log.info("已经登录的用户：{}", user);
        return responseService.success("已登录的用户", user);
    }

    /**
     * 查询所有用户列表
     *
     * @return 用户列表
     */
    @GetMapping("list")
    @JsonView(User.VoView.class)
    public CommonResponse userList(@RequestParam String type, HttpSession httpSession) {
        log.info("查询列表：{}", type);
        return responseService.success(userService.selectList(new EntityWrapper<>(new User().setType(type))));
    }


    /**
     * 未验证跳转
     *
     * @return
     */
    @RequestMapping(value = "/login_denied")
    public CommonResponse loginDenied() {
        return responseService.failure("请先登录");
    }
}
