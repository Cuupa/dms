package com.cuupa.dms.ui.settings;

import com.cuupa.dms.authentication.CurrentUser;
import com.cuupa.dms.authentication.DatabaseAccessControl;
import com.cuupa.dms.authentication.User;
import com.cuupa.dms.service.EncryptionService;
import com.cuupa.dms.ui.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Route(value = "user", layout = MainView.class)
public class UserSettings extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "user";

    private final TextField firstnameTextField = new TextField("First name");

    private final TextField lastnameTextField = new TextField("Last name");

    private final TextField accessTokenTextField = new TextField("Accesstoken");

    private final TextField passwordTextField = new TextField("New password");

    private final TextField passwordMatchingTextField = new TextField("Confirm password");

    public UserSettings(@Autowired DatabaseAccessControl databaseAccessControl, @Autowired EncryptionService encryptionService) {
        setSizeFull();
        final Accordion accordion = new Accordion();
        accordion.setWidth("100%");
        final User user = CurrentUser.get();
        if (!user.isEmpty()) {
            final VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setSizeFull();
            accordion.open(0);
            accordion.add("User setttings", getUsersettingsLayout(user));
            accordion.add("Personal information", getPersonalInformationLayout(user));
            accordion.add("External access", getExternalAccesLayout(user));
            add(accordion);
            final Button cancelButton = new Button("Cancel");
            cancelButton.addClickListener(event -> event.getSource().getUI().ifPresent(ui -> ui.navigate("")));
            final Button saveButton = new Button("Save");
            saveButton.addClickListener(event -> {
                if (passwordMatchingTextField.getValue().equals(passwordTextField.getValue())) {
                    saveChanges(databaseAccessControl, encryptionService, user);
                }
            });
            saveButton.setThemeName("primary");
            verticalLayout.add(accordion);
            final HorizontalLayout horizontalLayout = new HorizontalLayout(cancelButton, saveButton);
            verticalLayout.add(horizontalLayout);
            add(verticalLayout);
        }
    }

    private void saveChanges(DatabaseAccessControl databaseAccessControl, EncryptionService encryptionService, User user) {
        try {
            final User userFromDb = databaseAccessControl.getUser(user.getUsername());
            user.setFirstname(firstnameTextField.getValue());
            user.setLastname(lastnameTextField.getValue());
            user.setAccessToken(accessTokenTextField.getValue());
            if (StringUtils.isNotBlank(passwordTextField.getValue())) {
                final String salt = encryptionService.generateSalt();
                final String
                        encryptedPassword =
                        encryptionService.getEncryptedPassword(passwordTextField.getValue(), salt);
                user.setPassword(encryptedPassword);
                user.setSalt(salt);
            }
            user.setPassword(userFromDb.getPassword());
            user.setSalt(userFromDb.getSalt());
            databaseAccessControl.save(user);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    private Component getExternalAccesLayout(final User user) {
        final HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");

        accessTokenTextField.setReadOnly(true);
        accessTokenTextField.setValue(user.getAccessToken());
        accessTokenTextField.setWidth("100%");
        final Button button = new Button();
        button.setIcon(VaadinIcon.REFRESH.create());
        button.setText("Generate new accesstoken");
        horizontalLayout.add(accessTokenTextField, button);
        horizontalLayout.setDefaultVerticalComponentAlignment(Alignment.END);

        return horizontalLayout;
    }

    private Component getPersonalInformationLayout(final User user) {
        final VerticalLayout personalInformationLayout = new VerticalLayout();
        personalInformationLayout.setWidth("100%");

        firstnameTextField.setValue(user.getFirstname());
        firstnameTextField.setWidth("100%");

        lastnameTextField.setValue(user.getLastname());
        lastnameTextField.setWidth("100%");
        personalInformationLayout.add(firstnameTextField, lastnameTextField);

        return personalInformationLayout;
    }

    private Component getUsersettingsLayout(final User user) {
        final VerticalLayout userSettingsLayout = new VerticalLayout();
        userSettingsLayout.setWidth("100%");
        final TextField textFieldUsername = new TextField("Username");
        textFieldUsername.setEnabled(false);
        textFieldUsername.setValue(user.getUsername());
        textFieldUsername.setWidth("100%");
        passwordTextField.setWidth("100%");
        passwordMatchingTextField.setWidth("100%");
        userSettingsLayout.add(textFieldUsername, passwordTextField, passwordMatchingTextField);
        return userSettingsLayout;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {

    }
}
