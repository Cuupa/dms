package com.cuupa.dms.ui.dialog

import com.cuupa.dms.UIConstants
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout

class ConfirmDoneDialog(vararg components: Component?) : Dialog(*components) {

    lateinit var component: DialogListener

    fun setCallBack(component: DialogListener) {
        this.component = component
    }

    val messageLabel = Label("Do you want to mark the document as 'done'?")
    val confirmButton = Button("Confirm")
    val cancelButton = Button("Cancel")

    init {
        confirmButton.themeName = UIConstants.primaryTheme
        confirmButton.addClickListener { event ->
            component.consumeEvent(Event.CONFIRM)
            close()
        }
        cancelButton.addClickListener { close() }
        val layout = VerticalLayout()
        val horizontalLayout = HorizontalLayout()
        horizontalLayout.add(confirmButton, cancelButton)
        layout.add(messageLabel, horizontalLayout)
        add(layout)
    }

    enum class Event {
        CONFIRM, CANCEL
    }
}
