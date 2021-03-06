package lab.zlren.project.job.service;

import lab.zlren.project.job.common.response.CommonResponse;
import org.springframework.stereotype.Service;

/**
 * @author zlren
 * @date 2017-12-20
 */
@Service
public class ResponseService {

    public CommonResponse success() {
        return new CommonResponse(0, null, null);
    }

    public CommonResponse success(String msg) {
        return new CommonResponse(0, msg, null);
    }

    public CommonResponse success(Object data) {
        return success("", data);
    }

    public CommonResponse success(String msg, Object data) {
        return new CommonResponse(0, msg, data);
    }

    public CommonResponse failure(String msg) {
        return new CommonResponse(1, msg, null);
    }

    public CommonResponse failure(Integer code, String msg) {
        assert code != 0;
        return new CommonResponse(code, msg, null);
    }
}
