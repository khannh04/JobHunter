    package vn.hoidanit.jobhunter.domain.response.resume;

    import jakarta.persistence.EnumType;
    import jakarta.persistence.Enumerated;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import vn.hoidanit.jobhunter.util.constant.ResumeStateEnum;

    import java.time.Instant;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class ResFetchResumeDTO {
        private long id;
        private String email;
        private String url;

        @Enumerated(EnumType.STRING)
        private ResumeStateEnum status;

        private String createdBy;
        private String updatedBy;
        private Instant createdAt;
        private Instant updatedAt;

        private String companyName;
        private UserResume user;
        private JobResume job;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class UserResume{
            private long id;
            private String name;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class JobResume{
            private long id;
            private String name;
        }
    }
