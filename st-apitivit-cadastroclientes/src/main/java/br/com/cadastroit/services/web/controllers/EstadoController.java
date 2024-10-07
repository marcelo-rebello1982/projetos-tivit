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

import br.com.cadastroit.services.api.domain.Estado;
import br.com.cadastroit.services.api.domain.Pais;
import br.com.cadastroit.services.exceptions.EstadoException;
import br.com.cadastroit.services.repositories.EstadoRepository;
import br.com.cadastroit.services.repositories.PessoaRepository;
import br.com.cadastroit.services.repositories.TelefoneRepository;
import br.com.cadastroit.services.web.controllers.commons.ApiCadastroCommonsController;
import br.com.cadastroit.services.web.controllers.commons.CommonsValidation;
import br.com.cadastroit.services.web.dto.EstadoDTO;
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
@RequestMapping("/administracao/estado")
public class EstadoController extends ApiCadastroCommonsController {

	@Operation(summary = "Get port off running this")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the Telefone", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PortaDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Tarefa not found", content = @Content) })
	@Tag(name = "Telefone", description = "Get a port number")
	@GetMapping("/porta")
	public ResponseEntity<Object> findPort(@Value("${local.server.port}") String porta) {

		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			return ResponseEntity.ok().body(PortaDTO.builder().descr("Requisição respondida pela instância na porta").numeroPorta(porta).build());
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Operation(summary = "Get a Estado by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the Estado", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Estado.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
			@ApiResponse(responseCode = "404", description = "Estado not found", content = @Content) })
	@Tag(name = "Estado", description = "Get a Estado record by id")
	@GetMapping("/find/{id}")
	public ResponseEntity<Object> find(@RequestHeader("paisId") Long paisId, @PathVariable("id") Long id) {
		
		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			Estado entity = this.estadoRepository.findById(paisId, id, entityManagerFactory);
			return ResponseEntity.ok().body(estadoMapper.toDto(entity));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}
	
	@Operation(summary = "Create Estado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Estado created with sucessful", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = EstadoDTO.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Telefone", description = "Create a Estado record")
	@PostMapping("/create")
	public ResponseEntity<Object> handlePost(@RequestHeader("paisId") Long paisId, @Validated @RequestBody EstadoDTO entityDto) {
		
		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			Pais entity = commonsValidation.recuperarPais(paisId, entityManagerFactory);
			return this.mountEntity(entityDto, paisId, entity, false);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));

		}
	}
	
	@Operation(summary = "Update Estado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Telefone updated with sucessful", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Estado.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Estado", description = "Updating a Estado record")
	@PutMapping("/update/{id}")
	public ResponseEntity<Object> handleUpdate(@RequestHeader("paisId") Long paisId, @PathVariable("id") Long id,
			@Validated @RequestBody EstadoDTO entityDto) {
		
		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			Pais entity = commonsValidation.recuperarPais(paisId, entityManagerFactory);
			return this.mountEntity(entityDto, id, entity, true);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));

		}
	}
	
	@Operation(summary = "Delete Estado By id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "No Content", description = "Update Estado", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Estado", description = "Deleting a Estado record")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> delete(@RequestHeader("empresaId") Long empresaId, @PathVariable("id") Long id)
			throws EstadoException {
		
		CommonsValidation commonsValidation = this.createCommonsValidation();

		try {
			estadoRepository.delete(estadoRepository.findById(id, entityManagerFactory));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(commonsValidation.createResponseTemplate(String.format(ex.getMessage())));
		}
	}

	@Tag(name = "Pessoa", description = "Get maxId Estado")
	@GetMapping("/maxId")
	public ResponseEntity<Object> maxId(@RequestHeader("paisId") Long paisId) {
		try {
			Long id = this.estadoRepository.maxId(entityManagerFactory, paisId);
			return ResponseEntity.ok(id);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Search all Estados")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the List of Estado", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Estado.class))) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Estado", description = "Get all records using pagination mechanism")
	@GetMapping("/all/{page}/{length}")
	public ResponseEntity<Object> all(@RequestHeader("paisId") Long paisId, @RequestParam(required = false) Map<String, String> requestParams, @PathVariable("page") int page,
			@PathVariable("length") int length) {

		try {
			Long count = estadoRepository.count(paisId, null, entityManagerFactory);
			if (count > 0) {
				List<Estado> estados = estadoRepository.findAll(paisId, entityManagerFactory, requestParams, page, length);
				HttpHeaders headers = httpHeaders(count.toString());
				return ResponseEntity.ok().headers(headers).body(estadoMapper.toDto(estados));
			}
			return ResponseEntity.ok().headers(httpHeaders(count.toString())).body(EMPTY_MSG);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

	@Operation(summary = "Search all Estados by filters")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the List of Departaments", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Estado.class))) }),
			@ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content) })
	@Tag(name = "Estados", description = "Get Estados by filters")
	@PostMapping("/findByFilters/{page}/{length}")
	public ResponseEntity<Object> findByFilters(@RequestHeader("paisId") Long paisId, @RequestParam(required = false) Map<String, String> requestParams, @PathVariable("page") int page, @PathVariable("length") int length, @RequestBody EstadoDTO entityDto) {

		try {
			Long count = estadoRepository.count(paisId, entityDto, entityManagerFactory);
			if (count > 0) {
				List<Estado> list = estadoRepository.findByFilters(paisId, entityDto, entityManagerFactory, requestParams, page, length);
				HttpHeaders headers = httpHeaders(count.toString());
				return ResponseEntity.ok().headers(headers).body(estadoMapper.toDto(list));
			}
			return ResponseEntity.ok().headers(httpHeaders(count.toString())).body(EMPTY_MSG);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private ResponseEntity<Object> mountEntity(EstadoDTO entityDto, Long id, Pais pais, boolean update) throws EstadoException {

		try {

			Estado entity = this.estadoMapper.toEntity(entityDto);

			HttpHeaders headers = new HttpHeaders();
			entity.setPais(pais);
			entity.setId(update ? id : null);
			entity = estadoRepository.save(entity);
			headers.add("Location", "/find/" + entity.getId());
			return new ResponseEntity<>(headers, update ? HttpStatus.NO_CONTENT : HttpStatus.CREATED);
		} catch (NoResultException ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			throw new EstadoException(ex.getMessage(), ex);
		}
	}
	
	private CommonsValidation createCommonsValidation() {

		return CommonsValidation.builder()
				.pessoaRepository(new PessoaRepository())
				.estadoRepository(new EstadoRepository())
				.build();
	}
}
