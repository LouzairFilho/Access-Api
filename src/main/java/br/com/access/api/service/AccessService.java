package br.com.access.api.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.access.api.model.Access;
import br.com.access.api.model.DTO.AccessDTO;
import br.com.access.api.model.DTO.DashboardDTO;
import br.com.access.api.model.filtro.AccessFiltro;
import br.com.access.api.repository.AccessRepository;
import br.com.access.api.util.UtilApi;

@Service
public class AccessService {

	@Autowired
	private AccessRepository repository;

	private UtilApi utilApi;

	public AccessService() {
	}

	@Autowired
	public AccessService(UtilApi utilApi) {
		this.utilApi = utilApi;
	}

	public Access salvar(AccessDTO access) throws Exception {
		try {
			this.validarDadosObrigatorios(toAccess(access));
			return repository.save(toAccess(access));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Houve uma falha ao gravar o Log: " + e.getMessage());
		}
	}

	
	private void validarDadosObrigatorios(Access access) throws Exception {
		if (utilApi.isPreenchimentoNuloOuVazio(access.getData())) {
			throw new Exception("O preenchimento do campo Data é obrigatório !");
		}
		if (utilApi.isPreenchimentoNuloOuVazio(access.getIp())) {
			throw new Exception("O preenchimento do campo Ip é obrigatório !");
		}
		if (utilApi.isPreenchimentoNuloOuVazio(access.getRequest())) {
			throw new Exception("O preenchimento do campo Request é obrigatório !");
		}
		if (utilApi.isPreenchimentoNuloOuVazio(access.getStatus())) {
			throw new Exception("O preenchimento do campo Status é obrigatório !");
		}
		if (utilApi.isPreenchimentoNuloOuVazio(access.getUserAgent())) {
			throw new Exception("O preenchimento do campo UserAgent é obrigatório !");
		}
	}

	public Page<Access> obterLogsPaginados(Integer pagina, Integer total) {
		Pageable paginacao = new PageRequest(pagina.intValue(), total.intValue(),
				new Sort(Sort.Direction.DESC, "data"));
		return repository.findAll(paginacao);

	}

	public List<DashboardDTO> obterInfDashboard() {
		List<DashboardDTO> listDashboardDTO = new ArrayList<DashboardDTO>();
		Pageable paginacao = new PageRequest(0, 100, new Sort(Sort.Direction.DESC, "data"));
		
		List<Access> resultFindRequests = repository.findRequestsForIpAndUserAgent();
		
		if (resultFindRequests != null && !resultFindRequests.isEmpty()) {
			
			resultFindRequests.forEach(r -> {
				DashboardDTO dbDto = new DashboardDTO(r.getIp(), r.getUserAgent(), r.getQuantidade());
				listDashboardDTO.add(dbDto);
			});
		}
		return listDashboardDTO;
	}

	public void excluirAccessPorId(Long id) throws Exception {
		try {
			Access logCat = this.repository.findOne(id);
			if (utilApi.isPreenchimentoNuloOuVazio(logCat)) {
				throw new Exception("O registro informado não existe em nossa base de dados!");
			}
			this.repository.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Houve uma falha ao excluir registro: " + e.getMessage());
		}
	}

	public Access obterPorId(Long id) throws Exception {
		Access access = this.repository.findOne(id);
		if (utilApi.isPreenchimentoNuloOuVazio(access)) {
			throw new Exception("Nenhum registro foi encontrado com ID informado!");
		}
		return access;
	}

	public Access obterPorLinha(String linha) {

		String[] campos = linha.split("\\|");
		Access access = new Access();
		try {
			access.setData(this.parse(campos[0]));
			access.setIp(campos[1]);
			access.setRequest(campos[2]);
			access.setStatus(Integer.valueOf(campos[3]));
			access.setUserAgent(campos[4]);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro linha: " + linha);
		}
		return access;
	}

	public Access toAccess(AccessDTO accessDTO) {

		Access access = new Access();
		if (accessDTO.getId() != null) {
			access.setId(accessDTO.getId());
		}
		System.out.println(accessDTO.getData());

		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		try {
			Date d = simpleDateFormat.parse(accessDTO.getData().substring(0, 10));

			access.setData(d);
			access.setIp(accessDTO.getIp());
			access.setRequest(accessDTO.getRequest());
			access.setStatus(accessDTO.getStatus());
			access.setUserAgent(accessDTO.getUserAgent());

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return access;
	}

	private Date parse(String strData) throws ParseException {
		String pattern = "yyyy-MM-dd HH:mm:ss";

		SimpleDateFormat dateFormat = new SimpleDateFormat();

		dateFormat.applyPattern(pattern);
		return dateFormat.parse(strData);
	}

	public Page<Access> filtrar(AccessFiltro accessFiltro, Integer pagina, Integer total) {

		Pageable paginacao = new PageRequest(pagina.intValue(), total.intValue(),
				new Sort(Sort.Direction.DESC, "data"));

		if (!utilApi.isPreenchimentoNuloOuVazio(accessFiltro.getIp()) && !utilApi.isPreenchimentoNuloOuVazio(accessFiltro.getUserAgent())) {
			return repository.filtrarIpAndUserAgent(accessFiltro.getIp() + "%", "%" + accessFiltro.getUserAgent() + "%",
					paginacao);
		}
		if (utilApi.isPreenchimentoNuloOuVazio(accessFiltro.getIp()) && !utilApi.isPreenchimentoNuloOuVazio(accessFiltro.getUserAgent())) {
			return repository.filtrarUserAgent(accessFiltro.getUserAgent() + "%", paginacao);
		}
		if (!utilApi.isPreenchimentoNuloOuVazio(accessFiltro.getIp()) && utilApi.isPreenchimentoNuloOuVazio(accessFiltro.getUserAgent())) {
			return ((AccessRepository) repository).filtrarIp(accessFiltro.getIp() + "%",paginacao);
		}
		
		return repository.filtrarIpOrUserAgent("%" + accessFiltro.getIp() + "%", "%" + accessFiltro.getUserAgent() + "%",
				paginacao);
	}

}
