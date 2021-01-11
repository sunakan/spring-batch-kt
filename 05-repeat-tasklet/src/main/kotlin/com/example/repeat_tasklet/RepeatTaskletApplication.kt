package com.example.repeat_tasklet

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@EnableBatchProcessing
@SpringBootApplication
class RepeatTaskletApplication {

    @Bean
    fun job(jobBuilderFactory: JobBuilderFactory, step1: Step, step2: Step): Job {
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
    fun step2(stepBuilderFactory: StepBuilderFactory): Step {
        return stepBuilderFactory.get("step2")
            .tasklet(tasklet2())
            .build()
    }

    @Bean
    fun tasklet1(): Tasklet {
        return Tasklet { _, chunkContext ->
            val stepExecutionContext = chunkContext.stepContext
                .stepExecution
                .executionContext
            val count = when (stepExecutionContext["count"]) {
                is Int -> stepExecutionContext["count"] as Int
                else -> 0
            }
            println("---------------------------------------------------")
            println(count)
            println(stepExecutionContext)
            println("---------------------------------------------------")
            val ret = when {
                count < 5 -> { stepExecutionContext.put("count", count + 1); RepeatStatus.CONTINUABLE }
                else -> RepeatStatus.FINISHED
            }
            println("---------------------------------------------------After")
            println(stepExecutionContext)
            println("---------------------------------------------------After")
            println("==")
            println(ret)
            ret
        }
    }

    @Bean
    fun tasklet2(): Tasklet {
        return Tasklet { _, chunkContext ->
            println("終了!!")
            RepeatStatus.FINISHED
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<RepeatTaskletApplication>(*args)
        }
    }
}
