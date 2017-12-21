package lab.zlren.project.job.common.response;

import com.fasterxml.jackson.annotation.JsonView;
import lab.zlren.project.job.entity.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zlren
 * @date 2017-12-20
 */
@Data
@Slf4j
public class CommonResponse {

    @JsonView(User.VoView.class)
    private Integer code;

    @JsonView(User.VoView.class)
    private String msg;

    @JsonView(User.VoView.class)
    private Object data;

    public CommonResponse(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;

        log.info("{}", this);
    }
}
