package com.example.execution_context

import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.listener.ExecutionContextPromotionListener
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.config.Task

@EnableBatchProcessing
@SpringBootApplication
class ExecutionContextApplication {

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
            .listener(promotionListener())
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
            val name = when(chunkContext.stepContext.jobParameters["name"]) {
                is String -> chunkContext.stepContext.jobParameters["name"]
                else -> "名前無し"
            }
            // jobExecutionContext
            val jobExecutionContext = chunkContext.stepContext
                .stepExecution
                .jobExecution
                .executionContext
            jobExecutionContext.put("user.name", name)
            println("---------------------------------------------------")
            println(jobExecutionContext)
            println("---------------------------------------------------")
            RepeatStatus.FINISHED
        }
    }

    @Bean
    fun tasklet2(): Tasklet {
        return Tasklet { _, chunkContext ->
            val jobExecutionContext = chunkContext.stepContext
                .stepExecution
                .jobExecution
                .executionContext
            println("step間で情報を共有：ただし、そもそも良くないことなので気をつける")
            println("---------------------------------------------------Step2のContextの中身")
            println(jobExecutionContext)
            println("---------------------------------------------------")
            RepeatStatus.FINISHED
        }
    }

    @Bean
    fun promotionListener(): StepExecutionListener {
        val listener = ExecutionContextPromotionListener()
        listener.setKeys(arrayOf("name"))
        return listener
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ExecutionContextApplication::class.java, *args)
        }
    }
}
