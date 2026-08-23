package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository){
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public ResCreateJobDTO create(Job job) {
        // check skill
        if (job.getSkills() != null) {
            List<Long> reqSkill = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skill> dbSkill = this.skillRepository.findByIdIn(reqSkill);
            job.setSkills(dbSkill);
        }

        // create job;
        Job currentJob = this.jobRepository.save(job);

        // convert response
        ResCreateJobDTO res = new ResCreateJobDTO();
        res.setId(currentJob.getId());
        res.setName(currentJob.getName());
        res.setSalary(currentJob.getSalary());
        res.setQuantity(currentJob.getQuantity());
        res.setLocation(currentJob.getLocation());
        res.setLevel(currentJob.getLevel());
        res.setStartDate(currentJob.getStartDate());
        res.setEndDate(currentJob.getEndDate());
        res.setActive(currentJob.isActive());
        res.setCreatedAt(currentJob.getCreatedAt());
        res.setCreatedBy(currentJob.getCreatedBy());

        if(currentJob.getSkills() != null){
            List<String> skills = currentJob.getSkills()
                    .stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            res.setSkills(skills);
        }

        return res;

    }

    public Optional<Job> fetchJobById(long id){
        return this.jobRepository.findById(id);
    }

    public ResUpdateJobDTO update(Job job){
        // check skill
        if (job.getSkills() != null){
            List<Long> reqSkill = job.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Skill> dbSkill = this.skillRepository.findByIdIn(reqSkill);
            job.setSkills(dbSkill);
        }

        // update job
        Job currentJob = this.jobRepository.save(job);

        // convert response
        ResUpdateJobDTO res = new ResUpdateJobDTO();
        res.setId(currentJob.getId());
        res.setName(currentJob.getName());
        res.setSalary(currentJob.getSalary());
        res.setQuantity(currentJob.getQuantity());
        res.setLocation(currentJob.getLocation());
        res.setLevel(currentJob.getLevel());
        res.setStartDate(currentJob.getStartDate());
        res.setEndDate(currentJob.getEndDate());
        res.setActive(currentJob.isActive());
        res.setCreatedAt(currentJob.getCreatedAt());
        res.setCreatedBy(currentJob.getCreatedBy());

        if (currentJob.getSkills() != null){
            List<String> skill = currentJob.getSkills()
                    .stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            res.setSkills(skill);
        }

        return res;
    }

    public void delete(long id){
        this.jobRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAllJobs(Specification<Job> spe, Pageable pageable){
        Page<Job> jobPage = this.jobRepository.findAll(spe, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(jobPage.getNumber() + 1);
        mt.setPageSize(jobPage.getSize());

        mt.setPages(jobPage.getTotalPages());
        mt.setTotal(jobPage.getTotalElements());

        rs.setResult(jobPage.getContent());
        rs.setMeta(mt);

        return rs;
    }
}
