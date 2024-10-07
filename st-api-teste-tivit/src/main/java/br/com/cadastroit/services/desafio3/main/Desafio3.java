package br.com.cadastroit.services.desafio3.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cadastroit.services.api.services.DesafioService;
import br.com.cadastroit.services.desafio3.interfaces.MonthlyImpl;
import lombok.Getter;

@Getter
public class Desafio3 {

	private static DesafioService service = new DesafioService();
	

	public static void main(String[] args) {
		
		
    	Map<String, Map<String, List<Double>>> totalAnnualRevenue = new HashMap<String, Map<String,List<Double>>>();


		String[] mesesResumo = {"Janeiro"};
		
		String[] mesesCompleto = {
				"Janeiro", "Fevereiro", "Marco", "Abril", "Maio", "Junho", 
				"Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
		};
		

		for (String mes : mesesCompleto) /* ou mesesResumo para facilitar na conferencia do resulstado */ {

		service.processMonthlyRevenue(new MonthlyImpl(mes));

		}
		
		for (String mes : mesesCompleto) /* ou mesesResumo para facilitar na conferencia do resulstado */ {
			totalAnnualRevenue.putAll(service.processTotalRevenue(new MonthlyImpl(mes)));
		}
		

		service.processMaxMinMonthlyValue(totalAnnualRevenue);
		service.processDaysAverageAnnualMedia(totalAnnualRevenue);
	}
    
}
