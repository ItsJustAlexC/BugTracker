package BugTracker.bugket.domain;

import BugTracker.bugket.data.AppUserRepository;
import BugTracker.bugket.models.AppUser;
import BugTracker.bugket.data.AppUserRepositoryDouble;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserServiceTest {

    AppUserRepository repository = new AppUserRepositoryDouble();
    AppUserService service = new AppUserService(repository, new BCryptPasswordEncoder());

    @Test
    void shouldFindAllAppUsers(){
        List<AppUser> expected = repository.findAll();
        List<AppUser> actual = service.getAllAppUsers();
        assertEquals(expected.size(), actual.size());
    }


    @Test
    void shouldFindByUsername() {
        List<String> userRole = new ArrayList<>();
        userRole.add("USER");
        AppUser expected = new AppUser("test","$2a$10$7JquBL6mi2OO85djCq4jUecR/aKurpmW8Niv1ohxNtoJdoNZPrcKK", true, userRole);
        AppUser actual = (AppUser) service.loadUserByUsername("test");
        assertEquals(expected.getUsername(), actual.getUsername());
    }

    @Test
    void shouldNotFindMissing() {
        try {
            AppUser actual = (AppUser) service.loadUserByUsername("missing");
        } catch (UsernameNotFoundException e) {
            assertEquals("missing not found", e.getMessage());
        }
    }

    @Test
    void shouldNotFindNull() {
        try {
            AppUser actual = (AppUser) service.loadUserByUsername(null);
        } catch (UsernameNotFoundException e) {
            assertEquals("null not found", e.getMessage());
        }
    }

    @Test
    void shouldCreate(){
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        AppUser expected = new AppUser(
                3,
                "test2",
                "[this value is random]",
                true,
                roles
        );
        Result<AppUser> result = service.create("test2", "test2");

        assertTrue(result.isSuccess());
        assertEquals(expected.getId(), result.getPayload().getId());
        assertEquals(expected.isEnabled(), result.getPayload().isEnabled());
        assertEquals(expected.getAuthorities(), result.getPayload().getAuthorities());
    }

    @Test
    void shouldNotCreateNull(){
        Result<AppUser> result = service.create(null, "$2a$10$O62QNAVUXhWangiXFqTtDuXBlP9ObEZ6w7y1xnlEOA1ywB7dbQvPK");
        assertFalse(result.isSuccess());

        Result<AppUser> result2 = service.create("test2", null);
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldNotCreateInvalid() {
        Result<AppUser> result = service.create("a".repeat(101), "test");
        assertFalse(result.isSuccess());

        Result<AppUser> result2 = service.create("", "$2a$10$O62QNAVUXhWangiXFqTtDuXBlP9ObEZ6w7y1xnlEOA1ywB7dbQvPK");
        assertFalse(result2.isSuccess());

        Result<AppUser> result3 = service.create("test2", "");
        assertFalse(result3.isSuccess());
    }

    @Test
    void shouldNotCreateDuplicateUsername() {
        Result<AppUser> result = service.create("test", "test2");
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotCreateUsername40Characters() {
        Result<AppUser> result = service.create("Extra-Supercalifragelisticexpialadocious!", "test2");
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldEditUserRole() {
        Result<Void> result = service.editUserRoleByUsername("test", "DEV");
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotEditMissingUser() {
        Result<Void> result = service.editUserRoleByUsername("not a user", "DEV");
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotEditMissingRole() {
        Result<Void> result = service.editUserRoleByUsername("test", "missing");
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotEditNulls() {
        Result<Void> result = service.editUserRoleByUsername(null, "DEV");
        assertFalse(result.isSuccess());

        Result<Void> result2 = service.editUserRoleByUsername("test", null);
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldNotEditEmpty() {
        Result<Void> result = service.editUserRoleByUsername("", "DEV");
        assertFalse(result.isSuccess());

        Result<Void> result2 = service.editUserRoleByUsername("test", "");
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldNotEditAdministratorRole() {
        Result<Void> result2 = service.editUserRoleByUsername("admin", "DEV");
        assertFalse(result2.isSuccess());
    }
}