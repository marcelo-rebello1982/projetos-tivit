package br.com.cadastroit.services.web.controllers.commons;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.cadastroit.services.api.enums.DbLayerMessage;
import br.com.cadastroit.services.repositories.EstadoRepository;
import br.com.cadastroit.services.repositories.PessoaRepository;
import br.com.cadastroit.services.repositories.TelefoneRepository;
import br.com.cadastroit.services.web.mapper.EstadoMapper;
import br.com.cadastroit.services.web.mapper.PessoaMapper;
import br.com.cadastroit.services.web.mapper.TelefoneMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ApiCadastroCommonsController {

	@Autowired
	public EntityManagerFactory entityManagerFactory;

	@Autowired
	protected ObjectMapper mapperJson;
	
	protected String EMPTY_MSG = "List is empty...";

	@Autowired
	protected PessoaRepository pessoaRepository;

	@Autowired
	protected EstadoRepository estadoRepository;

	@Autowired
	protected TelefoneRepository telefoneRepository;
	
	protected PessoaMapper pessoaMapper = Mappers.getMapper(PessoaMapper.class);

	protected final EstadoMapper estadoMapper = Mappers.getMapper(EstadoMapper.class);
	
	protected final TelefoneMapper telefoneMapper = Mappers.getMapper(TelefoneMapper.class);

	protected ResponseEntity<Object> validarCabecalho(String uuid, String token) {

		boolean valido = (uuid != null && !uuid.equals("")) && (token != null && !token.equals(""));

		return !valido ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(DbLayerMessage.EMPTY_MSG.message()) : null;
	}

	protected ResponseEntity<Object> validarCollection(List<?> collection) {
		return collection.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(DbLayerMessage.EMPTY_MSG.message())
				: ResponseEntity.status(HttpStatus.OK).body(collection);
	}

	protected <T, D> D mountObject(Function<T, D> mapper, T entity) {
		return mapper.apply(entity);
	}

	protected <T, R> List<R> mountObject(Function<T, R> mapper, List<T> list) {
		return list.stream().map(mapper).collect(Collectors.toList());
	}

	public boolean validateStatusTarefa(BigDecimal status) {
		return new HashSet<>(Arrays.asList(1L)).contains(status.longValue());
	}
	
	public <T> T convertFromJson(String json, Class<T> clazz) throws JsonProcessingException {
		mapperJson.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapperJson.readValue(json, clazz);
	}
	
	public Object parserJson(String json, Class<?> clazz) throws IOException {
		mapperJson.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
		ObjectReader ow = mapperJson.reader();
		JsonParser jsonParser = null;
		jsonParser = ow.createParser(json);
		jsonParser.close();
		return jsonParser.readValueAs(clazz);
	}
	
	public String writeJsonBase64(Object value) throws JsonProcessingException {
		mapperJson.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapperJson.writer().withDefaultPrettyPrinter();
		String jsonDto = ow.writeValueAsString(value);
		String base64 = Base64.getEncoder().encodeToString(jsonDto.getBytes());
		return base64;
	}
	
	protected HttpHeaders httpHeaders(String count) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("summaryCount", count);
		return headers;
	}
	
	protected HttpHeaders httpHeaders(String count, Long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("summaryCount", count);
		headers.add("pessoaId", String.valueOf(id));
		return headers;
	}
}
