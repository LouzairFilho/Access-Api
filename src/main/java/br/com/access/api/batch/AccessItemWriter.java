package br.com.access.api.batch;

import java.util.List;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.access.api.model.Access;
import br.com.access.api.repository.AccessRepository;

@Component
@StepScope
public class AccessItemWriter implements ItemWriter<Access>{

	@Autowired
	private AccessRepository AccessRepository;
	
	@Override
	public void write(List<? extends Access> items) throws Exception {
		items.forEach(item -> {
			AccessRepository.save(item);
		});
	}


}
