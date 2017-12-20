package lab.zlren.project.job.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author zlren
 * @since 2017-12-20
 */
@Data
@Accessors(chain = true)
public class User implements Serializable {

    public interface VoView {
    }

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @JsonView(User.VoView.class)
    private Integer id;

    /**
     * 用户名
     */
    @JsonView(User.VoView.class)
    private String user;

    /**
     * 密码
     * 没有设置 @JsonView(View.UserVO.class)
     */
    private String pwd;

    /**
     * 角色（老板、求职者）
     */
    @JsonView(User.VoView.class)
    private String type;

    /**
     * 头像
     */
    @JsonView(User.VoView.class)
    private String avatar;

    /**
     * 自我介绍
     */
    @JsonView(User.VoView.class)
    private String desc;

    @JsonView(User.VoView.class)
    private String title;

    /**
     * 公司（老板）
     */
    @JsonView(User.VoView.class)
    private String company;

    /**
     * 薪水
     */
    @JsonView(User.VoView.class)
    private Float money;
}
