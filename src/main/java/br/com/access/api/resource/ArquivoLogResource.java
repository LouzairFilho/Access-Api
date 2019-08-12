package br.com.access.api.resource;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.access.api.model.ArquivoLog;
import br.com.access.api.service.ArquivoLogService;
import br.com.access.api.util.RetornoApi;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/access-api/arquivos")
public class ArquivoLogResource {

	@Autowired
	private ArquivoLogService arquivoLogService;

	@GetMapping(value = "/processar/{id}")
	public ResponseEntity<?> realizarProcessamentoArquivoLog(@PathVariable(value = "id") Long id)
			throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
			JobParametersInvalidException {

		try {
			ArquivoLog arquivo = this.arquivoLogService.realizarProcessamentoArquivoLog(id);
			
			return new ResponseEntity<>(new RetornoApi(arquivo, 200, "Processamento realizado com sucesso!"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Houve uma falha ao realizar processamento do arquivo! " + e.getMessage(),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
		String excecao = "";
		try {
			String nomeArquivo = this.arquivoLogService.montarDadosArquivoParaUpload(file);
			ArquivoLog arquivoLog = this.arquivoLogService.prepararArquivoLogPersistencia(file, nomeArquivo);

			arquivoLog = arquivoLogService.salvar(arquivoLog);
			return new ResponseEntity<>(new RetornoApi(null, 200, "Upload Realizado com Sucesso!"), HttpStatus.OK);
		} catch (Exception e) {
			excecao = e.getMessage();
			e.printStackTrace();
		}
		return new ResponseEntity<>("Houve uma falha ao realizar upload: " + excecao, HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping()
	public ResponseEntity<?> listarTodosArquivos(Pageable pageable) {

		return new ResponseEntity<>(arquivoLogService.obterArquivosLogPaginados(pageable.getPageNumber(), pageable.getPageSize()), HttpStatus.OK);
		

	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deletar(@PathVariable(value = "id") Long id) {
		String excecao = "";
		try {
			arquivoLogService.excluirArquivoLogPorId(id);
			return new ResponseEntity<>(new RetornoApi(null, 204, "Arquivo Deletado com Sucesso!"), HttpStatus.OK);
		} catch (Exception e) {
			excecao = e.getMessage();
			e.printStackTrace();
		}
		return new ResponseEntity<>( excecao, HttpStatus.BAD_REQUEST);
	}
}
