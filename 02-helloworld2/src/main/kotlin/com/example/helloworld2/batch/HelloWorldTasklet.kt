package com.example.helloworld2.batch

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

class HelloWorldTasklet : Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        println("-------------------------------------------------HelloWorld")
        println("-------------------------------------------------ChunkContextからjobParameter参照")
        val name = chunkContext.stepContext.jobParameters.get("name")
        println("jobParameters['name']の中身は '$name'")
        return RepeatStatus.FINISHED
    }
}
