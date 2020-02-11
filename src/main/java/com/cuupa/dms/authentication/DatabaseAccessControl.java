package com.cuupa.dms.authentication;

import com.cuupa.dms.database.user.DbUser;
import com.cuupa.dms.database.user.UserRepository;
import com.cuupa.dms.service.PasswordEncryptionService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DatabaseAccessControl implements AccessControl {

    private final UserRepository userRepository;

    private final PasswordEncryptionService passwordEncryptionService;

    public DatabaseAccessControl(@Autowired UserRepository userRepository, @Autowired PasswordEncryptionService passwordEncryptionService) {
        this.userRepository = userRepository;
        this.passwordEncryptionService = passwordEncryptionService;
    }

    @Override
    public boolean signIn(final String username, final String password) {
        DbUser dbUser = userRepository.findByUsername(username);
        if (dbUser != null) {
            try {
                final String
                        encryptedPassword =
                        passwordEncryptionService.getEncryptedPassword(password, dbUser.getSalt());
                dbUser = userRepository.findByUsernameAndPassword(username, encryptedPassword);
                if (dbUser != null) {
                    User user = new User(dbUser.getUsername(), dbUser.getFirstname(), dbUser.getLastname());
                    CurrentUser.set(user);
                }
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserSingedIn() {
        return !CurrentUser.get().isEmpty();
    }

    @Override
    public boolean isUserRole(UserRole role) {
        return false;
    }

    @Override
    public String getPrincipalName() {
        return CurrentUser.get().getUsername();
    }

    @Override
    public void singOut() {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().navigate("");
    }

    @Override
    public boolean register(final String username, final String password, final String salt, final String firstname, final String lastname) {
        final DbUser byUsername = userRepository.findByUsername(username);
        if (byUsername == null) {
            final DbUser dbUser = new DbUser(username, password, salt, firstname, lastname);
            return userRepository.save(dbUser) != null;
        }

        return false;
    }
}
