package com.cuupa.dms.ui.login

import com.cuupa.dms.UIConstants
import com.cuupa.dms.authentication.AccessControl
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.login.AbstractLogin.LoginEvent
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.login.LoginI18n
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.apache.juli.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired

@Route("login")
@PageTitle("Login")
@CssImport("./styles/shared-styles.css")
class LoginScreen(@param:Autowired private val accessControl: AccessControl) : FlexLayout() {

    private fun buildUI() {
        setSizeFull()
        className = "login-screen"
        val loginForm = LoginForm(LoginI18n())
        loginForm.addLoginListener { event: LoginEvent -> login(event) }
        loginForm.addForgotPasswordListener { Notification.show("") }
        val centeringLayout = VerticalLayout()
        centeringLayout.setSizeFull()
        centeringLayout.justifyContentMode = JustifyContentMode.CENTER
        centeringLayout.alignItems = FlexComponent.Alignment.CENTER
        centeringLayout.add(loginForm)
        val registerButton = Button(getTranslation(UIConstants.register))
        registerButton.addClickListener { ui.get().navigate(UIConstants.register) }
        centeringLayout.add(registerButton)
        add(centeringLayout)
    }

    private fun login(event: LoginEvent) {
        if (accessControl.signIn(event.username, event.password, false)) {
            ui.get().navigate("")
        } else {
            event.source.isError = true
            LOGGER.error("Failed to log in user '${event.username}'")
            val runnable = Runnable {
                try {
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    event.source.isEnabled = true
                }
            }
            Thread(runnable).start()
        }
    }

    companion object {
        private val LOGGER = LogFactory.getLog(LoginScreen::class.java)
    }

    init {
        buildUI()
    }
}