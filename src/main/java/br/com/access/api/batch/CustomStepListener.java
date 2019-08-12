package br.com.access.api.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class CustomStepListener implements StepExecutionListener {

	private Long qntRegistros = 0L;

	@Override
	public void beforeStep(StepExecution stepExecution) {
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (stepExecution.getReadCount() > 0) {
			this.setQntRegistros(Long.valueOf(stepExecution.getWriteCount()));
		}
		return ExitStatus.COMPLETED;
	}

	public Long getQntRegistros() {
		return qntRegistros;
	}

	public void setQntRegistros(Long qntRegistros) {
		this.qntRegistros = qntRegistros;
	}

}
