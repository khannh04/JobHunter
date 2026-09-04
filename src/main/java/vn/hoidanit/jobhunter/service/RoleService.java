package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository){
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public boolean existByName(String name){
        return this.roleRepository.existsByName(name);
    }

    public Role create(Role role){
        // check permission
        if (role.getPermissions() != null){
            List<Long> reqPermission = role.getPermissions()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Permission> currentPermission = this.permissionRepository.findByIdIn(reqPermission);
            role.setPermissions(currentPermission);
        }
        return this.roleRepository.save(role);
    }

    public Role fetchById(long id){
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isPresent()){
            return roleOptional.get();
        }
        return null;
    }

    public Role update(Role role){
        Role roleDB = this.fetchById(role.getId());
        if (role.getPermissions() != null){
            List<Long> reqPermission = role.getPermissions()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Permission> permissions = this.permissionRepository.findByIdIn(reqPermission);
            role.setPermissions(permissions);
        }
        roleDB.setName(role.getName());
        roleDB.setActive(role.isActive());
        roleDB.setDescription(role.getDescription());
        roleDB.setPermissions(role.getPermissions());
        roleDB = this.roleRepository.save(roleDB);
        return roleDB;
    }

    public ResultPaginationDTO fetchAll(Specification<Role> spec, Pageable pageable){
        Page<Role> rolePage = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(rolePage.getNumber() + 1);
        mt.setPageSize(rolePage.getSize());

        mt.setPages(rolePage.getTotalPages());
        mt.setTotal(rolePage.getTotalElements());

        res.setMeta(mt);
        res.setResult(rolePage.getContent());

        return res;
    }
}
