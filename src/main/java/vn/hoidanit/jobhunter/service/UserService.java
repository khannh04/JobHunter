package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.*;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private  final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateNewUser(User user) {
         return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id){
        this.userRepository.deleteById(id);
    }

    public boolean isEmailExist(String email){
        return this.userRepository.existsByEmail(email);
    }

    public User fetchUserById(long id){
        Optional<User> userOptional = this.userRepository.findById(id);
        if(userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable){
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageUser.getNumber() + 1);
        mt.setPageSize(pageUser.getSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageUser.getContent());

        // remove sensitive data
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> new ResUserDTO(
                        item.getId(),
                        item.getEmail(),
                        item.getName(),
                        item.getGender(),
                        item.getAge(),
                        item.getAddress(),
                        item.getCreatedAt(),
                        item.getUpdatedAt()))
                .collect(Collectors.toList());
        rs.setResult(listUser);
        return rs;
    }

    public User updateUser(User user){
        Optional<User> currentUser = this.userRepository.findById(user.getId());
        if(currentUser.isPresent()){
            User updateUser = currentUser.get();
            updateUser.setAddress(user.getAddress());
            updateUser.setName(user.getName());
            updateUser.setAge(user.getAge());
            updateUser.setGender(user.getGender());
            return this.userRepository.save(updateUser);
        }
        return null;
    }

    public ResCreateUserDTO convertToResCreateUserDto(User user){
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setAddress(user.getAddress());
        res.setGender(user.getGender());
        res.setCreateAt(user.getCreatedAt());
        res.setEmail(user.getEmail());
        return res;
    }

    public ResUserDTO convertToResUserDto(User user){
        ResUserDTO res = new ResUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setEmail(user.getEmail());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
    }

    public ResUpdateUserDTO convertToResUpdateUserDto(User user){
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
    }

    public User handleUserByUsername(String username){
        return this.userRepository.findByEmail(username);
    }

}
