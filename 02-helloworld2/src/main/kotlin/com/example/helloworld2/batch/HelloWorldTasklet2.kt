package com.example.helloworld2.batch

import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

class HelloWorldTasklet2(val name: String?) : Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        println("=================name")
        println(name)
        println("=================")
        return RepeatStatus.FINISHED
    }
}
