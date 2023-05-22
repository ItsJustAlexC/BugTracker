package BugTracker.bugket.data;


import BugTracker.bugket.models.AppUser;


import java.util.ArrayList;

import java.util.List;


public class AppUserRepositoryDouble implements AppUserRepository{
    public List<AppUser> all = new ArrayList<>();

    public AppUserRepositoryDouble() {

        List<String> adminRole = new ArrayList<>();
        adminRole.add("ADMIN");
        List<String> devRole = new ArrayList<>();
        devRole.add("DEV");
        List<String> userRole = new ArrayList<>();
        userRole.add("USER");

        all.add(new AppUser("admin", "$2a$12$M97L0g/BETfVkdrWu98lWu29w1T232KW8CtJ8Q4XfP/NISiEy71xq", true, adminRole));
        all.add(new AppUser("test", "$2a$10$7JquBL6mi2OO85djCq4jUecR/aKurpmW8Niv1ohxNtoJdoNZPrcKK", true, userRole));
    }

    @Override
    public List<AppUser> findAll() {
        return all;
    }

    @Override
    public AppUser findByUsername(String username) {
        for(AppUser user : all){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    @Override
    public AppUser create(AppUser user) {
        user.setId(3);
        return user;
    }

    @Override
    public boolean editUserRole(String username, String newRole) {
        return true;
    }

    @Override
    public List<String> getRolesList() {
        ArrayList<String> roles = new ArrayList<>();
        roles.add("USER");
        roles.add("DEV");
        roles.add("ADMIN");
        return roles;
    }


}
