package com.phresh.view;

import com.phresh.domain.Disease;
import com.phresh.domain.Vaccination;
import com.phresh.domain.VaccinationSchedule;
import com.phresh.domain.VaccinationType;
import com.phresh.exceptions.RuleException;
import com.phresh.presenter.VaccinationCardPresenter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;


@Route("vaccination_card")
@PermitAll
@PageTitle("Vaccination Card")
public class VaccinationCardView extends AbstractGridView<VaccinationCardPresenter, Vaccination> implements AfterNavigationObserver {

    private static final Logger logger = Logger.getLogger(VaccinationCardView.class.getSimpleName());


    @Autowired
    public VaccinationCardView(VaccinationCardPresenter presenter) {
        super(presenter, Vaccination.class);
    }

    @Override
    protected void doInitSearch() {
        setGridModel(presenter.doSearch(null, null, null, null, null));
    }

    @Override
    public Component addLayoutAboveGrid() {
        FormLayout searchFieldLayout = new FormLayout();
        TextField freeTextField = new TextField("Search Field");
        DatePicker fromDatePicker = new DatePicker("From Date");
        DatePicker toDatePicker = new DatePicker("To Date");
        ComboBox<VaccinationType> vaccinationTypeComboBox = new ComboBox<>("Vaccination Type", presenter.getAllVaccinationTypes());
        ComboBox<Disease> diseaseComboBox = new ComboBox<>("Disease Treated", presenter.getAllDiseases());
        Button searchButton = new Button("Search", buttonClickEvent -> {
            List<Vaccination> vaccinations = presenter.doSearch(freeTextField.getValue(), fromDatePicker.getValue(), toDatePicker.getValue(),
                    diseaseComboBox.getValue(), vaccinationTypeComboBox.getValue());
            setGridModel(vaccinations);
        });
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button addButton = new Button("Add Vaccination", buttonClickEvent -> addVaccination());

        searchFieldLayout.addFormRow(freeTextField, vaccinationTypeComboBox, diseaseComboBox, fromDatePicker, toDatePicker);
        searchFieldLayout.addFormRow(searchButton, addButton);
        searchFieldLayout.setAutoResponsive(true);
        searchFieldLayout.setExpandFields(true);
        searchFieldLayout.setWidth("100%");

        return searchFieldLayout;
    }

    private void addVaccination() {
        Dialog dialog = new Dialog("Add Vaccination");
        VerticalLayout dialogLayout = new VerticalLayout();
        ComboBox<VaccinationType> vaccinationTypeComboBox = new ComboBox<>("Vaccination Type");
        vaccinationTypeComboBox.setItems(presenter.getAllVaccinationTypes());
        DatePicker dateOfVaccinationDatePicker = new DatePicker("Date of Vaccination");
        dateOfVaccinationDatePicker.setMax(LocalDate.now());
        dateOfVaccinationDatePicker.setMin(presenter.getUserFromAuthenticationContext().getDateOfBirth());
        TextField commentsTextField = new TextField("Comments");
        commentsTextField.setMaxLength(500);
        commentsTextField.setWidthFull();

        Binder<Vaccination> binder = new Binder<>(Vaccination.class);
        binder.forField(vaccinationTypeComboBox).asRequired(e -> "Vaccination Type is Required").bind(Vaccination::getVaccinationType, Vaccination::setVaccinationType);
        binder.forField(dateOfVaccinationDatePicker).asRequired(e -> "Date of Vaccination is Required").bind(Vaccination::getDateOfVaccination, Vaccination::setDateOfVaccination);
        binder.forField(commentsTextField).bind(Vaccination::getComments, Vaccination::setComments);
        Div beanValidationErrors = new Div();
        beanValidationErrors.addClassName(LumoUtility.TextColor.ERROR);
        binder.setStatusLabel(beanValidationErrors);

        HorizontalLayout detailsLayout = new HorizontalLayout();
        detailsLayout.add(vaccinationTypeComboBox, dateOfVaccinationDatePicker);
        dialogLayout.add(detailsLayout, commentsTextField, beanValidationErrors);


        Button addButton = new Button("Add", buttonClickEvent -> {
            Vaccination vaccination = new Vaccination();
            if (binder.writeBeanIfValid(vaccination)) {
                try {
                    presenter.saveVaccination(vaccination);
                    Notification.show("Vaccination created");
                    dialog.close();
                    UI.getCurrent().navigate(VaccinationCardView.class);
                } catch (RuleException e) {
                    showNotificationError(e);
                }
            }
        });
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", buttonClickEvent -> {
            binder.getFields().forEach(HasValue::clear);
            dialog.close();
        });

        HorizontalLayout buttons = new HorizontalLayout(addButton, cancelButton);
        buttons.setAlignItems(FlexComponent.Alignment.END);
        dialog.add(dialogLayout, buttons);
        dialog.setHeight("25%");
        dialog.open();
    }

    @Override
    protected void addColumnsToGrid(Grid<Vaccination> grid) {
        grid.addColumn(Vaccination::getVaccinationType).setHeader("Vaccination Type").setSortable(true);
        grid.addColumn(Vaccination::getDateOfVaccination).setHeader("Date of Vaccination").setSortable(true);
        grid.addColumn(v -> {
            VaccinationSchedule vs = presenter.getNextVaccinationSchedule(v);
            if (vs != null) {
                return vs.getScheduledDate();
            }
            return null;
        }).setHeader("Expected Date of Renewal").setSortable(true);
        grid.addColumn(Vaccination::getComments).setHeader("Comments");
        grid.addColumn((ValueProvider<Vaccination, Object>) vaccination -> String.join(", ", vaccination.getVaccinationType().getDiseasesTreated().stream().map(Disease::getName).toList())).setHeader("Diseases Treated");
    }

    @Override
    protected boolean checkMatches(Vaccination item, String searchTerm) {
        if (searchTerm.isEmpty()) {
            return true;
        }
        boolean matchesTypeName = matchesTerm(item.getVaccinationType().getTypeName(),
                searchTerm);
        boolean matchesDate = matchesTerm(item.getDateOfVaccination(), searchTerm);
        boolean matchesDiseases = matchesTerm(item.getVaccinationType().getDiseasesTreated(),
                searchTerm);

        return matchesTypeName || matchesDate || matchesDiseases;
    }

    private boolean matchesTerm(Set<Disease> diseases, String searchTerm) {
        if (diseases.isEmpty()) {
            return false;
        }
        return diseases.stream().anyMatch(d -> d.getName().toLowerCase().contains(searchTerm.toLowerCase()));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        logger.info("Entering " + VaccinationCardView.class.getSimpleName());
    }
}
