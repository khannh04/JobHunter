package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("create a role")
    public ResponseEntity<Role> create(@Valid @RequestBody Role role) throws IdInvalidException {
        // check permission
        if (this.roleService.existByName(role.getName())){
            throw new IdInvalidException("Role with name: " + role.getName() + " already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(role));
    }

    @PutMapping("/roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> update(@RequestBody Role role) throws IdInvalidException {
        if (this.roleService.fetchById(role.getId()) == null){
            throw new IdInvalidException("Role with id: " + role.getId() + " does not exist");
        }
        if (this.roleService.existByName(role.getName())){
            throw new IdInvalidException("Role with name: " + role.getName() + " already exists");
        }
        return ResponseEntity.ok().body(this.roleService.update(role));
    }

    @GetMapping("/roles")
    @ApiMessage("fetch all roles")
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Role> spec, Pageable pageable){
        return ResponseEntity.ok().body(this.roleService.fetchAll(spec, pageable));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("delete a role")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        // check id
        if (this.roleService.fetchById(id) == null){
            throw new IdInvalidException("Role with id: " + id + " does not exist");
        }
        return ResponseEntity.ok().body(null);
    }
}
