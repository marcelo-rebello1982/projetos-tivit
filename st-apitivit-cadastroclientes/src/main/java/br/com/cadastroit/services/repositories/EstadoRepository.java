package br.com.cadastroit.services.repositories;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.cadastroit.services.api.domain.Estado;
import br.com.cadastroit.services.api.domain.Estado_;
import br.com.cadastroit.services.api.domain.Pais;
import br.com.cadastroit.services.api.domain.Pais_;
import br.com.cadastroit.services.api.enums.DbLayerMessage;
import br.com.cadastroit.services.exceptions.EstadoException;
import br.com.cadastroit.services.exceptions.PessoaException;
import br.com.cadastroit.services.repositories.impl.EstadoRepositoryImpl;
import br.com.cadastroit.services.web.dto.EstadoDTO;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class EstadoRepository implements Serializable {

	private static final long serialVersionUID = 5261840790088760980L;
	
	private static final String MODE = "Error on %s mode to %s, [error] = %s";
	private static final String OBJECT = "ESTADO";
	private static final String ORDER = "order";
	
	@Autowired
	private EstadoRepositoryImpl estadoRepositoryImpl;

	public Long maxId(EntityManagerFactory entityManagerFactory) throws EstadoException {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Estado> from = cq.from(Estado.class);
			TypedQuery<Long> tQuery = em
					.createQuery(cq.select(cb.max(from.get(Estado_.id))));
			return tQuery.getSingleResult();
		} catch (Exception ex) {
			throw new EstadoException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", ""));
		} finally {
			em.clear();
			em.close();
		}
	}

	public Estado findById(Long id, EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Estado> cq = cb.createQuery(Estado.class);
			Root<Estado> from = cq.from(Estado.class);
			TypedQuery<Estado> tQuery = em.createQuery(cq.select(from).where(cb.equal(from.get(Estado_.id), id)));
			return tQuery.getSingleResult();
		} catch (NoResultException ex) {
			throw new EstadoException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", id));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public Estado findById(Long paisId, Long id, EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Estado> cq = cb.createQuery(Estado.class);
			Root<Estado> from = cq.from(Estado.class);
			Join<Estado, Pais> joinPais = from.join(Estado_.pais, JoinType.INNER);
			TypedQuery<Estado> tQuery = em
					.createQuery(cq.select(from).where(cb.equal(joinPais.get(Pais_.id), paisId),
							cb.equal(from.get(Estado_.id), id)));
			return tQuery.getSingleResult();
		} catch (NoResultException ex) {
			throw new EstadoException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", paisId));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public Long maxId(EntityManagerFactory entityManagerFactory, Long id) throws EstadoException {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Estado> from = cq.from(Estado.class);
			TypedQuery<Long> tQuery = em.createQuery(cq.select(cb.max(from.get(Estado_.id))));
			return tQuery.getSingleResult();
		} catch (Exception ex) {
			throw new EstadoException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", id));
		} finally {
			em.clear();
			em.close();
		}
	}

	public Long count(Long paisId, EstadoDTO entityDto, EntityManagerFactory entityManagerFactory) throws PessoaException {

		EntityManager em = entityManagerFactory.createEntityManager();
		List<Predicate> predicates = new ArrayList<>();

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Estado> from = cq.from(Estado.class);
			predicates = this.createPredicates(paisId, entityDto != null ? entityDto : null, from, cb);

			return em.createQuery(cq.select(cb.count(from)).where(predicates.toArray(new Predicate[] {}))).getSingleResult();

		} catch (Exception ex) {
			throw new EstadoException(String.format(MODE, "count", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}

	public List<Estado> findAll(Long paisId, EntityManagerFactory entityManagerFactory,	Map<String, String> requestParams, int page, int length) throws EstadoException {
		
		EntityManager em = entityManagerFactory.createEntityManager();
		
		try {

			List<Order> orderBy = new ArrayList<>();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Estado> cq = cb.createQuery(Estado.class);
			Root<Estado> from = cq.from(Estado.class);
			Join<Estado, Pais> joinPais = from.join(Estado_.pais, JoinType.INNER);

			for (Entry<String, String> entry : requestParams.entrySet()) {
				if (entry.getKey().startsWith(ORDER)) {
					orderBy.add((entry.getValue() == null || entry.getValue().equals("desc")
							? cb.desc(from.get(Estado_.id))
							: cb.asc(from.get(Estado_.id))));
				}
			}

			TypedQuery<Estado> tQuery = em.createQuery(cq.select(from).where(cb.equal(joinPais.get(Pais_.id), paisId)).orderBy(orderBy));
			
			tQuery.setFirstResult((page - 1) * length);
			tQuery.setMaxResults(length);
			return tQuery.getResultList();
			
		} catch (Exception ex) {
			throw new EstadoException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", paisId));
		} finally {
			em.clear();
			em.close();
		}
	}


	public List<Estado> findByFilters(Long paisId, EstadoDTO dto, EntityManagerFactory entityManagerFactory, Map<String, String> requestParams, int page, int max)
			throws PessoaException {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {

			List<Order> orderBy = new ArrayList<>();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Estado> cq = cb.createQuery(Estado.class);
			Root<Estado> from = cq.from(Estado.class);

			// o parametro order via pathVariable apenas para ordenação asc ou desc conforme seleção do usuário em tela.
			// http://{{SERVER}}:{{GATEWAY_PORT}}/st-api-cadastro-Estado/administracao/Estado/all/1/10?order=desc
			
			for (Entry<String, String> entry : requestParams.entrySet()) {
				if (entry.getKey().startsWith(ORDER)) {
					orderBy.add((entry.getValue() == null || entry.getValue().equals("desc") 
							? cb.desc(from.get(Estado_.id))
							: cb.asc(from.get(Estado_.id))));
				}
			}

			List<Predicate> predicates = this.createPredicates(paisId, dto, from, cb);
			TypedQuery<Estado> tQuery = em.createQuery(cq.select(from).where(predicates.stream().toArray(Predicate[]::new)).orderBy(orderBy));
			if (page != 0 && max != 0) {
				tQuery.setFirstResult((page - 1) * max);
				tQuery.setMaxResults(max);
			}
			return tQuery.getResultList();
		} catch (Exception ex) {
			throw new EstadoException(String.format(MODE, "findByFilters", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	
	
	public List<Predicate> createPredicates(Long paisId, EstadoDTO entityDto, Root<Estado> from, CriteriaBuilder cb) {

		List<Predicate> predicates = new ArrayList<>();

		// aqui eu poderia fazer um join filtrando por Estado de uma determinada pessoa somente.

		if (paisId != null && paisId > 0L) {
			Join<Estado, Pais> joinPais = from.join(Estado_.pais, JoinType.INNER);
			predicates.add(cb.equal(joinPais.get(Pais_.id), paisId));
		}

	    if (entityDto != null) {
	    	
	    	checkIsNull(entityDto.getSigla()).ifPresent(field -> predicates.add(cb.like(from.get(Estado_.sigla), "%" + field + "%")));
	    	
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

	
	public Optional<Estado> findById(Long id) {

		return estadoRepositoryImpl.findById(id);
	}

	public <S extends Estado> S save(S entity) {

		return estadoRepositoryImpl.save(entity);
	}

	public void delete(Estado entity) {

		estadoRepositoryImpl.delete(entity);
	}

	public <T> Optional<T> checkIsNull(T field) {

		return Optional.ofNullable(field);
	}
}
