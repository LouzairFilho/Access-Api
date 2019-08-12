package br.com.access.api;

import java.io.FileNotFoundException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import br.com.access.api.batch.CustomStepListener;
import br.com.access.api.model.Access;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	private final JobBuilderFactory jobs;

	@Autowired
	private final StepBuilderFactory steps;

	public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobs = jobBuilderFactory;
		this.steps = stepBuilderFactory;
	}

	@Bean
	public Job importRegisterJob(final Step myStep) {
		return jobs.get("importRegisterJob").incrementer(new RunIdIncrementer()).flow(myStep).end().build();
	}

	@Bean
	public Step myStep(final ItemReader<Access> itemReader, final ItemWriter<Access> itemWriter,
			final ItemProcessor<Access, Access> itemProcessor, final TaskExecutor myExecutor,
			final CustomStepListener customStepListener) {
		return steps.get("importBillingLineJob_step1").<Access, Access>chunk(300).reader(itemReader)
				.processor(itemProcessor).writer(itemWriter).faultTolerant().skipLimit(10).skip(Exception.class)
				.noSkip(FileNotFoundException.class).listener(customStepListener).taskExecutor(myExecutor).build();
	}

	@Bean
	public TaskExecutor myExecutor() {
		final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(10);
		return taskExecutor;
	}

}
