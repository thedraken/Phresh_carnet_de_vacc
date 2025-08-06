package com.phresh.repository;

import com.phresh.domain.User;
import com.phresh.domain.VaccinationSchedule;
import com.phresh.domain.VaccinationType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccinationScheduleRepository extends CrudRepository<VaccinationSchedule, Long> {

    List<VaccinationSchedule> findAllByVaccinationTypeAndUserAndVaccinationIsNull(VaccinationType vaccinationType, User user);

    List<VaccinationSchedule> findAllByUserAndVaccinationIsNull(User user);
}
