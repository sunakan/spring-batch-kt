package com.example.callable_tasklet

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.step.tasklet.CallableTaskletAdapter
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.util.concurrent.Callable

@EnableBatchProcessing
@SpringBootApplication
class CallableTaskletApplication {

    @Bean
    fun job1(jobBuilderFactory: JobBuilderFactory, step1: Step): Job {
        return jobBuilderFactory.get("basicjob")
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
    fun tasklet1(): CallableTaskletAdapter {
        val callableTaskletAdapter = CallableTaskletAdapter()
        callableTaskletAdapter.setCallable(callableObject())
        return callableTaskletAdapter
    }

    @Bean
    fun callableObject(): Callable<RepeatStatus>  {
        return Callable<RepeatStatus> {
            println("=====================================別スレッド")
            println("HelloWorld")
            RepeatStatus.FINISHED
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<CallableTaskletApplication>(*args)
        }
    }
}
