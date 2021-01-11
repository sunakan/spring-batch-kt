package com.example.helloworld2.jobs

import com.example.helloworld2.Helloworld2Application
import com.example.helloworld2.batch.HelloWorldTasklet
import com.example.helloworld2.batch.HelloWorldTasklet2
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableBatchProcessing
class HelloWorldJob {
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
            .tasklet(HelloWorldTasklet())
            .build()
    }
    @Bean
    fun step2(stepBuilderFactory: StepBuilderFactory): Step {
        return stepBuilderFactory.get("step2")
            .tasklet(tasklet(null))
            .build()
    }

    @StepScope
    @Bean
    fun tasklet(@Value("#{jobParameters['name']}") name: String?): Tasklet {
        return HelloWorldTasklet2(name = name)
    }

    // これは実行時にエラーになる
    //@StepScope
    //@Bean
    //fun step2(stepBuilderFactory: StepBuilderFactory, @Value("#{jobParameters['name']}") name: String?): Step {
    //    return stepBuilderFactory.get("step2")
    //            .tasklet(HelloWorldTasklet2(name = name))
    //            .build()
    //}
    //エラー内容
    // 2021-01-11 01:41:43.834  INFO 3202 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [step1] executed in 38ms
    // 2021-01-11 01:41:43.844 ERROR 3202 --- [           main] o.s.batch.core.job.AbstractJob           : Encountered fatal error executing job
    // org.springframework.beans.factory.support.ScopeNotActiveException: Error creating bean with name 'scopedTarget.step2': Scope 'step' is not acti$e for the current thread; consider defining a scoped proxy for this bean if you intend to refer to it from a singleton; nested exception is jav$.lang.IllegalStateException: No context holder available for step scope0
    // ...
    // Caused by: java.lang.IllegalStateException: No context holder available for step scope
    // ...
}
