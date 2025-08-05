package com.phresh.repository;

import com.phresh.domain.Disease;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseRepository extends CrudRepository<Disease, Long> {

}
