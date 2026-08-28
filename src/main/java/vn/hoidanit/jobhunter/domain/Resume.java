package vn.hoidanit.jobhunter.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.constant.ResumeStateEnum;

import java.time.Instant;

@Entity
@Table(name = "resumes")
@Getter
@Setter
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Email cannot blank")
    private String email;

    @NotBlank(message = "URL cannot blank")
    private String url;

    @Enumerated(EnumType.STRING)
    private ResumeStateEnum status;

    private String createdBy;
    private String updatedBy;
    private Instant createdAt;
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void handleBeforeCreate(){
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate(){
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get() : "";
        this.updatedAt = Instant.now();
    }
}
