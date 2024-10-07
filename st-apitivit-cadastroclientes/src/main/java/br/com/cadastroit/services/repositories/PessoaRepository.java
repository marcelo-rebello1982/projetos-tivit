package br.com.cadastroit.services.repositories;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.cadastroit.services.api.domain.Estado;
import br.com.cadastroit.services.api.domain.Estado_;
import br.com.cadastroit.services.api.domain.Pessoa;
import br.com.cadastroit.services.api.domain.Pessoa_;
import br.com.cadastroit.services.api.domain.Telefone;
import br.com.cadastroit.services.api.domain.Telefone_;
import br.com.cadastroit.services.api.enums.DbLayerMessage;
import br.com.cadastroit.services.api.enums.Status;
import br.com.cadastroit.services.api.enums.TipoPessoa;
import br.com.cadastroit.services.exceptions.PessoaException;
import br.com.cadastroit.services.repositories.impl.PessoaRepositoryImpl;
import br.com.cadastroit.services.utils.UtilDate;
import br.com.cadastroit.services.utils.Utilities;
import br.com.cadastroit.services.web.dto.EnderecoDTO;
import br.com.cadastroit.services.web.dto.EstadoDTO;
import br.com.cadastroit.services.web.dto.PessoaDTO;
import br.com.cadastroit.services.web.dto.TelefoneDTO;
import br.com.cadastroit.services.web.mapper.PessoaMapper;
import lombok.NoArgsConstructor;

