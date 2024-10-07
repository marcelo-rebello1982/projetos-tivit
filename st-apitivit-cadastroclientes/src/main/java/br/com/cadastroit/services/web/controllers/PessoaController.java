package br.com.cadastroit.services.web.controllers;


import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.com.cadastroit.services.api.domain.HealthCheck;
import br.com.cadastroit.services.api.domain.Pessoa;
import br.com.cadastroit.services.api.enums.DbLayerMessage;
import br.com.cadastroit.services.repositories.PessoaRepository;
import br.com.cadastroit.services.web.controllers.commons.ApiCadastroCommonsController;
import br.com.cadastroit.services.web.controllers.commons.CommonsValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/administracao/pessoa")
public class PessoaController extends ApiCadastroCommonsController {

	@Tag(name = "Pessoa", description = "Get status from API - Consulta Pessoa")
	@GetMapping("/status")
	public ResponseEntity<Object> status(@RequestParam(name = "status", required = false, defaultValue = "UP") String status) {

		try {
			return new ResponseEntity<>(HealthCheck.builder().status(status).maxId(pessoaRepository.maxId(entityManagerFactory)).build(),
					HttpStatus.OK);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body((String.format(ex.getMessage())));
		}
	}

	@Tag(name = "Pessoa", description = "Get maxId Pessoa")
	@GetMapping("/maxId")
	public ResponseEntity<Object> maxId() {

		try {
			Long id = pessoaRepository.maxId(entityManagerFactory);
			return ResponseEntity.ok(id);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body((String.format(ex.getMessage())));
		}
	}
	
	@Tag(name = "Pessoa", description = "Get Pessoa by Id")
	@GetMapping("/find/{id}")
	public ResponseEntity<Object> find(@PathVariable("id") Long id, @RequestHeader(value = "token", required = false) String token) {

		CommonsValidation commonsValidation = this.createCommonsValidation();
		
	
		try {
			Pessoa entity = pessoaRepository.findById(id, entityManagerFactory);
			return ResponseEntity.ok().body(pessoaMapper.toDto(entity));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Operation(summary = "Search all Pessoa")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the List of Pessoa", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Pessoa.class))) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Pessoa", description = "Get all records using pagination mechanism")
	@GetMapping("/all/{page}/{length}")
	public ResponseEntity<Object> all(@RequestHeader(value ="estadoId", required = false) Long estadoId, @RequestParam(required = false) Map<String, String> requestParams, @PathVariable("page") int page,
			@PathVariable("length") int length) {

		try {
			Long count = pessoaRepository.count(estadoId, null, entityManagerFactory);
			if (count > 0) {
				
				List<Pessoa> pessoas = pessoaRepository.findAll(estadoId, entityManagerFactory, requestParams, page, length);
				HttpHeaders headers = httpHeaders(count.toString());
				
				Boolean resumir = Boolean.valueOf(StringUtils.equalsAny("true", requestParams.get("resumir")));
				
				return ResponseEntity.ok()
				        .headers(headers)
				        .body(resumir ? pessoaMapper.toResumoDto(pessoas) : pessoaMapper.toDto(pessoas));
				
			}
			return ResponseEntity.ok().headers(httpHeaders(count.toString())).body(EMPTY_MSG);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}
	
	@Tag(name = "Pessoa", description = "Get Pessoa by UF")
	@GetMapping("/findUfByCriteria/{uf}")
	public ResponseEntity<Object> findUfByCriteria(@PathVariable("uf") String uf, @RequestHeader(value = "token", required = false) String token) {

		CommonsValidation commonsValidation = this.createCommonsValidation();
		
		// aqui poderia implementar o count semelhante ao path acima e também paginar.
		

		try {
			List<Pessoa> pessoas = pessoaRepository.findUfByCriteria(uf, entityManagerFactory);
			return ResponseEntity.ok().body(pessoaMapper.toDto(pessoas));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Tag(name = "Pessoa", description = "Get Pessoa by Id")
	@GetMapping("/findByQueryParam/{uf}")
	public ResponseEntity<Object> findByQueryParam(@PathVariable("uf") String  uf) {

		CommonsValidation commonsValidation = this.createCommonsValidation();
		
		try {
			List<Pessoa> entity = pessoaRepository.findUfByQueryParam(uf, entityManagerFactory);
			return ResponseEntity.ok().body(pessoaMapper.toDto(entity));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Tag(name = "Pessoa", description = "Get Pessoa by Id")
	@GetMapping("/findUfByJPQL/{page}/{length}")
	public ResponseEntity<Object> findUfByJPQL(@RequestHeader(value ="estadoId", required = false) Long estadoId, @PathVariable("page") int page, @PathVariable("length") int length,  @RequestParam(required = false) Map<String, String> requestParams) {

		CommonsValidation commonsValidation = this.createCommonsValidation();

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityManager managers = entityManagerFactory.createEntityManager();

		try {

			Long count = pessoaRepository.count(estadoId , requestParams, page, length, true, entityManager, managers);

			if (count > 0) {
				List<Pessoa> entity = pessoaRepository.findUfByJPQL(estadoId, requestParams,
                        page, length, entityManagerFactory);
				return ResponseEntity.ok().body(pessoaMapper.toDto(entity));
			}
			
			return ResponseEntity.ok().headers(this.httpHeaders(count.toString())).body(DbLayerMessage.EMPTY_MSG.message());
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	private CommonsValidation createCommonsValidation() {

		return CommonsValidation.builder()
				.pessoaRepository(new PessoaRepository())
				.build();
	}
	
	 public HttpHeaders httpHeaders(String count) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("summaryCount", count);
	        return headers;
	    }
}
