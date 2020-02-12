package com.cuupa.dms.ui.register;

import com.cuupa.dms.authentication.AccessControl;
import com.cuupa.dms.service.EncryptionService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class RegisterClicklistener implements com.vaadin.flow.component.ComponentEventListener<com.vaadin.flow.component.ClickEvent<com.vaadin.flow.component.button.Button>> {

    private final RegisterScreen registerScreen;

    public RegisterClicklistener(RegisterScreen registerScreen) {
        this.registerScreen = registerScreen;
    }

    @Override
    public void onComponentEvent(ClickEvent<Button> event) {

        if (passwordMatching()) {
            showErrorPasswordMatching();
        }

        if (allRequiredFieldsFilled()) {
            if (isMailAddress()) {
                registerUser(registerScreen.getAccessControl(), registerScreen.getEncryptionService());
            } else {
                showErrorNoMail();
            }
        } else {
            showErrorRequiredFields();
        }
    }

    private void showErrorPasswordMatching() {
        registerScreen.getPasswordConfirmationTextField().setInvalid(true);
    }

    private boolean passwordMatching() {
        return registerScreen.getPasswordTextField()
                             .getValue()
                             .equals(registerScreen.getPasswordConfirmationTextField().getValue());
    }

    private void showErrorNoMail() {
        registerScreen.getUsernameTextField().setErrorMessage("Please provide a valid mail address");
        registerScreen.getUsernameTextField().setInvalid(true);
    }

    private boolean isMailAddress() {
        final String username = registerScreen.getUsernameTextField().getValue();
        return new EMail(username).isValid();
    }

    private void showErrorRequiredFields() {
        if (StringUtils.isBlank(registerScreen.getUsernameTextField().getValue())) {
            registerScreen.getUsernameTextField().setInvalid(true);
        }

        if (StringUtils.isBlank(registerScreen.getPasswordTextField().getValue())) {
            registerScreen.getPasswordTextField().setInvalid(true);
        }
    }

    private void registerUser(@Autowired AccessControl accessControl, @Autowired EncryptionService encryptionService) {
        try {
            final String salt = encryptionService.generateSalt();
            final String
                    encryptedPassword =
                    encryptionService.getEncryptedPassword(registerScreen.getPasswordTextField().getValue(), salt);
            final boolean
                    successfull =
                    accessControl.register(registerScreen.getUsernameTextField().getValue(),
                                           encryptedPassword,
                                           salt,
                                           registerScreen.getFirstnameTextField().getValue(),
                                           registerScreen.getLastnameTextField().getValue(),
                                           encryptionService.getAccessToken());
            if (!successfull) {
                registerScreen.getErrorLabel().setVisible(true);
            } else {
                registerScreen.getMailService().sendConfirmationMail();
                accessControl.signIn(registerScreen.getUsernameTextField().getValue(), encryptedPassword, true);
                registerScreen.getUI().get().navigate("");
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    private boolean allRequiredFieldsFilled() {
        return StringUtils.isNotBlank(registerScreen.getUsernameTextField().getValue()) &&
               StringUtils.isNotBlank(registerScreen.getPasswordTextField().getValue());
    }
}
