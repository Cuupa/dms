package com.cuupa.dms.ui.register

import com.cuupa.dms.UIConstants
import com.cuupa.dms.authentication.AccessControl
import com.cuupa.dms.service.EncryptionService
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.button.Button
import org.springframework.beans.factory.annotation.Autowired

class RegisterClicklistener(private val registerView: RegisterView) : ComponentEventListener<ClickEvent<Button?>> {

    override fun onComponentEvent(event: ClickEvent<Button?>) {
        if (passwordMatching()) {
            showErrorPasswordMatching()
        }
        if (allRequiredFieldsFilled()) {
            if (isMailAddress) {
                registerUser(registerView.accessControl, registerView.encryptionService)
            } else {
                showErrorNoMail()
            }
        } else {
            showErrorRequiredFields()
        }
    }

    private fun showErrorPasswordMatching() {
        registerView.passwordConfirmationTextField.isInvalid = true
    }

    private fun passwordMatching(): Boolean {
        return (registerView.passwordTextField
                .value
                == registerView.passwordConfirmationTextField.value)
    }

    private fun showErrorNoMail() {
        registerView.usernameTextField.errorMessage = registerView.getTranslation(UIConstants.provideValidMailaddress)
        registerView.usernameTextField.isInvalid = true
    }

    private val isMailAddress: Boolean
        get() {
            val username = registerView.usernameTextField.value
            return EMail(username).isValid
        }

    private fun showErrorRequiredFields() {
        if (registerView.usernameTextField.value.isBlank()) {
            registerView.usernameTextField.isInvalid = true
        }
        if (registerView.passwordTextField.value.isBlank()) {
            registerView.passwordTextField.isInvalid = true
        }
    }

    private fun registerUser(@Autowired accessControl: AccessControl, @Autowired encryptionService: EncryptionService) {
        val salt = encryptionService.generateSalt()
        val encryptedPassword = encryptionService.getEncryptedPassword(registerView.passwordTextField.value, salt)
        val successful = accessControl.register(registerView.usernameTextField.value,
                encryptedPassword,
                salt,
                registerView.firstnameTextField.value,
                registerView.lastnameTextField.value,
                encryptionService.accessToken)

        if (!successful) {
            registerView.errorLabel.isVisible = true
        } else {
            registerView.mailService.sendConfirmationMail()
            accessControl.signIn(registerView.usernameTextField.value, encryptedPassword, true)
            registerView.ui.get().navigate("")
        }
    }

    private fun allRequiredFieldsFilled(): Boolean {
        return registerView.usernameTextField.value.isNotBlank() &&
                registerView.passwordTextField.value.isNotBlank()
    }
}