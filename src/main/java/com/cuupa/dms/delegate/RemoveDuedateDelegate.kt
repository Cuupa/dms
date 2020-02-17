package com.cuupa.dms.delegate

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class RemoveDuedateDelegate : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        if (execution!!.hasVariable("dueDate")) {
            execution.setVariable("dueDate", null)
        }
    }
}