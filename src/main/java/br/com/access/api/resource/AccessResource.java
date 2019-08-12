package br.com.access.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.access.api.model.Access;
import br.com.access.api.model.DTO.AccessDTO;
import br.com.access.api.model.filtro.AccessFiltro;
import br.com.access.api.service.AccessService;
import br.com.access.api.util.RetornoApi;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/access-api/accesss")
public class AccessResource {

	@Autowired
	private AccessService accessService;
	
	
	@PostMapping(value="/save",consumes = "application/json")
	public ResponseEntity<?> salvar(@RequestBody AccessDTO access) throws InterruptedException {
		String excecao = "";
		try {
			
			accessService.salvar(access);
			return new ResponseEntity<>(new RetornoApi(null, 200, "Log de Acesso salvo com Sucesso!"), HttpStatus.OK);
		} catch (Exception e) {
			excecao = e.getMessage();
			e.printStackTrace();
		}
		
		return new ResponseEntity<>( "Erro ao salvar"+excecao, HttpStatus.BAD_REQUEST);
		

	}
	
	
	
	@GetMapping()
	public ResponseEntity<?> listarTodosArquivos(Pageable pageable) {

		return new ResponseEntity<>(accessService.obterLogsPaginados(pageable.getPageNumber(), pageable.getPageSize()), HttpStatus.OK);

	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deletar(@PathVariable(value = "id") Long id) {
		String excecao = "";
		try {
			accessService.excluirAccessPorId(id);
			return new ResponseEntity<>(new RetornoApi(null, 204, "Log deletado com Sucesso!"), HttpStatus.OK);
		} catch (Exception e) {
			excecao = e.getMessage();
			e.printStackTrace();
		}
		return new ResponseEntity<>( excecao, HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping(value="/filtro")
	public ResponseEntity<?> filtrar(Pageable pageable,@RequestBody AccessFiltro accessFiltro) throws InterruptedException {
		Page<Access> list = accessService.filtrar(accessFiltro, pageable.getPageNumber(), pageable.getPageSize());
		
		
		return new ResponseEntity<>(new RetornoApi(list, 200, "Pesquisa realizada com Sucesso!"), HttpStatus.OK);
		

	}
	
	
}
