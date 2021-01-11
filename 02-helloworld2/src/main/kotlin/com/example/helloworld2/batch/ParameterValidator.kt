package com.example.helloworld2.batch

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.core.JobParametersValidator
import org.springframework.util.StringUtils
import kotlin.Throws

class ParameterValidator : JobParametersValidator {
    @Throws(JobParametersInvalidException::class)
    override fun validate(parameters: JobParameters?) {
        val name = parameters!!.getString("name")
        if (!StringUtils.hasText(name)) {
            throw JobParametersInvalidException("name parameter is missing")
        }
    }
}
