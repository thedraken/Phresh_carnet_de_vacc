package com.phresh.view;

import com.phresh.domain.VaccinationSchedule;
import com.phresh.presenter.ScheduledVaccinationPresenter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("vaccination_schedule")
@PermitAll
@PageTitle("Vaccination Schedule")
public class ScheduledVaccinationView extends AbstractGridView<ScheduledVaccinationPresenter, VaccinationSchedule> {


    public ScheduledVaccinationView(ScheduledVaccinationPresenter presenter) {
        super(presenter, VaccinationSchedule.class);
    }

    @Override
    protected Component addLayoutAboveGrid() {
        return null;
    }

    @Override
    protected void doInitSearch() {
        setGridModel(presenter.getVaccinationSchedules());
    }

    @Override
    protected boolean checkMatches(VaccinationSchedule item, String searchTerm) {
        if (searchTerm.isEmpty()) {
            return true;
        }
        boolean matchesTypeName = matchesTerm(item.getVaccinationType().getTypeName(),
                searchTerm);
        boolean matchesDate = matchesTerm(item.getScheduledDate(), searchTerm);

        return matchesTypeName || matchesDate;
    }


    @Override
    protected void addColumnsToGrid(Grid<VaccinationSchedule> grid) {
        grid.addColumn(VaccinationSchedule::getVaccinationType).setHeader("Vaccination Type").setSortable(true);
        grid.addColumn(VaccinationSchedule::getScheduledDate).setHeader("Scheduled Date").setSortable(true);
    }
}