@Repository
@NoArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class PessoaRepository implements Serializable {

	private static final long serialVersionUID = -4076927646284930352L;
	
	private static final String MODE = "Error on %s mode to %s, [error] = %s";
	private static final String OBJECT = "PESSOA";
	private static final String ORDER = "order";
	
    private final Utilities utilities = Utilities.builder().build();
    private final UtilDate utilDate = UtilDate.builder().build();
    
    
     // toLocalDateTimeString
	
	@Autowired
	private PessoaRepositoryImpl pessoaRepositoryImpl;
	
	protected final PessoaMapper pessoaMapper = Mappers.getMapper(PessoaMapper.class);
	
	public Long maxId(EntityManagerFactory entityManagerFactory, String cd) throws SQLException {

		EntityManager em = entityManagerFactory.createEntityManager();
		Query query = em.createNativeQuery("select nvl(max(id),1) from " + OBJECT);
		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Long maxId(EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Pessoa> from = cq.from(Pessoa.class);
			TypedQuery<Long> result = em.createQuery(cq.select(cb.max(from.get(Pessoa_.id))));
			return result.getSingleResult();
		} catch (Exception ex) {
			throw new PessoaException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", null));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	@Transactional
	public Pessoa findById(Long id, EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Pessoa> cq = cb.createQuery(Pessoa.class);
			Root<Pessoa> from = cq.from(Pessoa.class);
			TypedQuery<Pessoa> tQuery = em.createQuery(cq.select(from).where(cb.equal(from.get(Pessoa_.id), id)));
			return tQuery.getSingleResult();
		} catch (NoResultException ex) {
			throw new PessoaException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", id));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public List<Pessoa> findUfByCriteria(String uf, EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Pessoa> cq = cb.createQuery(Pessoa.class);
			Root<Pessoa> from = cq.from(Pessoa.class);
			Join<Pessoa, Estado> joinEstado = from.join(Pessoa_.uf, JoinType.INNER);
			TypedQuery<Pessoa> tQuery = em.createQuery(cq.select(from).where(cb.equal(joinEstado.get(Estado_.sigla), uf)));
			return tQuery.getResultList();
		} catch (NoResultException ex) {
			throw new PessoaException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", uf));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<Pessoa> findUfByQueryParam(String sigla,  EntityManagerFactory entityManagerFactory) {
		
		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("SELECT p FROM Pessoa p ")
				.append(" LEFT JOIN FETCH p.uf u")
				.append(" WHERE u.sigla = :sigla ");
		
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query qry = entityManager.createQuery(sqlQuery.toString());
		qry.setParameter("sigla", sigla);
		qry.setMaxResults(100);

		List<Pessoa> list = qry.getResultList();
		return list;
		
	}
	
	public List<Pessoa> findUfByJPQL(Long estadoId , Map<String, String> requestParams, int page, int length, EntityManagerFactory entityManagerFactory) {
		
        AtomicReference<List<Pessoa>> pessoas = new AtomicReference<>(new ArrayList<>());
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        AtomicBoolean order = new AtomicBoolean(true);


        try {
        	
        requestParams.entrySet().stream()
        	.filter(entry -> entry.getKey()
        			.equalsIgnoreCase("ORDER"))
        	.forEach(entry -> {
        		if (entry.getValue() == null || entry.getValue().equals("desc")) {
        			order.set(true);
        		} else {
        			order.set(false);
        		}
        });
        	
      	StringBuilder toSelect = new StringBuilder();
      	toSelect.append("P.ID, P.NOME, P.RAZAOSOCIAL, P.STATUS, P.TIPOPESSOA, P.ESTADO_ID, P.UPDATEAT, ")
                     .append(" P.ENDERECO_ID, P.TELEFONE_ID FROM ");
      	
      	
		StringBuilder sqlQryString = this.utilities
				.createQuery(false, toSelect.toString(), "PESSOA", estadoId != null ? estadoId : null,
						this.utilities.createPredicates(estadoId != null ? true : false, estadoId))
							.append(" ORDER BY P.ID")
									.append(order.get() == true ? " DESC" : " ASC");

		Query query = entityManager.createNativeQuery(sqlQryString.toString());
		query.setFirstResult((page - 1) * length);
        query.setMaxResults(length);
           
		List<Object[]> result = query.getResultList();
		
		 result.stream() 
         
		 	.map( obj ->

                 PessoaDTO.builder()
                 		.id(Long.valueOf(obj[0].toString()))
                 		.nome(obj[1].toString())
                 		.razaosocial(obj[2].toString())
                 		.status(Status.valueOf(obj[3].toString()))
                 		.tipoPessoa(TipoPessoa.valueOf(obj[4].toString()))
                 		.uf(EstadoDTO.builder().id(Long.valueOf(obj[5].toString())).build())
                 		.endereco(EnderecoDTO.builder().id(Long.valueOf(obj[7].toString())).build())
                 		.telefone(TelefoneDTO.builder().id(Long.valueOf(obj[8].toString())).build())
                        .build())
		 							
		 					.forEach( obj ->
                        
 								pessoas.get().add(pessoaMapper.toEntity(obj)));

		 return pessoas.get();
		
        } catch (Exception ex) {
            throw new PessoaException(String.format(MODE, "PESSOA", OBJECT, ex.getMessage()));
        } finally {
        	entityManager.clear();
        	entityManager.close();
        }
	}
	
	private void createCondictions(StringBuilder condicoes, PessoaDTO pessoa) {

		if (pessoa.getNome() != null && !pessoa.getNome().equals("")) {
			condicoes.append(condicoes.length() == 0 ? " WHERE " : " AND ").append(" e.nome LIKE :nome ");
		}
		
	//	if (empresaSic.getCnpj() != null && !empresaSic.getCnpj().equals("")) {
	//		condicoes.append(condicoes.length() == 0 ? " WHERE " : " AND ").append("( e.cnpj LIKE :cnpj OR e.cpf LIKE :cpf )");
	//	}
	}
	 
	public LinkedHashSet<Pessoa> recuperarTodosRegistrosPaginados(PessoaDTO pessoa, EntityManagerFactory entityManagerFactory, Integer page, Integer length)
			throws PessoaException {

		LinkedHashSet<Pessoa> result = new LinkedHashSet<>();

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		StringBuilder condicoes = new StringBuilder();

		try {

			this.createCondictions(condicoes, pessoa);
			Query qry = entityManager.createQuery("SELECT e FROM pessoa p " + condicoes.toString() + " order by p.id desc");
			Query qryMax = entityManager.createQuery("SELECT COUNT(e) FROM Pessoa p " + condicoes.toString());

			this.definiParametro(qry, pessoa);
			this.definiParametro(qryMax, pessoa);

			qry.setFirstResult((page - 1) * length);
			qry.setMaxResults(length);

			// this.maxLength = Integer.parseInt(qryMax.getSingleResult().toString());

			List<Pessoa> empresas = qry.getResultList();
			result.addAll(empresas);
		} catch (Exception ex) {
			throw new PessoaException("Erro ao recuperar lista de dados {0}", ex);
		}
		return result;
	}

	private void definiParametro(Query qry, PessoaDTO pessoa) {

		if (pessoa.getNome() != null && !pessoa.getNome().equals("")) {
			qry.setParameter("nome", "%" + pessoa.getNome() + "%");
		}
		
		// if (pessoa.getCnpj() != null && !pessoa.getCnpj().equals("")) {
		//	qry.setParameter("cnpj", "%" + pessoa.getCnpj() + "%");
		//	qry.setParameter("cpf", "%" + pessoa.getCnpj() + "%");
		// }
	}
	
    public Long count(Long estadoId, Map<String, String> requestParams, int page, int length, boolean rowCount, EntityManager... managers) throws PessoaException {

        try {

            AtomicBoolean order = new AtomicBoolean(true);
            requestParams.entrySet().stream()
                    .filter(entry -> entry.getKey()
                            .equalsIgnoreCase("ORDER"))
                    .forEach(entry -> {
                        if (entry.getValue() == null || entry.getValue().equals("desc")) {
                            order.set(true);
                        } else {
                            order.set(false);
                        }
                    });
            
            StringBuilder sqlRowCount = this.utilities.createQuery(rowCount, "PESSOA", "", estadoId,
                    this.utilities.createPredicates(estadoId != null ? true : false, estadoId != null ? estadoId : null ));
            sqlRowCount.append(rowCount ? "" :
                            " ORDER BY P.ID ")
                    .append(rowCount ? ";" : order.get() == true
                            ? "DESC" : "ASC");

            Query tRows = managers[1].createNativeQuery(sqlRowCount.toString());
            return Long.valueOf(tRows.getSingleResult().toString());
        } catch (Exception ex) {
            throw new PessoaException(String.format(MODE, "PESSOA", OBJECT, ex.getMessage()));
        } finally {
        	if (managers[1].isOpen()) {
        		managers[1].clear();
        		managers[1].close();
        	}
        }
    }
    
    public Long count(Long estadoId, PessoaDTO entityDto, EntityManagerFactory entityManagerFactory) throws PessoaException {

		EntityManager em = entityManagerFactory.createEntityManager();
		List<Predicate> predicates = new ArrayList<>();

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Pessoa> from = cq.from(Pessoa.class);
			predicates = this.createPredicates(estadoId, entityDto != null ? entityDto : null, from, cb);

			return em.createQuery(cq.select(cb.count(from)).where(predicates.toArray(new Predicate[] {}))).getSingleResult();

		} catch (Exception ex) {
			throw new PessoaException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "COUNT", estadoId != null ? estadoId : 0));
		} finally {
			em.clear();
			em.close();
		}
	}
    
	public List<Pessoa> findAll(Long estadoId, EntityManagerFactory entityManagerFactory, Map<String, String> requestParams, int page, int length)
			throws PessoaException {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {

			List<Order> orderBy = new ArrayList<>();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Pessoa> cq = cb.createQuery(Pessoa.class);
			Root<Pessoa> from = cq.from(Pessoa.class);
			Join<Pessoa, Estado> joinEstado = from.join(Pessoa_.uf, JoinType.INNER);

			for (Entry<String, String> entry : requestParams.entrySet()) {
				if (entry.getKey().startsWith(ORDER)) {
					orderBy.add((entry.getValue() == null || entry.getValue().equals("desc") ? cb.desc(from.get(Pessoa_.id))
							: cb.asc(from.get(Pessoa_.id))));
				}
			}

			TypedQuery<Pessoa> tQuery;
			
			if (estadoId != null) 
				tQuery = em.createQuery(cq.select(from).where(cb.equal(joinEstado.get(Estado_.id), estadoId)).orderBy(orderBy)); 
			tQuery = em.createQuery(cq.select(from).orderBy(orderBy));
			
			tQuery.setFirstResult((page - 1) * length);
			tQuery.setMaxResults(length);
			return tQuery.getResultList();

		} catch (Exception ex) {
			throw new PessoaException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", estadoId));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public List<Predicate> createPredicates(Long estadoId, PessoaDTO entityDto, Root<Pessoa> from, CriteriaBuilder cb) {

		List<Predicate> predicates = new ArrayList<>();

		// aqui eu poderia fazer um join filtrando por Telefone de uma determinada pessoa somente.

		if (estadoId != null && estadoId > 0L) {
			Join<Pessoa, Estado> joinEstado = from.join(Pessoa_.uf, JoinType.INNER);
			predicates.add(cb.equal(joinEstado.get(Estado_.id), estadoId));
		}

	    if (entityDto != null) {
	    	
	    	checkIsNull(entityDto.getNome()).ifPresent(field -> predicates.add(cb.like(from.get(Pessoa_.nome), "%" + field + "%")));
	    	
	    }
	 	    

		// if (entityDto.getDataInicio() != null) {
		// predicates.add(cb.greaterThanOrEqualTo(joinTarefa.get(Tarefa_.dataInicio), entityDto.getDataInicio()));
		//
		// }

		// if (entityDto.getDataFinal() != null) {
		// predicates.add(cb.lessThanOrEqualTo(joinTarefa.get(Tarefa_.dataFinal), entityDto.getDataFinal()));
		// }

		return predicates;
	}

    
	public Optional<Pessoa> findById(Long id) {

		return pessoaRepositoryImpl.findById(id);
	}

	public <S extends Pessoa> S save(S entity) {

		return pessoaRepositoryImpl.save(entity);
	}

	public void delete(Pessoa entity) {

		pessoaRepositoryImpl.delete(entity);
	}

	public <T> Optional<T> checkIsNull(T field) {

		return Optional.ofNullable(field);
	}

}
