package com.cuupa.dms.ui.login;

import com.cuupa.dms.authentication.AccessControl;
import com.cuupa.dms.authentication.AccessControlFactory;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;


@Route("Login")
@PageTitle("Login")
@CssImport("./styles/shared-styles.css")
public class LoginScreen extends FlexLayout {

    private static final Log LOGGER = LogFactory.getLog(LoginScreen.class);

    private AccessControl accessControl;

    public LoginScreen() {
        accessControl = AccessControlFactory.getInstance().createAccessControl();
        buildUI();
    }

    private void buildUI() {
        setSizeFull();
        setClassName("login-screen");

        LoginForm loginForm = new LoginForm();
        loginForm.addLoginListener(this::login);
        loginForm.addForgotPasswordListener(event -> Notification.show(""));

        FlexLayout centeringLayout = new FlexLayout();
        centeringLayout.setSizeFull();
        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeringLayout.setAlignItems(Alignment.CENTER);
        centeringLayout.add(loginForm);

        add(centeringLayout);
    }

    private void login(final AbstractLogin.LoginEvent event) {
        if (accessControl.signIn(event.getUsername(), event.getPassword())) {
            getUI().get().navigate("");
        } else {
            event.getSource().setError(true);
            LOGGER.error("Failed to log in user '" + event.getUsername() + "'");
            Runnable runnable = () -> {
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    event.getSource().setEnabled(true);
                }
            };

            new Thread(runnable).start();
        }
    }
}
