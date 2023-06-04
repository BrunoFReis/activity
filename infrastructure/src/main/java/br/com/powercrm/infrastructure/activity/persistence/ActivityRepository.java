package br.com.powercrm.infrastructure.activity.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<ActivityJpaEntity, Long> {

    Page<ActivityJpaEntity> findAll(Specification<ActivityJpaEntity> whereCause, Pageable page);
}
