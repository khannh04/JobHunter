package vn.hoidanit.jobhunter.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {
    private int statusCode;
    private String error;

    //message maybe is String or ArrayList
    private Object message;
    private T data;

}
