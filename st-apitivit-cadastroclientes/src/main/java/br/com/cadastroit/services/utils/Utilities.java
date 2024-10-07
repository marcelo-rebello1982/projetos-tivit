package br.com.cadastroit.services.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
@Getter
public class Utilities {

	public static String createQuery(StringBuilder sBuilder, int length) {

		return new StringBuilder().append(sBuilder).append("(").append(String.join(",", Collections.nCopies(length, "?"))).append(")").toString();
	}

	public StringBuilder createQuery(Boolean rowCount, String fields, String table, Long estadoId , LinkedHashMap<String, String> campoValorCond) {

		StringBuilder sb = new StringBuilder();
		sb.append(rowCount ? "SELECT COUNT (*) FROM " : "SELECT ").append(fields).append(table).append(" P");
		if (!campoValorCond.isEmpty()) {
			String cond = campoValorCond.entrySet()
					.stream()
					.map(entry -> entry.getKey().toUpperCase() + entry.getValue())
					.collect(Collectors.joining(" AND ", estadoId != null ? " INNER JOIN ESTADO E ON E.ID = P.ESTADO_ID WHERE " : "" + " WHERE ", ""));
			sb.append(cond);
		}
		return sb;
	}

	public LinkedHashMap<String, String> createPredicates(boolean objectNotNull, Long estadoId ) {

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		//	if (objectNotNull && uf != null) {
		//
		//	this.putValuesInMap(map, uf, "P.UF = '");

		Optional.ofNullable(estadoId).ifPresent(estado -> {
			
			this.putValuesInMap(map, String.valueOf(estadoId), " P.ESTADO_ID = '");
			
		});

		return map;
	}
	
	public void putValuesInMap(Map<String, String> map, String key, String value) {

		Optional.ofNullable(key).ifPresent(v -> map.put(value, !value.contains("BETWEEN") ? v + "'" : v));
	}
}
