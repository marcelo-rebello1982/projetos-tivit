package br.com.cadastroit.services.web.controllers;


import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.cadastroit.services.api.domain.Telefone;
import br.com.cadastroit.services.api.domain.Pessoa;
import br.com.cadastroit.services.exceptions.TelefoneException;
import br.com.cadastroit.services.repositories.TelefoneRepository;
import br.com.cadastroit.services.repositories.EstadoRepository;
import br.com.cadastroit.services.repositories.PessoaRepository;
import br.com.cadastroit.services.web.controllers.commons.ApiCadastroCommonsController;
import br.com.cadastroit.services.web.controllers.commons.CommonsValidation;
import br.com.cadastroit.services.web.dto.TelefoneDTO;
import br.com.cadastroit.services.web.dto.PortaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/administracao/telefone")
public class TelefoneController extends ApiCadastroCommonsController {

	@Operation(summary = "Get port off running this")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the Order", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PortaDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Tarefa not found", content = @Content) })
	@Tag(name = "Order", description = "Get a port number")
	@GetMapping("/porta")
	public ResponseEntity<Object> findPort(@Value("${local.server.port}") String porta) {

		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			return ResponseEntity.ok().body(PortaDTO.builder().descr("Requisição respondida pela instância na porta").numeroPorta(porta).build());
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Operation(summary = "Get a Telefone by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the Telefone", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Telefone.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Telefone not found", content = @Content) })
	@Tag(name = "Telefone", description = "Get a Telefone record by id")
	@GetMapping("/find/{id}")
	public ResponseEntity<Object> find(@RequestHeader("pessoaId") Long pessoaId, @PathVariable("id") Long id) {
		
		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			Telefone entity = this.telefoneRepository.findById(pessoaId, id, entityManagerFactory);
			return ResponseEntity.ok().body(telefoneMapper.toDto(entity));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Operation(summary = "Create Telefone")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Telefone created with sucessful", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = TelefoneDTO.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Order", description = "Create a Telefone record")
	@PostMapping("/create")
	public ResponseEntity<Object> handlePost(@RequestHeader("pessoaId") Long pessoaId, @Validated @RequestBody TelefoneDTO entityDto) {
		
		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			Pessoa entity = commonsValidation.recuperarPessoa(pessoaId, entityManagerFactory);
			return this.mountEntity(entityDto, pessoaId, entity, false);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));

		}
	}
	
	@Operation(summary = "Update Telefone")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Order updated with sucessful", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Telefone.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Telefone", description = "Updating a Telefone record")
	@PutMapping("/update/{id}")
	public ResponseEntity<Object> handleUpdate(@RequestHeader("pessoaId") Long pessoaId, @PathVariable("id") Long id,
			@Validated @RequestBody TelefoneDTO entityDto) {
		
		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			Pessoa entity = commonsValidation.recuperarPessoa(pessoaId, entityManagerFactory);
			return this.mountEntity(entityDto, id, entity, true);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));

		}
	}
	
	@Operation(summary = "Delete Telefone By id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "No Content", description = "Update Telefone", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Telefone", description = "Deleting a Telefone record")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> delete(@RequestHeader("empresaId") Long empresaId, @PathVariable("id") Long id)
			throws TelefoneException {
		
		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			telefoneRepository.delete(telefoneRepository.findById(id, entityManagerFactory));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}

	@Tag(name = "Pessoa", description = "Get maxId Telefone")
	@GetMapping("/maxId")
	public ResponseEntity<Object> maxId(@RequestHeader("pessoaId") Long pessoaId) {
		try {
			Long id = this.telefoneRepository.maxId(entityManagerFactory, pessoaId);
			return ResponseEntity.ok(id);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Search all Telefones")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the List of Telefone", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Telefone.class))) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Telefone", description = "Get all records using pagination mechanism")
	@GetMapping("/all/{page}/{length}")
	public ResponseEntity<Object> all(@RequestHeader("pessoaId") Long pessoaId, @RequestParam(required = false) Map<String, String> requestParams, @PathVariable("page") int page,
			@PathVariable("length") int length) {

		try {
			Long count = telefoneRepository.count(pessoaId, null, entityManagerFactory);
			if (count > 0) {
				List<Telefone> telefones = telefoneRepository.findAll(pessoaId, entityManagerFactory, requestParams, page, length);
				HttpHeaders headers = httpHeaders(count.toString());
				return ResponseEntity.ok().headers(headers).body(telefoneMapper.toDto(telefones));
			}
			return ResponseEntity.ok().headers(httpHeaders(count.toString())).body(EMPTY_MSG);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

	@Operation(summary = "Search all Telefones by filters")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the List of Departaments", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Telefone.class))) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Telefones", description = "Get Telefones by filters")
	@PostMapping("/findByFilters/{page}/{length}")
	public ResponseEntity<Object> findByFilters(@RequestHeader("pessoaId") Long pessoaId, @RequestParam(required = false) Map<String, String> requestParams, @PathVariable("page") int page, @PathVariable("length") int length, @RequestBody TelefoneDTO entityDto) {

		try {
			Long count = telefoneRepository.count(pessoaId, entityDto, entityManagerFactory);
			if (count > 0) {
				List<Telefone> list = telefoneRepository.findByFilters(pessoaId, entityDto, entityManagerFactory, requestParams, page, length);
				HttpHeaders headers = httpHeaders(count.toString());
				return ResponseEntity.ok().headers(headers).body(telefoneMapper.toDto(list));
			}
			return ResponseEntity.ok().headers(httpHeaders(count.toString())).body(EMPTY_MSG);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private ResponseEntity<Object> mountEntity(TelefoneDTO entityDto, Long id, Pessoa pessoa, boolean update) throws TelefoneException {

		try {

			Telefone entity = this.telefoneMapper.toEntity(entityDto);

			HttpHeaders headers = new HttpHeaders();
			entity.setPessoa(pessoa);
			entity.setId(update ? id : null);
			entity = telefoneRepository.save(entity);
			headers.add("Location", "/find/" + entity.getId());
			return new ResponseEntity<>(headers, update ? HttpStatus.NO_CONTENT : HttpStatus.CREATED);
		} catch (NoResultException ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			throw new TelefoneException(ex.getMessage(), ex);
		}
	}
	
	private CommonsValidation createCommonsValidation() {

		return CommonsValidation.builder()
				.pessoaRepository(new PessoaRepository())
				.telefoneRepository(new TelefoneRepository())
				.build();
	}
}
