package com.phresh;

import com.phresh.domain.Role;
import com.phresh.domain.RoleAccess;
import com.phresh.domain.User;
import com.phresh.repository.RoleAccessRepository;
import com.phresh.repository.RoleRepository;
import com.phresh.repository.UserRepository;
import com.phresh.security.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AppSetupService {

    private final RoleRepository roleRepository;
    private final RoleAccessRepository roleAccessRepository;
    private final UserRepository userRepository;
    private final PasswordEncryptor passwordEncryptor;

    @Autowired
    public AppSetupService(RoleRepository roleRepository, RoleAccessRepository roleAccessRepository, UserRepository userRepository, PasswordEncryptor passwordEncryptor) {
        this.roleRepository = roleRepository;
        this.roleAccessRepository = roleAccessRepository;
        this.userRepository = userRepository;
        this.passwordEncryptor = passwordEncryptor;
    }

    public void startApp() {
        RoleAccess userRoleAccess = new RoleAccess(RoleAccess.ROLE_USER);
        RoleAccess adminRoleAccess = new RoleAccess(RoleAccess.ROLE_ADMIN);

        roleAccessRepository.save(userRoleAccess);
        roleAccessRepository.save(adminRoleAccess);

        Role userRole = new Role("member", Set.of(userRoleAccess));
        Role adminRole = new Role("admin", Set.of(adminRoleAccess));

        roleRepository.save(userRole);
        roleRepository.save(adminRole);

        //TODO Not for production!
        User user = new User("Admin", "Admin", "phresh@admin.lu", passwordEncryptor.encryptPassword("Admin1-TestSystem"), Set.of(adminRole));
        userRepository.save(user);
    }
}
