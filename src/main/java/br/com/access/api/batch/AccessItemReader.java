package br.com.access.api.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.access.api.model.Access;
import br.com.access.api.service.AccessService;

@Component
@StepScope
public class AccessItemReader implements ItemReader<Access>, InitializingBean {

	private BufferedReader br;
	
	@Autowired
	private AccessService accessService;
	
	private File file;

	@Value("#{jobParameters[fileName]}")
	private String fileName;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.file = new File(fileName);
		this.br = new BufferedReader(new FileReader(file));
	}

	@Override
	public Access read()
			throws Exception, UnexpectedInputException, ParseException,
			NonTransientResourceException {
		try {
			String line;
			if ((line = br.readLine()) != null) {
				return accessService.obterPorLinha(line);
			}
		} catch (IOException e) {
			System.out.println("expected exception: {}" + e.getMessage());
		}
		this.br.close();
		this.file.delete();
		return null;
	}
	

}
