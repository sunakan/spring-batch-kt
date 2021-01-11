package com.example.incrementing_job_parameter.jobs

import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
//import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
@EnableBatchProcessing
class MainJob {
    @Bean
    fun job(jobBuilderFactory: JobBuilderFactory, step1: Step): Job {
        return jobBuilderFactory.get("basicjob")
            //.incrementer(RunIdIncrementer())
            .incrementer(dateStampIncrementer())
            .start(step1)
            .build()
    }

    @Bean
    fun step1(stepBuilderFactory: StepBuilderFactory): Step {
        return stepBuilderFactory.get("step1")
            .tasklet(tasklet1())
            .build()
    }

    @Bean
    fun tasklet1(): Tasklet {
        return Tasklet { _, _ ->
            println("-------------------------------------")
            println("HelloWorld")
            println("-------------------------------------")
            RepeatStatus.FINISHED
        }
    }

    @Bean
    fun dateStampIncrementer(): JobParametersIncrementer {
        return JobParametersIncrementer { parameters ->
            println("-------------------parametersの中身")
            println(parameters)
            println("-------------------")
            when (parameters) {
                is JobParameters -> JobParametersBuilder(parameters)
                else -> JobParametersBuilder()
            }.addDate("currentDate", Date())
            .toJobParameters()
        }
    }
}
