package com.cuupa.dms.ui.register

import com.cuupa.dms.UIConstants
import com.cuupa.dms.authentication.AccessControl
import com.cuupa.dms.service.EncryptionService
import com.cuupa.dms.service.MailService
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Autowired

@Route("register")
@PageTitle("Register")
@CssImport("./styles/shared-styles.css")
class RegisterView(@param:Autowired val accessControl: AccessControl, @param:Autowired val encryptionService:
EncryptionService, @param:Autowired val mailService: MailService) : FlexLayout() {

    val errorLabel = Label(UIConstants.usernameAlreadyTaken)
    val usernameTextField = TextField(UIConstants.email)
    val passwordTextField = PasswordField(UIConstants.password)
    val passwordConfirmationTextField = TextField(UIConstants.confirmpassword)
    val firstnameTextField = TextField(UIConstants.firstname)
    val lastnameTextField = TextField(UIConstants.lastname)

    init {
        setSizeFull()
        errorLabel.isVisible = false
        usernameTextField.errorMessage = UIConstants.missingRequiredField
        passwordTextField.errorMessage = UIConstants.missingRequiredField
        passwordConfirmationTextField.errorMessage = UIConstants.passwordsNotMatching
        justifyContentMode = JustifyContentMode.CENTER
        alignItems = FlexComponent.Alignment.CENTER
        val verticalLayout = VerticalLayout(errorLabel,
                usernameTextField,
                passwordTextField,
                firstnameTextField,
                lastnameTextField)
        verticalLayout.setSizeFull()
        verticalLayout.justifyContentMode = JustifyContentMode.CENTER
        verticalLayout.alignItems = FlexComponent.Alignment.CENTER
        val cancelButton = Button(UIConstants.cancel)
        cancelButton.addClickListener { ui.get().navigate("") }
        val registerButton = Button(UIConstants.register)
        registerButton.addClickListener(RegisterClicklistener(this))
        registerButton.themeName = UIConstants.primaryTheme
        val buttonLayout = HorizontalLayout(cancelButton, registerButton)
        verticalLayout.add(buttonLayout)
        add(verticalLayout)
    }
}