package vn.hoidanit.jobhunter.controller;


import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService){
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create A Job")
    public ResponseEntity<ResCreateJobDTO> create(@Valid @RequestBody Job job){

        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.create(job));
    }

    @PutMapping("/jobs")
    @ApiMessage("Update A Job")
    public ResponseEntity<ResUpdateJobDTO> update(@Valid @RequestBody Job job) throws IdInvalidException {
        // check id
        Optional<Job> currentJob = this.jobService.fetchJobById(job.getId());
        if (currentJob.isEmpty()){
            throw new IdInvalidException("Job not found");
        }

        return ResponseEntity.ok().body(this.jobService.update(job, currentJob.get()));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete A Job")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchJobById(id);
        if (currentJob.isEmpty()){
            throw new IdInvalidException("Job not found");
        }
        this.jobService.delete(id);

        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/jobs")
    @ApiMessage("Get All Jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Job> spec,
                                                          Pageable pageable){
        return ResponseEntity.ok().body(this.jobService.fetchAllJobs(spec, pageable));
    }


}
