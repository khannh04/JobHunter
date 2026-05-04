package vn.hoidanit.jobhunter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
    private long id;

    private String email;

    private String name;

    private GenderEnum gender;

    private  int age;

    private String address;

    private Instant createdAt;

    private  Instant updatedAt;

}
