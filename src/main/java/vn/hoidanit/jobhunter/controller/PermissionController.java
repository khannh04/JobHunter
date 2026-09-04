package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService){
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> create(@Valid @RequestBody Permission permission) throws IdInvalidException {
        if (this.permissionService.isPermissionExist(permission)){
            throw new IdInvalidException("Permission already exists!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.create(permission));
    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> update(@RequestBody Permission permission) throws IdInvalidException {
        if (this.permissionService.fetchById(permission.getId()) == null){
            throw new IdInvalidException("Permission is not exist");
        }
        if (this.permissionService.isPermissionExist(permission)){
            throw new IdInvalidException("Permission already exists!");
        }
        return ResponseEntity.ok().body(this.permissionService.update(permission));
    }

    @GetMapping("/permissions")
    @ApiMessage("fetch all permissions")
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Permission> spec, Pageable pageable){
        return ResponseEntity.ok().body(this.permissionService.getAll(spec, pageable));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("delete a permission")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if(this.permissionService.fetchById(id) ==  null){
            throw new IdInvalidException("Permission with id: " + id + " does not exist");
        }

        this.permissionService.delete(id);
        return ResponseEntity.ok().body(null);
    }
}
