package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;
    private final UserService userService;
    private final FilterSpecificationConverter filterSpecificationConverter;
    private final FilterBuilder filterBuilder;

    public ResumeController(ResumeService resumeService, UserService userService,
                            FilterSpecificationConverter filterSpecificationConverter, FilterBuilder filterBuilder){
        this.resumeService = resumeService;
        this.userService = userService;
        this.filterSpecificationConverter = filterSpecificationConverter;
        this.filterBuilder = filterBuilder;
    }

    @PostMapping("resumes")
    @ApiMessage("Create resume")
    public ResponseEntity<ResCreateResumeDTO> create(@Valid @RequestBody Resume resume) throws IdInvalidException {
        // check user and job
        boolean isIdExist = this.resumeService.checkResumeExistByUserAndJob(resume);
        if (!isIdExist){
            throw new IdInvalidException("User/Job not found");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.create(resume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update resume")
    public ResponseEntity<ResUpdateResumeDTO> update(@RequestBody Resume resume) throws IdInvalidException {
        // check id
        Optional<Resume> currentResume = this.resumeService.fetchById(resume.getId());
        if (currentResume.isEmpty()){
            throw new IdInvalidException("Resume is not exist");
        }

        Resume updateRes = currentResume.get();
        updateRes.setStatus(resume.getStatus());

        return ResponseEntity.ok().body(this.resumeService.update(updateRes));
    }

    @DeleteMapping("resumes/{id}")
    @ApiMessage("Delete resume")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        // check id
        Optional<Resume> resume = this.resumeService.fetchById(id);
        if (resume.isEmpty()){
            throw new IdInvalidException("Resume is not exist");
        }
        this.resumeService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Fetch resume with id")
    public ResponseEntity<ResFetchResumeDTO> getById(@PathVariable("id") long id) throws IdInvalidException {
        // check id
        Optional<Resume> resumeOptional = this.resumeService.fetchById(id);
        if (resumeOptional.isEmpty()){
            throw new IdInvalidException("resume is not exist");
        }

        return ResponseEntity.ok().body(this.resumeService.getResume(resumeOptional.get()));
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resume")
    public ResponseEntity<ResultPaginationDTO> getAll(
            @Filter Specification<Resume> spec,
            Pageable pageable){
        List<Long> arrJobIds = null;
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUser = this.userService.handleUserByUsername(email);
        if (currentUser != null){
            Company userCompany = currentUser.getCompany();
            if (userCompany != null){
                List<Job> companyJobs = userCompany.getJobs();
                if (companyJobs != null && !companyJobs.isEmpty()){
                    arrJobIds = companyJobs.stream().map(x -> x.getId())
                            .collect(Collectors.toList());
                }
            }
        }
        Specification<Resume> jobInSpec = filterSpecificationConverter.convert(filterBuilder.field("job").in(filterBuilder.input(arrJobIds)).get());
        Specification<Resume> finalSpec = jobInSpec.and(spec);
        return ResponseEntity.ok().body(this.resumeService.fetchAll(finalSpec, pageable));
    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable){
        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(pageable));
    }
}
