package vn.hoidanit.jobhunter.controller;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.job.ResCreateJobDTO;
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
    public ResponseEntity<ResCreateJobDTO> update(@Valid @RequestBody Job job) throws IdInvalidException {
        // check id
        Optional<Job> currentJob = this.jobService.fetchJobById(job.getId());
        if (!currentJob.isPresent()){
            throw new IdInvalidException("Job not found");
        }

        return ResponseEntity.ok().body(null);
    }
}
