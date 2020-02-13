package com.cuupa.dms.ui.settings

import com.cuupa.dms.UIConstants
import com.cuupa.dms.authentication.CurrentUser
import com.cuupa.dms.authentication.DatabaseAccessControl
import com.cuupa.dms.authentication.User
import com.cuupa.dms.service.EncryptionService
import com.cuupa.dms.ui.MainView
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.accordion.Accordion
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.OptionalParameter
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Autowired

@Route(value = "user", layout = MainView::class)
class UserSettings(@Autowired databaseAccessControl: DatabaseAccessControl, @Autowired encryptionService: EncryptionService) : HorizontalLayout(), HasUrlParameter<String?> {

    private val firstnameTextField = TextField(UIConstants.firstname)
    private val lastnameTextField = TextField(UIConstants.lastname)
    private val accessTokenTextField = TextField(UIConstants.accesstoken)
    private val passwordTextField = TextField(UIConstants.newpassword)
    private val passwordMatchingTextField = TextField(UIConstants.confirmpassword)

    private fun saveChanges(databaseAccessControl: DatabaseAccessControl, encryptionService: EncryptionService, user: User) {
        user.firstname = firstnameTextField.value
        user.lastname = lastnameTextField.value
        val accessToken = accessTokenTextField.value
        if (passwordTextField.value.isNotBlank()) {
            val newSalt = encryptionService.generateSalt()
            val encryptedPassword = encryptionService.getEncryptedPassword(passwordTextField.value, newSalt)
            databaseAccessControl.save(user, encryptedPassword, newSalt, accessToken)
        } else {
            databaseAccessControl.save(user, accessToken)
        }
    }

    private fun getExternalAccesLayout(databaseAccessControl: DatabaseAccessControl, user: User): Component {
        val horizontalLayout = HorizontalLayout()
        horizontalLayout.width = UIConstants.maxSize
        accessTokenTextField.isReadOnly = true
        accessTokenTextField.value = databaseAccessControl.getAccessToken(user.username)
        accessTokenTextField.width = UIConstants.maxSize
        val button = Button(UIConstants.generatenewaccesstoken, VaadinIcon.REFRESH.create())
        horizontalLayout.add(accessTokenTextField, button)
        horizontalLayout.defaultVerticalComponentAlignment = FlexComponent.Alignment.END
        return horizontalLayout
    }

    private fun getPersonalInformationLayout(user: User): Component {
        val personalInformationLayout = VerticalLayout()
        personalInformationLayout.width = UIConstants.maxSize
        firstnameTextField.value = user.firstname
        firstnameTextField.width = UIConstants.maxSize
        lastnameTextField.value = user.lastname
        lastnameTextField.width = UIConstants.maxSize
        personalInformationLayout.add(firstnameTextField, lastnameTextField)
        return personalInformationLayout
    }

    private fun getUsersettingsLayout(user: User): Component {
        val userSettingsLayout = VerticalLayout()
        userSettingsLayout.width = UIConstants.maxSize
        val textFieldUsername = TextField(UIConstants.username)
        textFieldUsername.isEnabled = false
        textFieldUsername.value = user.username
        textFieldUsername.width = UIConstants.maxSize
        passwordTextField.width = UIConstants.maxSize
        passwordMatchingTextField.width = UIConstants.maxSize
        userSettingsLayout.add(textFieldUsername, passwordTextField, passwordMatchingTextField)
        return userSettingsLayout
    }

    override fun setParameter(event: BeforeEvent, @OptionalParameter parameter: String?) {}

    companion object {
        const val VIEW_NAME = "user"
    }

    init {
        setSizeFull()
        val accordion = Accordion()
        accordion.width = UIConstants.maxSize
        val user = CurrentUser.get()
        if (!user.isEmpty) {
            val verticalLayout = VerticalLayout()
            verticalLayout.setSizeFull()
            accordion.open(0)
            accordion.add(UIConstants.usersettings, getUsersettingsLayout(user))
            accordion.add(UIConstants.personalInformation, getPersonalInformationLayout(user))
            accordion.add(UIConstants.externalAccess, getExternalAccesLayout(databaseAccessControl, user))
            add(accordion)
            val cancelButton = Button(UIConstants.cancel)
            cancelButton.addClickListener { event: ClickEvent<Button> -> event.source.ui.ifPresent { ui: UI -> ui.navigate("") } }
            val saveButton = Button(UIConstants.save)
            saveButton.addClickListener {
                if (passwordMatchingTextField.value == passwordTextField.value) {
                    saveChanges(databaseAccessControl, encryptionService, user)
                }
            }
            saveButton.themeName = UIConstants.primaryTheme
            verticalLayout.add(accordion)
            val horizontalLayout = HorizontalLayout(cancelButton, saveButton)
            verticalLayout.add(horizontalLayout)
            add(verticalLayout)
        }
    }
}