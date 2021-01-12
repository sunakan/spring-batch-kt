package com.example.system_command_tasklet

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.SystemCommandTasklet
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@EnableBatchProcessing
@SpringBootApplication
class SystemCommandTaskletApplication {

    @Bean
    fun job(jobBuilderFactory: JobBuilderFactory, step1: Step, step2: Step): Job {
        return jobBuilderFactory.get("basicjob")
            .incrementer(RunIdIncrementer())
            .start(step1)
            .start(step2)
            .build()
    }

    @Bean
    fun step1(stepBuilderFactory: StepBuilderFactory): Step {
        return stepBuilderFactory.get("step1")
            .tasklet(tasklet1())
            .build()
    }

    @Bean
    fun tasklet1(): SystemCommandTasklet {
        val systemCommandTasklet = SystemCommandTasklet()
        // うまくいかない
        //systemCommandTasklet.setCommand(" echo '----------------' && pwd > hogehogehoge.txt && echo '----------------'")
        systemCommandTasklet.setCommand("touch hogehogehoge.txt")
        systemCommandTasklet.setTimeout(5000)
        systemCommandTasklet.setInterruptOnCancel(true)
        systemCommandTasklet.setWorkingDirectory("/tmp/")
        return systemCommandTasklet
    }

    @Bean
    fun step2(stepBuilderFactory: StepBuilderFactory): Step {
        return stepBuilderFactory.get("step2")
            .tasklet(tasklet2())
            .build()
    }

    @Bean
    fun tasklet2(): Tasklet {
        return Tasklet { _, chunkContext ->
            println("======================Hello")
            println(chunkContext)
            println("======================Hello")
            RepeatStatus.FINISHED
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<SystemCommandTaskletApplication>(*args)
        }
    }
}
