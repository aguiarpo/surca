package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Vet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

public interface VetRepository extends PagingAndSortingRepository<Vet, Long> {
    @Transactional
    void removeByUserUserLevelId(Long id);
    Page<Vet> findByUserName(Pageable pageable, String name);
    Page<Vet> findByCrmv(Pageable pageable, String crmv);
    Page<Vet> findByUserNameStartingWith(Pageable pageable, String name);
    Page<Vet> findByUserEmail(Pageable pageable, String crmv);
}
