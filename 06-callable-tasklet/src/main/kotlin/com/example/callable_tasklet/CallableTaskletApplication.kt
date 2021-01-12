package com.example.callable_tasklet

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.step.tasklet.CallableTaskletAdapter
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.util.concurrent.Callable

@EnableBatchProcessing
@SpringBootApplication
class CallableTaskletApplication {

    @Bean
    fun job1(jobBuilderFactory: JobBuilderFactory, step1: Step, step2: Step): Job {
        return jobBuilderFactory.get("basicjob")
            .start(step1)
            .next(step2)
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
    fun callableObject(): Callable<RepeatStatus> {
        return Callable<RepeatStatus> {
            println("=====================================別スレッド")
            println("HelloWorld")
            RepeatStatus.FINISHED
        }
    }

    @Bean
    fun step2(stepBuilderFactory: StepBuilderFactory): Step {
        return stepBuilderFactory.get("step2")
            .tasklet(tasklet2(null))
            .build()
    }

    @StepScope
    @Bean
    fun tasklet2(@Value("#{jobParameters['message']}") message: String?): MethodInvokingTaskletAdapter {
        val methodInvokingTaskletAdapter = MethodInvokingTaskletAdapter()
        methodInvokingTaskletAdapter.setTargetObject(service())
        // 文字列でメソッド名記述とか。。。orz
        methodInvokingTaskletAdapter.setTargetMethod("serviceMethod")

        println("----jobParameters['message']")
        println(message)
        println("----")
        methodInvokingTaskletAdapter.setArguments(arrayOf(message))
        return methodInvokingTaskletAdapter
    }

    @Bean
    fun service(): CustomService {
        return CustomService()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<CallableTaskletApplication>(*args)
        }
    }
}

class CustomService {
    fun serviceMethod(message: String) {
        println("==========================")
        println("service method's args ==> $message")
        println("==========================")
    }
}
