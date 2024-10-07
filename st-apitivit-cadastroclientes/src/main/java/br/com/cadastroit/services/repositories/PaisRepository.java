package br.com.cadastroit.services.repositories;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.cadastroit.services.api.domain.Pais;
import br.com.cadastroit.services.api.domain.Pais_;
import br.com.cadastroit.services.api.enums.DbLayerMessage;
import br.com.cadastroit.services.exceptions.PaisException;
import br.com.cadastroit.services.exceptions.PessoaException;
import br.com.cadastroit.services.exceptions.TarefaException;
import br.com.cadastroit.services.repositories.impl.PaisRepositoryImpl;
import br.com.cadastroit.services.web.dto.PaisDTO;
import br.com.cadastroit.services.web.mapper.PaisMapper;
import lombok.NoArgsConstructor;

@Repository
@NoArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class PaisRepository implements Serializable {
	
private static final long serialVersionUID = 4319099779571900454L;
	
	private static final String MODE = "Error on %s mode to %s, [error] = %s";
	private static final String OBJECT = "PAIS";
	private static final String ORDER = "order";
	
	@Autowired
	private PaisRepositoryImpl paisRepositoryImpl;

	protected final PaisMapper Pais = Mappers.getMapper(PaisMapper.class);
	
	public Long maxId(EntityManagerFactory entityManagerFactory) throws SQLException {

		EntityManager em = entityManagerFactory.createEntityManager();
		Query query = em.createNativeQuery("select nvl(max(id),1) from " + OBJECT);
		return ((Number) query.getSingleResult()).longValue();
	}
	
	public Long maxId_(EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Pais> from = cq.from(Pais.class);
			TypedQuery<Long> result = em.createQuery(cq.select(cb.max(from.get(Pais_.id))));
			return result.getSingleResult();
		} catch (Exception ex) {
			throw new PaisException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", null));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public Long maxId(EntityManagerFactory entityManagerFactory, Long pessoaId) throws TarefaException {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Pais> from = cq.from(Pais.class);
			TypedQuery<Long> tQuery = em
					.createQuery(cq.select(cb.max(from.get(Pais_.id))));
			return tQuery.getSingleResult();
		} catch (Exception ex) {
			throw new PaisException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", pessoaId));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public Pais findById(Long id, EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Pais> cq = cb.createQuery(Pais.class);
			Root<Pais> from = cq.from(Pais.class);
			TypedQuery<Pais> tQuery = em.createQuery(cq.select(from).where(cb.equal(from.get(Pais_.id), id)));
			return tQuery.getSingleResult();
		} catch (NoResultException ex) {
			throw new PaisException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", id));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public Pais findById(Long pessoaId, Long id, EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {
		//	CriteriaBuilder cb = em.getCriteriaBuilder();
		//	CriteriaQuery<Pais> cq = cb.createQuery(Pais.class);
		//	Root<Pais> from = cq.from(Pais.class);
		//	Join<Pais, Tarefa> joinTarefa = from.join(Pais_.tarefa, JoinType.INNER);
		//	Join<Tarefa, Pessoa> joinPessoa = joinTarefa.join(Tarefa_.pessoa, JoinType.INNER);
		//	TypedQuery<Pais> tQuery = em
		//			.createQuery(cq.select(from).where(cb.equal(joinPessoa.get(Pessoa_.id), pessoaId), cb.equal(from.get(Pais_.id), id)));
		//	return tQuery.getSingleResult();
			return null;
		} catch (NoResultException ex) {
			throw new PaisException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", id));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public Long count(PaisDTO entityDto, EntityManagerFactory entityManagerFactory) throws PessoaException {

		EntityManager em = entityManagerFactory.createEntityManager();
		List<Predicate> predicates = new ArrayList<>();

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Pais> from = cq.from(Pais.class);
			predicates = this.createPredicates(entityDto != null ? entityDto : null, from, cb);

			return em.createQuery(cq.select(cb.count(from)).where(predicates.toArray(new Predicate[] {}))).getSingleResult();

		} catch (Exception ex) {
			throw new PaisException(String.format(MODE, "count", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public List<Pais> findAll(EntityManagerFactory entityManagerFactory, Map<String, String> requestParams, int page, int length)
			throws PessoaException {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {

			List<Order> orderBy = new ArrayList<>();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Pais> cq = cb.createQuery(Pais.class);
			Root<Pais> from = cq.from(Pais.class);

			for (Entry<String, String> entry : requestParams.entrySet()) {
				if (entry.getKey().startsWith(ORDER)) {
					orderBy.add((entry.getValue() == null || entry.getValue().equals("desc") ? cb.desc(from.get(Pais_.id))
							: cb.asc(from.get(Pais_.id))));
				}
			}
			
			TypedQuery<Pais> tQuery = em.createQuery(cq.select(from).orderBy(orderBy));
			List<Pais> Pais = tQuery.getResultList();
			
			Pais.stream().forEach( depto -> {
				
				
			});

			tQuery.setFirstResult((page - 1) * length);
			tQuery.setMaxResults(length);
			return Pais;
		} catch (Exception ex) {
			throw new PaisException(String.format(MODE, "pagination", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public List<Pais> findByFilters(PaisDTO dto, EntityManagerFactory entityManagerFactory, Map<String, String> requestParams, int page, int max)
			throws PessoaException {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {

			List<Order> orderBy = new ArrayList<>();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Pais> cq = cb.createQuery(Pais.class);
			Root<Pais> from = cq.from(Pais.class);

			requestParams.entrySet().stream().filter(entry -> entry.getKey().startsWith(ORDER)).map(e -> {
				return (e.getValue() == null || e.getValue().equals("desc")) ? cb.desc(from.get(Pais_.id)) : cb.asc(from.get(Pais_.id));

			}).forEach(orderBy::add);

			List<Predicate> predicates = createPredicates(dto, from, cb);
			TypedQuery<Pais> tQuery = em.createQuery(cq.select(from).where(predicates.stream().toArray(Predicate[]::new)).orderBy(orderBy));
			if (page != 0 && max != 0) {
				tQuery.setFirstResult((page - 1) * max);
				tQuery.setMaxResults(max);
			}
			return tQuery.getResultList();
		} catch (Exception ex) {
			throw new PaisException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", ""));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public List<Predicate> createPredicates(PaisDTO entityDto, Root<Pais> from, CriteriaBuilder cb) {

		List<Predicate> predicates = new ArrayList<>();


		if (entityDto != null) {

		//	if (entityDto.getDataInicio() != null) {
		//		predicates.add(cb.greaterThanOrEqualTo(joinTarefa.get(Tarefa_.dataInicio), entityDto.getDataInicio()));
        //
		//	}

		//	if (entityDto.getDataFinal() != null) {
		//		predicates.add(cb.lessThanOrEqualTo(joinTarefa.get(Tarefa_.dataFinal), entityDto.getDataFinal()));
		//	}
			
		}
		
		return predicates;
	}
	
	public Optional<Pais> findById(Long id) {

		return paisRepositoryImpl.findById(id);
	}

	public <S extends Pais> S save(S entity) {

		return paisRepositoryImpl.save(entity);
	}

	public void delete(Pais entity) {

		paisRepositoryImpl.delete(entity);
	}

	public <T> Optional<T> checkIsNull(T field) {

		return Optional.ofNullable(field);
	}


}
