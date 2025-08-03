package com.phresh.repository;

import com.phresh.domain.VaccinationType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationTypeRepository extends CrudRepository<VaccinationType, Long> {


}
