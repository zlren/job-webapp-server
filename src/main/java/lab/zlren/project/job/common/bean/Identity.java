package lab.zlren.project.job.common.bean;

import com.fasterxml.jackson.annotation.JsonView;
import lab.zlren.project.job.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author zlren
 * @date 2017-12-25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Identity extends User {

    @JsonView(User.VoView.class)
    private Long duration;

    @JsonView(User.VoView.class)
    private String token;
}
