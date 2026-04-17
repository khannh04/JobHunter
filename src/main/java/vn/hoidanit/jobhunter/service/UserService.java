package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;

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

    public User fetchUserById(long id){
        Optional<User> userOptional = this.userRepository.findById(id);
        if(userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public List<User> fetchAllUser(){
        return this.userRepository.findAll();
    }

    public User updateUser(long id, User user){
        Optional<User> currentUser = this.userRepository.findById(id);
        if(currentUser.isPresent()){
            User updateUser = currentUser.get();
            updateUser.setName(user.getName());
            updateUser.setEmail(user.getEmail());
            updateUser.setPassword(user.getPassword());
            return this.userRepository.save(updateUser);
        }
        return null;
    }

}
