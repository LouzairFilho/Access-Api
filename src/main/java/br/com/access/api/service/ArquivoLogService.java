package br.com.access.api.service;

import java.io.File;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.access.api.batch.CustomStepListener;
import br.com.access.api.model.ArquivoLog;
import br.com.access.api.repository.ArquivoLogRepository;
import br.com.access.api.util.UtilApi;

@Service
public class ArquivoLogService {

	@Autowired
	private UtilApi utilApi;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@Autowired
	private ArquivoLogRepository repository;

	@Autowired
	private CustomStepListener stepListener;

	public ArquivoLog salvar(ArquivoLog arquivoLog) {
		return repository.save(arquivoLog);
	}

	public ArquivoLog prepararArquivoLogPersistencia(MultipartFile file, String nomeArquivoRenomeado) {
		ArquivoLog arquivoLog = new ArquivoLog();
		arquivoLog.setNome(file.getOriginalFilename());
		arquivoLog.setNomeArquivoRenomeado(nomeArquivoRenomeado);
		arquivoLog.setEndereco(this.utilApi.getPathUpload().concat(nomeArquivoRenomeado));
		return arquivoLog;
	}

	
	private String antesDeFazerUpload(MultipartFile file) throws Exception  {
		if (file == null) {
			return "Arquivo é um campo obrigatório!";
		}
		String extensao = this.utilApi.getExtensao(file.getOriginalFilename());
		
		if (!extensao.equalsIgnoreCase("log")) {
			throw new Exception("O arquivo com formato incorreto, apenas com extenção [ .log ]!");
		}
		return utilApi.gerarIdentificadorAleatorio("_API_FILE_").concat(file.getOriginalFilename());
	}

	public ArquivoLog realizarProcessamentoArquivoLog(Long id) throws Exception {
		try {
			ArquivoLog arquivoLog = repository.findOne(id);
			if (utilApi.isPreenchimentoNuloOuVazio(arquivoLog)) {
				throw new Exception("O arquivo com ID informado não existe!");
			}

			final JobParameters jobParameter = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
					.addString("fileName", arquivoLog.getEndereco()).toJobParameters();

			JobExecution jobExecution = jobLauncher.run(job, jobParameter);
			System.out.println("JobExecution: " + jobExecution.getStatus());

			System.out.println("Batch is Running...");
			while (jobExecution.isRunning()) {
				System.out.println("...");
			}

			arquivoLog.setProcessado(Boolean.TRUE);
			arquivoLog.setQuantidade(this.stepListener.getQntRegistros());
			repository.save(arquivoLog);

			return arquivoLog;
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public String montarDadosArquivoParaUpload(MultipartFile file) throws Exception {
		String nomeArquivo = this.antesDeFazerUpload(file);
		this.utilApi.fazerUpload(file.getBytes(), this.utilApi.getPathUpload(), nomeArquivo);
		return nomeArquivo;
	}

	public Page<ArquivoLog> obterArquivosLogPaginados(Integer pagina, Integer total) {
		
		
		Pageable paginacao = new PageRequest(pagina.intValue() ,total.intValue(), new Sort(Sort.Direction.DESC, "id"));
		return repository.findAll(paginacao);
	}

	public void excluirArquivoLogPorId(Long id) throws Exception {
		try {
			ArquivoLog arquivoLog = this.repository.findOne(id);
			if (utilApi.isPreenchimentoNuloOuVazio(arquivoLog)) {
				throw new Exception("O arquivo informado não existe em nossa base de dados!");
			} else {
				if (arquivoLog.getProcessado()) {
					throw new Exception("O arquivo informado já foi processado!");
				}
			}
			this.excluirArquivoFisicamenteDaPastaDeUploads(arquivoLog);
			this.repository.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Houve uma falha ao excluir ArquivoLog: " + e.getMessage());
		}
	}

	private void excluirArquivoFisicamenteDaPastaDeUploads(ArquivoLog arquivoLog) {
		File file = new File(arquivoLog.getEndereco());
		if (file.exists()) {
			file.delete();
		}
	}

}
