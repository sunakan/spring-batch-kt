package com.example.helloworld

import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport

class JobListener : JobExecutionListenerSupport() {
    override fun beforeJob(jobExecution: JobExecution) {
        super.beforeJob(jobExecution)
        println("======================================================ジョブ開始")
    }

    override fun afterJob(jobExecution: JobExecution) {
        super.afterJob(jobExecution)
        println("======================================================ジョブ終了")
    }
}
