package BugTracker.bugket.controllers;

import BugTracker.bugket.domain.AppUserService;
import BugTracker.bugket.domain.Result;
import BugTracker.bugket.models.AppUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }


    @GetMapping("/users")
    public List<AppUser> findAll(){
        return appUserService.getAllAppUsers();
    }


    @PutMapping("/update_user/{username}/{newRole}")
    public ResponseEntity<?> updateUserRole(@PathVariable String username, @PathVariable String newRole) {
        Result<Void> result = appUserService.editUserRoleByUsername(username, newRole);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
