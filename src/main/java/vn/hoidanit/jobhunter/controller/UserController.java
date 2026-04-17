package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/user")
    public User createNewUser(@RequestBody User postManUser) {
        User karicUser = this.userService.handleCreateNewUser(postManUser);

        return karicUser;
    }

    @DeleteMapping("/user/{id}")
    public  String deleteUser(@PathVariable("id") long id){
        this.userService.handleDeleteUser(id);
        return "delete user with id: " + id;
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return this.userService.fetchUserById(id);
    }

    @GetMapping("/user")
    public List<User> getAllUser() {
        return this.userService.fetchAllUser();
    }

    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable("id") long id, @RequestBody User user) {
        return this.userService.updateUser(id, user);
    }
}
