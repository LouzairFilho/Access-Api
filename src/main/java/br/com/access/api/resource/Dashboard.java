package br.com.access.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.access.api.service.AccessService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/access-api/dashboard")
public class Dashboard {

	@Autowired
	private AccessService accessService;
	
	@GetMapping(value = "/ip-useragente")
	public ResponseEntity<?> getIpPorUserAgent() {

		return new ResponseEntity<>(accessService.obterInfDashboard(), HttpStatus.OK);

	}
}
