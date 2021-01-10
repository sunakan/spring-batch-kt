package com.example.helloworld

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableBatchProcessing
class BatchConfiguration {
    @Bean
    fun job(jobBuilderFactory: JobBuilderFactory, step1: Step, step2: Step): Job {
        println("-------------------Hello Job")
        return jobBuilderFactory.get("job")
            .listener(JobListener())
            .start(step1)
            .next(step2)
            .build()
    }

    @Bean
    fun step1(stepBuilderFactory: StepBuilderFactory): Step {
        println("-------------------Hello Step 1")
        return stepBuilderFactory.get("step")
            .tasklet(HelloworldTasklet())
            .build()
    }

    @Bean
    fun step2(stepBuilderFactory: StepBuilderFactory): Step {
        println("-------------------Hello Step 2")
        return stepBuilderFactory.get("step")
            .tasklet(HelloworldTasklet())
            .build()
    }
}
