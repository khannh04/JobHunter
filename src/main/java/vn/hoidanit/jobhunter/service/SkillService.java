package vn.hoidanit.jobhunter.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;

import java.util.Optional;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository){
        this.skillRepository = skillRepository;
    }

    public boolean isNameExist(String name){
        return this.skillRepository.existsByName(name);
    }

    public Skill createASkill(Skill skill){
        return this.skillRepository.save(skill);
    }

    public Skill fetchSkillById(long id){
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        if(skillOptional.isPresent()){
            return skillOptional.get();
        }
        return null;
    }

    public Skill updateASkill(Skill skill){
        Optional<Skill> currentSkill = this.skillRepository.findById(skill.getId());
        if (currentSkill.isPresent()){
            Skill updateSkill = currentSkill.get();
            updateSkill.setName(skill.getName());
            return this.skillRepository.save(updateSkill);
        }
        return null;
    }

    public ResultPaginationDTO fetchAllSkills(Specification<Skill> spec, Pageable pageable){
        Page<Skill> skillPage = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(skillPage.getNumber() + 1);
        mt.setPageSize(skillPage.getSize());

        mt.setPages(skillPage.getTotalPages());
        mt.setTotal(skillPage.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(skillPage.getContent());

        return rs;
    }

    public void deleteSkillById(long id){
        // delete job inside skill_job table
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        //delete
        this.skillRepository.delete(currentSkill);
    }
}
