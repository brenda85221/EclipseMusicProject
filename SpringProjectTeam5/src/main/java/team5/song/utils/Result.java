package team5.song.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;
    public static <T> Result<T> buildResult(Integer code, String msg, T body) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(body);
        return result;
    }
}
