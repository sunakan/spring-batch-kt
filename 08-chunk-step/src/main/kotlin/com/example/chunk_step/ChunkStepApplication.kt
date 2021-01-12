package com.example.chunk_step

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.support.ListItemReader
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.util.UUID
import java.util.stream.IntStream
import kotlin.streams.toList

@EnableBatchProcessing
@SpringBootApplication
class ChunkStepApplication {

    @Bean
    fun job(jobBuilderFactory: JobBuilderFactory, step1: Step): Job {
        return jobBuilderFactory.get("basicjob")
            .incrementer(RunIdIncrementer())
            .start(step1)
            .build()
    }

    @Bean
    fun step1(stepBuilderFactory: StepBuilderFactory): Step {
        return stepBuilderFactory.get("step1")
            .chunk<String, String>(10)
            .reader(itemReader())
            .writer(itemWriter())
            .build()
    }

    @Bean
    fun itemReader(): ListItemReader<String> {
        val list = IntStream.range(1, 43)
            .mapToObj { _ -> UUID.randomUUID().toString() }
            .toList()
        return ListItemReader<String>(list)
    }

    @Bean
    fun itemWriter(): ItemWriter<String> {
        return ItemWriter { items: List<String> ->
            println("------------------------------")
            items.forEach(::println)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<ChunkStepApplication>(*args)
        }
    }
}
