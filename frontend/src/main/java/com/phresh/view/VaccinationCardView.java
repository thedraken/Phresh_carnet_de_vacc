package com.phresh.view;

import com.phresh.presenter.VaccinationCardPresenter;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;


@Route("vaccination_card")
@PermitAll
@PageTitle("Vaccination Card")
public class VaccinationCardView extends AbstractLoggedInView<VaccinationCardPresenter> {

    @Autowired
    public VaccinationCardView(VaccinationCardPresenter presenter) {
        super(presenter);
    }

    @Override
    public VerticalLayout buildLayout() {
        return new VerticalLayout();
    }
}
