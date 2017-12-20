package lab.zlren.project.job.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lab.zlren.project.job.entity.User;
import lab.zlren.project.job.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * @author zlren
 * @date 2017-12-20
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

}
