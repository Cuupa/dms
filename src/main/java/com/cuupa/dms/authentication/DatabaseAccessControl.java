package com.cuupa.dms.authentication;

import com.cuupa.dms.database.user.DbUser;
import com.cuupa.dms.database.user.UserRepository;
import com.cuupa.dms.service.EncryptionService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DatabaseAccessControl implements AccessControl {

    private final UserRepository userRepository;

    private final EncryptionService encryptionService;

    public DatabaseAccessControl(@Autowired UserRepository userRepository, @Autowired EncryptionService encryptionService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
    }

    @Override
    public boolean signIn(final String username, final String password, final boolean alreadyHashed) {
        DbUser dbUser = userRepository.findByUsername(username);
        if (dbUser != null) {
            try {
                String encryptedPassword = getPassword(password, alreadyHashed, dbUser);
                dbUser = userRepository.findByUsernameAndPassword(username, encryptedPassword);
                if (dbUser != null) {
                    User
                            user =
                            new User(dbUser.getId(),
                                     dbUser.getUsername(),
                                     dbUser.getFirstname(),
                                     dbUser.getLastname(),
                                     dbUser.getAccesstoken(),
                                     dbUser.isConfirmed());
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
    public User getUser(final String username) {
        final DbUser dbUser = userRepository.findByUsername(username);
        final User
                user =
                new User(dbUser.getId(),
                         dbUser.getUsername(),
                         dbUser.getFirstname(),
                         dbUser.getLastname(),
                         dbUser.getAccesstoken(),
                         dbUser.isConfirmed());

        user.setSalt(dbUser.getSalt());
        user.setPassword(dbUser.getPassword());
        return user;
    }

    private String getPassword(String password, boolean alreadyHashed, DbUser dbUser) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String encryptedPassword;
        if (!alreadyHashed) {
            encryptedPassword = encryptionService.getEncryptedPassword(password, dbUser.getSalt());
        } else {
            encryptedPassword = password;
        }
        return encryptedPassword;
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
    public boolean register(final String username, final String password, final String salt, final String firstname, final String lastname, final String accesstoken) {
        final DbUser byUsername = userRepository.findByUsername(username);
        if (byUsername == null) {
            final DbUser dbUser = new DbUser(username, password, salt, firstname, lastname, accesstoken, false);
            return userRepository.save(dbUser) != null;
        }

        return false;
    }

    @Override
    public boolean save(User user) {
        final DbUser
                dbUser =
                new DbUser(user.getId(),
                           user.getUsername(),
                           user.getPassword(),
                           user.getSalt(),
                           user.getFirstname(),
                           user.getLastname(),
                           user.getAccessToken(),
                           user.isConfirmed());

        return userRepository.save(dbUser) != null;
    }
}
