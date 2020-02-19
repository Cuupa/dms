package com.cuupa.dms.ui.dialog

interface DialogListener {

    fun consumeEvent(event: ConfirmDoneDialog.Event)
}
