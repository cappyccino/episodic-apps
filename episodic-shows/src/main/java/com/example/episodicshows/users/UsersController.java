package com.example.episodicshows.users;

import com.example.episodicshows.viewings.ViewingResponseWrapper;
import com.example.episodicshows.viewings.ViewingService;
import com.example.episodicshows.viewings.ViewingWrapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserRepository userRepository;
    private final ViewingService viewingService;

    public UsersController(UserRepository userRepository, ViewingService viewingService) {
        assert userRepository != null && viewingService != null;

        this.userRepository = userRepository;
        this.viewingService = viewingService;
    }

    @GetMapping
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/{id}/recently-watched")
    public List<ViewingResponseWrapper> getRecentlyWatched(@PathVariable("id") Long id) {
        return viewingService.getRecentlyWatched(id);
    }

    @PatchMapping("/{id}/viewings")
    public void createOrUpdateViewing(
            @PathVariable("id") Long userId,
            @RequestBody ViewingWrapper data) {
        viewingService.createOrUpdateViewing(userId, data);
    }
}
