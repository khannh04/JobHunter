package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;

import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository){
        this.permissionRepository = permissionRepository;
    }

    public boolean isPermissionExist(Permission p){
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(p.getModule(), p.getApiPath(), p.getMethod());
    }

    public Permission create(Permission permission){
        return this.permissionRepository.save(permission);
    }

    public Permission fetchById(long id){
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        if (permissionOptional.isPresent()) {
            return permissionOptional.get();
        }
        return null;
    }

    public Permission update(Permission p){
            Permission updatePermission = this.fetchById(p.getId());
            if (updatePermission != null){
                updatePermission.setName(p.getName());
                updatePermission.setApiPath(p.getApiPath());
                updatePermission.setMethod(p.getMethod());
                updatePermission.setModule(p.getModule());

                updatePermission = this.permissionRepository.save(updatePermission);
                return updatePermission;
        }
        return null;
    }

    public ResultPaginationDTO getAll(Specification<Permission> spec, Pageable pageable){
        Page<Permission> permissionPage = this.permissionRepository.findAll(pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(permissionPage.getNumber() + 1);
        mt.setPageSize(permissionPage.getSize());

        mt.setPages(permissionPage.getTotalPages());
        mt.setTotal(permissionPage.getTotalElements());

        res.setMeta(mt);
        res.setResult(permissionPage.getContent());

        return res;
    }

    public void delete(long id){
        // delete permission_role
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        Permission currentPermission = permissionOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        // delete permission
        this.permissionRepository.delete(currentPermission);
    }
}
