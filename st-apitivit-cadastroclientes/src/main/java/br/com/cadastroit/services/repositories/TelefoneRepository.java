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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.cadastroit.services.api.domain.Pessoa;
import br.com.cadastroit.services.api.domain.Pessoa_;
import br.com.cadastroit.services.api.domain.Telefone;
import br.com.cadastroit.services.api.domain.Telefone_;
import br.com.cadastroit.services.api.enums.DbLayerMessage;
import br.com.cadastroit.services.exceptions.PessoaException;
import br.com.cadastroit.services.exceptions.TelefoneException;
import br.com.cadastroit.services.repositories.impl.TelefoneRepositoryImpl;
import br.com.cadastroit.services.web.dto.TelefoneDTO;
import lombok.NoArgsConstructor;

@Repository
@NoArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class TelefoneRepository implements Serializable {

private static final long serialVersionUID = 5261840790088760980L;
	
	private static final String MODE = "Error on %s mode to %s, [error] = %s";
	private static final String OBJECT = "ESTADO";
	private static final String ORDER = "order";
	
	@Autowired
	private TelefoneRepositoryImpl telefoneRepositoryImpl;

	public Long maxId(EntityManagerFactory entityManagerFactory) throws TelefoneException {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Telefone> from = cq.from(Telefone.class);
			TypedQuery<Long> tQuery = em
					.createQuery(cq.select(cb.max(from.get(Telefone_.id))));
			return tQuery.getSingleResult();
		} catch (Exception ex) {
			throw new TelefoneException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", ""));
		} finally {
			em.clear();
			em.close();
		}
	}

	public Telefone findById(Long id, EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Telefone> cq = cb.createQuery(Telefone.class);
			Root<Telefone> from = cq.from(Telefone.class);
			TypedQuery<Telefone> tQuery = em.createQuery(cq.select(from).where(cb.equal(from.get(Telefone_.id), id)));
			return tQuery.getSingleResult();
		} catch (NoResultException ex) {
			throw new TelefoneException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", id));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public Telefone findById(Long pessoaId, Long id, EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Telefone> cq = cb.createQuery(Telefone.class);
			Root<Telefone> from = cq.from(Telefone.class);
			Join<Telefone, Pessoa> joinPessoa = from.join(Telefone_.pessoa, JoinType.INNER);
			TypedQuery<Telefone> tQuery = em
					.createQuery(cq.select(from).where(cb.equal(joinPessoa.get(Pessoa_.id), pessoaId),
							cb.equal(from.get(Telefone_.id), id)));
			return tQuery.getSingleResult();
		} catch (NoResultException ex) {
			throw new TelefoneException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", pessoaId));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public Long maxId(EntityManagerFactory entityManagerFactory, Long id) throws TelefoneException {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Telefone> from = cq.from(Telefone.class);
			TypedQuery<Long> tQuery = em.createQuery(cq.select(cb.max(from.get(Telefone_.id))));
			return tQuery.getSingleResult();
		} catch (Exception ex) {
			throw new TelefoneException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", id));
		} finally {
			em.clear();
			em.close();
		}
	}

	public Long count(Long pessoaId, TelefoneDTO entityDto, EntityManagerFactory entityManagerFactory) throws PessoaException {

		EntityManager em = entityManagerFactory.createEntityManager();
		List<Predicate> predicates = new ArrayList<>();

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Telefone> from = cq.from(Telefone.class);
			predicates = this.createPredicates(pessoaId, entityDto != null ? entityDto : null, from, cb);

			return em.createQuery(cq.select(cb.count(from)).where(predicates.toArray(new Predicate[] {}))).getSingleResult();

		} catch (Exception ex) {
			throw new TelefoneException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "COUNT", pessoaId));
		} finally {
			em.clear();
			em.close();
		}
	}

	public List<Telefone> findAll(Long pessoaId, EntityManagerFactory entityManagerFactory,	Map<String, String> requestParams, int page, int length) throws TelefoneException {
		
		EntityManager em = entityManagerFactory.createEntityManager();
		
		try {

			List<Order> orderBy = new ArrayList<>();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Telefone> cq = cb.createQuery(Telefone.class);
			Root<Telefone> from = cq.from(Telefone.class);
			Join<Telefone, Pessoa> joinPessoa = from.join(Telefone_.pessoa, JoinType.INNER);

			for (Entry<String, String> entry : requestParams.entrySet()) {
				if (entry.getKey().startsWith(ORDER)) {
					orderBy.add((entry.getValue() == null || entry.getValue().equals("desc")
							? cb.desc(from.get(Telefone_.id))
							: cb.asc(from.get(Telefone_.id))));
				}
			}

			TypedQuery<Telefone> tQuery = em.createQuery(
					cq.select(from).where(cb.equal(joinPessoa.get(Pessoa_.id), pessoaId)).orderBy(orderBy));
			tQuery.setFirstResult((page - 1) * length);
			tQuery.setMaxResults(length);
			return tQuery.getResultList();
			
		} catch (Exception ex) {
			throw new TelefoneException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", pessoaId));
		} finally {
			em.clear();
			em.close();
		}
	}


	public List<Telefone> findByFilters(Long pessoaId, TelefoneDTO dto, EntityManagerFactory entityManagerFactory, Map<String, String> requestParams, int page, int max)
			throws PessoaException {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {

			List<Order> orderBy = new ArrayList<>();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Telefone> cq = cb.createQuery(Telefone.class);
			Root<Telefone> from = cq.from(Telefone.class);

			// o parametro order via pathVariable apenas para ordenação asc ou desc conforme seleção do usuário em tela.
			// http://{{SERVER}}:{{GATEWAY_PORT}}/st-api-cadastro-Telefone/administracao/Telefone/all/1/10?order=desc
			
			for (Entry<String, String> entry : requestParams.entrySet()) {
				if (entry.getKey().startsWith(ORDER)) {
					orderBy.add((entry.getValue() == null || entry.getValue().equals("desc") 
							? cb.desc(from.get(Telefone_.id))
							: cb.asc(from.get(Telefone_.id))));
				}
			}

			List<Predicate> predicates = this.createPredicates(pessoaId, dto, from, cb);
			TypedQuery<Telefone> tQuery = em.createQuery(cq.select(from).where(predicates.stream().toArray(Predicate[]::new)).orderBy(orderBy));
			if (page != 0 && max != 0) {
				tQuery.setFirstResult((page - 1) * max);
				tQuery.setMaxResults(max);
			}
			return tQuery.getResultList();
		} catch (Exception ex) {
			throw new TelefoneException(String.format(MODE, "findByFilters", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	
	
	public List<Predicate> createPredicates(Long pessoaId, TelefoneDTO entityDto, Root<Telefone> from, CriteriaBuilder cb) {

		List<Predicate> predicates = new ArrayList<>();

		// aqui eu poderia fazer um join filtrando por Telefone de uma determinada pessoa somente.

		if (pessoaId != null && pessoaId > 0L) {
			Join<Telefone, Pessoa> joinPessoa = from.join(Telefone_.pessoa, JoinType.INNER);
			predicates.add(cb.equal(joinPessoa.get(Pessoa_.id), pessoaId));
		}

	    if (entityDto != null) {
	    	
	    	checkIsNull(entityDto.getNumero()).ifPresent(field -> predicates.add(cb.like(from.get(Telefone_.numero), "%" + field + "%")));
	    	
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

	
	public Optional<Telefone> findById(Long id) {

		return telefoneRepositoryImpl.findById(id);
	}

	public <S extends Telefone> S save(S entity) {

		return telefoneRepositoryImpl.save(entity);
	}

	public void delete(Telefone entity) {

		telefoneRepositoryImpl.delete(entity);
	}

	public <T> Optional<T> checkIsNull(T field) {

		return Optional.ofNullable(field);
	}

}
