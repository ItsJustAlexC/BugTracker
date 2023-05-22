package BugTracker.bugket.domain;

import BugTracker.bugket.models.AppUser;
import BugTracker.bugket.data.AppUserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder encoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder encoder){
        this.appUserRepository = appUserRepository;
        this.encoder = encoder;
    }

    public List<AppUser> getAllAppUsers() {
        return appUserRepository.findAll();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        AppUser appUser = appUserRepository.findByUsername(username);

        if (appUser == null || !appUser.isEnabled()) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return appUser;
    }

    public Result<AppUser> create(String username, String password) {
        Result<AppUser> result = validate(username, password);

        AppUser existing = appUserRepository.findByUsername(username);
        if(existing != null) {
            result.addMessage(ActionStatus.DUPLICATE, "Username " + username + " is taken.");
        }

        if (!result.isSuccess()) {
            return result;
        }

        password = encoder.encode(password);

        AppUser appUser = new AppUser(0, username, password, true, List.of("USER"));

        try {
            appUser = appUserRepository.create(appUser);
            result.setPayload(appUser);
        } catch (DuplicateKeyException ex) {
            result.addMessage(ActionStatus.INVALID, "The provided username already exists");
        }
        return result;
    }

    public Result<Void> editUserRoleByUsername(String username, String newRole) throws UsernameNotFoundException {
        Result<Void> result = new Result<>();
        if(username == null || newRole == null) {
            result.addMessage(ActionStatus.INVALID, "Null fields");
            return result;
        }
        if(username.isBlank() || newRole.isBlank()) {
            result.addMessage(ActionStatus.INVALID, "Empty fields");
            return result;
        }

        AppUser appUser = appUserRepository.findByUsername(username);

        if(appUser == null) {
            result.addMessage(ActionStatus.NOT_FOUND, "User not found");
            return result;
        }
        if(appUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            result.addMessage(ActionStatus.INVALID,"Cannot edit an admin");
            return result;
        }

        List<String> roles = appUserRepository.getRolesList();
        if(!roles.contains(newRole)) {
            result.addMessage(ActionStatus.INVALID, newRole + " not an existing role");
        }

        boolean success = appUserRepository.editUserRole(username, newRole);
        if(!success){
            result.addMessage(ActionStatus.INVALID,"Failed to edit");
        }
        return result;
    }

    private Result<AppUser> validate(String username, String password) {
        Result<AppUser> result = new Result<>();
        if (username == null || username.isBlank()) {
            result.addMessage(ActionStatus.INVALID, "username is required");
            return result;
        }

        if (password == null || password.isBlank()) {
            result.addMessage(ActionStatus.INVALID, "password is required");
            return result;
        }

        if (username.length() > 40) {
            result.addMessage(ActionStatus.INVALID, "username must be less than 40 characters");
        }
        return result;
    }
}
