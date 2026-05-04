package vn.hoidanit.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

import java.time.Instant;

@Setter
@Getter
public class ResCreateUserDTO {
    private long id;

    private String name;

    private int age;

    private String email;

    private GenderEnum gender;

    private String address;

    private Instant createAt;

}
