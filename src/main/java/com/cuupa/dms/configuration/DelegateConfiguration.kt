package com.cuupa.dms.configuration

import com.cuupa.dms.delegate.RemoveDuedateDelegate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class DelegateConfiguration {

    @Bean(name = ["RemoveDuedateDelegate"])
    open fun removeDuedateDelegate(): RemoveDuedateDelegate {
        return RemoveDuedateDelegate()
    }
}