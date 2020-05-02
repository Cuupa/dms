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

    val errorLabel = Label(getTranslation("usernameAlreadyTaken"))
    val usernameTextField = TextField(getTranslation("email"))
    val passwordTextField = PasswordField(getTranslation("password"))
    val passwordConfirmationTextField = PasswordField(getTranslation("confirmpassword"))
    val firstnameTextField = TextField(getTranslation("firstname"))
    val lastnameTextField = TextField(getTranslation("lastname"))

    init {
        setSizeFull()
        errorLabel.isVisible = false
        usernameTextField.errorMessage = getTranslation("missingRequiredField")
        passwordTextField.errorMessage = getTranslation("missingRequiredField")
        passwordConfirmationTextField.errorMessage = getTranslation("passwordsNotMatching")
        justifyContentMode = JustifyContentMode.CENTER
        alignItems = FlexComponent.Alignment.CENTER
        val verticalLayout = VerticalLayout(errorLabel,
                usernameTextField,
                passwordTextField,
                passwordConfirmationTextField,
                firstnameTextField,
                lastnameTextField)
        verticalLayout.setSizeFull()
        verticalLayout.justifyContentMode = JustifyContentMode.CENTER
        verticalLayout.alignItems = FlexComponent.Alignment.CENTER
        val cancelButton = Button(getTranslation("cancel"))
        cancelButton.addClickListener { ui.get().navigate("") }
        val registerButton = Button(getTranslation("register"))
        registerButton.addClickListener(RegisterClicklistener(this))
        registerButton.themeName = UIConstants.primaryTheme
        val buttonLayout = HorizontalLayout(cancelButton, registerButton)
        verticalLayout.add(buttonLayout)
        add(verticalLayout)
    }
}