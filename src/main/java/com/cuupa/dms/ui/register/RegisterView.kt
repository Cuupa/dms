package com.cuupa.dms.ui.register;

import com.cuupa.dms.authentication.AccessControl;
import com.cuupa.dms.service.EncryptionService;
import com.cuupa.dms.service.MailService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("register")
@PageTitle("Register")
@CssImport("./styles/shared-styles.css")
public class RegisterScreen extends FlexLayout {

    private final Label errorLabel = new Label("Username already taken");

    private final TextField usernameTextField = new TextField("Email");

    private final TextField passwordTextField = new TextField("Password");

    private final TextField passwordConfirmationTextField = new TextField("Confirm password");

    private final TextField firstnameTextField = new TextField("First name");

    private final TextField lastnameTextField = new TextField("Last name");

    private final AccessControl accessControl;

    private final EncryptionService encryptionService;

    private final MailService mailService;

    public RegisterScreen(@Autowired AccessControl accessControl, @Autowired EncryptionService encryptionService, @Autowired MailService mailService) {
        this.accessControl = accessControl;
        this.encryptionService = encryptionService;
        this.mailService = mailService;
        setSizeFull();

        errorLabel.setVisible(false);
        usernameTextField.setErrorMessage("Missing required field");
        passwordTextField.setErrorMessage("Missing required field");
        passwordConfirmationTextField.setErrorMessage("Password does not match");

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        final VerticalLayout
                verticalLayout =
                new VerticalLayout(errorLabel,
                                   usernameTextField,
                                   passwordTextField,
                                   firstnameTextField,
                                   lastnameTextField);
        verticalLayout.setSizeFull();
        verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(Alignment.CENTER);

        final Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> getUI().get().navigate(""));
        final Button registerButton = new Button("Register");
        registerButton.addClickListener(new RegisterClicklistener(this));

        registerButton.setThemeName("primary");

        final HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, registerButton);
        verticalLayout.add(buttonLayout);

        add(verticalLayout);

    }

    protected AccessControl getAccessControl() {
        return accessControl;
    }

    protected EncryptionService getEncryptionService() {
        return encryptionService;
    }

    protected Label getErrorLabel() {
        return errorLabel;
    }

    protected TextField getUsernameTextField() {
        return usernameTextField;
    }

    protected TextField getPasswordTextField() {
        return passwordTextField;
    }

    public TextField getPasswordConfirmationTextField() {
        return passwordConfirmationTextField;
    }

    protected TextField getFirstnameTextField() {
        return firstnameTextField;
    }

    protected TextField getLastnameTextField() {
        return lastnameTextField;
    }

    public MailService getMailService() {
        return mailService;
    }
}