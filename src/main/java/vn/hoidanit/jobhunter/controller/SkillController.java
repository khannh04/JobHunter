package vn.hoidanit.jobhunter.controller;


import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService){
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create a skill")
    public ResponseEntity<Skill> create(@Valid @RequestBody Skill skill) throws IdInvalidException {
        // check name
        if(skill.getName() != null && this.skillService.isNameExist(skill.getName())){
            throw new IdInvalidException("Skill name '" + skill.getName() + "' already exists.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.createASkill(skill));
    }

    @PutMapping("/skills")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> update(@Valid @RequestBody Skill skill) throws IdInvalidException {
        // check id
        Skill currentSkill = this.skillService.fetchSkillById(skill.getId());
        if (currentSkill == null){
            throw new IdInvalidException("Skill id = " + skill.getId() + " does not exist.");
        }

        // check name
        if(skill.getName() != null && this.skillService.isNameExist(skill.getName())){
            throw new IdInvalidException("Skill name '" + skill.getName() + "' already exists.");
        }
        return ResponseEntity.ok().body(this.skillService.updateASkill(skill));
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete A Skill")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Skill currentSkill = this.skillService.fetchSkillById(id);
        if (currentSkill == null){
            throw new IdInvalidException("Skill not found");
        }
        this.skillService.deleteSkillById(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/skills")
    @ApiMessage("Fetch All Skills")
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Skill> spec, Pageable pageable){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.fetchAllSkills(spec, pageable));
    }

}
