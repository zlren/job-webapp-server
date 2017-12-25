package lab.zlren.project.job.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lab.zlren.project.job.common.bean.Identity;
import lab.zlren.project.job.entity.User;
import lab.zlren.project.job.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zlren
 * @date 2017-12-20
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private TokenService tokenService;

    public Identity generateToken(User user) {
        Identity identity = new Identity();
        BeanUtils.copyProperties(user, identity);
        identity.setToken(tokenService.createToken(identity));
        return identity;
    }
}
