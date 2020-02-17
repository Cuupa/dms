package com.cuupa.dms.service

import com.cuupa.dms.Constants
import com.cuupa.dms.controller.ProcessParameters
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.TaskService
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CamundaService(@Autowired val runtimeService: RuntimeService, @Autowired val taskService: TaskService) {


    fun getProcessesForOwner(owner: String): List<String> {
        if (owner.isNullOrBlank()) {
            return listOf()
        }
        val taskList = taskService.createTaskQuery().processDefinitionKey(Constants.OPENTASK_MODEL).processVariableValueEquals("owner", owner).active().list()
        val processInstanceIds = taskList.map { task -> task.processInstanceId }.toMutableSet()
        return if (processInstanceIds.isNotEmpty()) {
            val processInstances = runtimeService.createProcessInstanceQuery().processInstanceIds(processInstanceIds).list()
            processInstances.map { instance -> instance.processInstanceId }
        } else {
            listOf()
        }
    }

    fun startProcess(withDueDates: ProcessParameters): String? {
        val map: MutableMap<String, Any> = createMandatoryVariables()
        map.put("dueDate", getTimerDate(withDueDates))
        map.put("owner", withDueDates.getOwner())
        return runtimeService.startProcessInstanceByKey(Constants.OPENTASK_MODEL, map)!!.processInstanceId
    }

    private fun createMandatoryVariables(): MutableMap<String, Any> {
        return mutableMapOf(Pair("complete", false))
    }

    private fun getTimerDate(withDueDates: ProcessParameters): String {
        val localDate = LocalDate.parse(withDueDates.getDueDate(), DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        val localTime = LocalTime.of(8, 0)
        val localDateTime = LocalDateTime.of(localDate, localTime)
        return localDateTime.toString()
    }
}
