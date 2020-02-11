package com.cuupa.dms.ui.register;

import com.cuupa.dms.authentication.AccessControl;
import com.cuupa.dms.service.PasswordEncryptionService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Route("register")
@PageTitle("Register")
@CssImport("./styles/shared-styles.css")
public class RegisterScreen extends FlexLayout {

    private final Label errorLabel = new Label("Username already taken");

    private final TextField usernameTextField = new TextField("Email");

    private final TextField passwordTextField = new TextField("Password");

    private final TextField firstnameTextField = new TextField("First name");

    private final TextField lastnameTextField = new TextField("Last name");

    public RegisterScreen(@Autowired AccessControl accessControl, @Autowired PasswordEncryptionService passwordEncryptionService) {
        setSizeFull();

        errorLabel.setVisible(false);
        usernameTextField.setErrorMessage("Missing required field");
        passwordTextField.setErrorMessage("Missing required field");

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        final VerticalLayout
                verticalLayout =
                new VerticalLayout(usernameTextField, passwordTextField, firstnameTextField, lastnameTextField);
        verticalLayout.setSizeFull();
        verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(Alignment.CENTER);

        final Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> getUI().get().navigate(""));
        final Button registerButton = new Button("Register");
        registerButton.addClickListener(event -> {
            if (allRequiredFieldsFilled()) {
                try {
                    final String salt = passwordEncryptionService.generateSalt();
                    final String
                            encryptedPassword =
                            passwordEncryptionService.getEncryptedPassword(passwordTextField.getValue(), salt);
                    final boolean
                            successfull =
                            accessControl.register(usernameTextField.getValue(),
                                                   encryptedPassword,
                                                   salt,
                                                   firstnameTextField.getValue(),
                                                   lastnameTextField.getValue());
                    if (!successfull) {
                        errorLabel.setVisible(true);
                    } else {
                        accessControl.signIn(usernameTextField.getValue(), encryptedPassword);
                        getUI().get().navigate("");
                    }
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                }
            } else {
                if (StringUtils.isBlank(usernameTextField.getValue())) {
                    usernameTextField.setInvalid(true);
                }

                if (StringUtils.isBlank(passwordTextField.getValue())) {
                    passwordTextField.setInvalid(true);
                }
            }
        });
        registerButton.setThemeName("primary");

        final HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, registerButton);
        verticalLayout.add(buttonLayout);

        add(verticalLayout);

    }

    private boolean allRequiredFieldsFilled() {
        return StringUtils.isNotBlank(usernameTextField.getValue()) &&
               StringUtils.isNotBlank(passwordTextField.getValue());
    }
}