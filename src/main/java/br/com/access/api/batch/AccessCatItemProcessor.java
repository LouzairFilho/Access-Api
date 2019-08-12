

package br.com.access.api.batch;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import br.com.access.api.model.Access;

@Component
@StepScope
public class AccessCatItemProcessor implements ItemProcessor<Access, Access>{

	@Override
	public Access process(Access item) throws Exception {

		return item;
	}

}
