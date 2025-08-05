package com.phresh.view;

import com.phresh.domain.Disease;
import com.phresh.domain.Vaccination;
import com.phresh.domain.VaccinationType;
import com.phresh.exceptions.RuleException;
import com.phresh.presenter.VaccinationCardPresenter;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;


@Route("vaccination_card")
@PermitAll
@PageTitle("Vaccination Card")
public class VaccinationCardView extends AbstractLoggedInView<VaccinationCardPresenter> implements AfterNavigationObserver {

    private static final Logger logger = Logger.getLogger(VaccinationCardView.class.getSimpleName());
    private Grid<Vaccination> vaccinationGrid;
    private TextField searchField;
    private List<Registration> registrations;

    @Autowired
    public VaccinationCardView(VaccinationCardPresenter presenter) {
        super(presenter);
    }

    @Override
    public VerticalLayout buildLayout() {
        VerticalLayout windowLayout = new VerticalLayout();

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
        vaccinationGrid = new Grid<>(Vaccination.class);
        vaccinationGrid.setEmptyStateText("No vaccinations found");

        searchField = new TextField();
        searchField.setWidth("50%");
        searchField.setPlaceholder("Filter");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        windowLayout.add(searchFieldLayout, searchField, vaccinationGrid);
        windowLayout.setSizeFull();
        registrations = new ArrayList<>();
        setGridModel(presenter.doSearch(null, null, null, null, null));

        return windowLayout;
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
        binder.forField(vaccinationTypeComboBox).asRequired().bind(Vaccination::getVaccinationType, Vaccination::setVaccinationType);
        binder.forField(dateOfVaccinationDatePicker).asRequired().bind(Vaccination::getDateOfVaccination, Vaccination::setDateOfVaccination);
        binder.forField(commentsTextField).bind(Vaccination::getComments, Vaccination::setComments);

        HorizontalLayout detailsLayout = new HorizontalLayout();
        detailsLayout.add(vaccinationTypeComboBox, dateOfVaccinationDatePicker);
        dialogLayout.add(detailsLayout, commentsTextField);


        Button addButton = new Button("Add", buttonClickEvent -> {
            Vaccination vaccination = new Vaccination();
            if (binder.writeBeanIfValid(vaccination)) {
                try {
                    presenter.saveVaccination(vaccination);
                    new Notification("Vaccination created").open();
                    dialog.close();
                    UI.getCurrent().navigate(VaccinationCardView.class);
                } catch (RuleException e) {
                    Notification notification = new Notification(e.getMessage());
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.open();
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

    private void setGridModel(List<Vaccination> vaccinations) {
        if (registrations != null) {
            registrations.forEach(Registration::remove);
            registrations.clear();
        }
        vaccinationGrid.removeAllColumns();
        vaccinationGrid.addColumn(Vaccination::getVaccinationType).setHeader("Vaccination Type").setSortable(true);
        vaccinationGrid.addColumn(Vaccination::getDateOfVaccination).setHeader("Date of Vaccination").setSortable(true);
        vaccinationGrid.addColumn(Vaccination::getDateOfRenewal).setHeader("Expected Date of Renewal").setSortable(true);
        vaccinationGrid.addColumn(Vaccination::getComments).setHeader("Comments");
        vaccinationGrid.addColumn((ValueProvider<Vaccination, Object>) vaccination -> String.join(", ", vaccination.getVaccinationType().getDiseasesTreated().stream().map(Disease::getName).toList())).setHeader("Diseases Treated");
        vaccinationGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        GridListDataView<Vaccination> dataView = vaccinationGrid.setItems(vaccinations);
        registrations.add(searchField.addValueChangeListener(e -> dataView.refreshAll()));
        dataView.addFilter(vaccination -> {
            String searchTerm = searchField.getValue().trim();
            if (searchTerm.isEmpty()) {
                return true;
            }
            boolean matchesFullName = matchesTerm(vaccination.getVaccinationType().getTypeName(),
                    searchTerm);
            boolean matchesEmail = matchesTerm(vaccination.getDateOfVaccination(), searchTerm);
            boolean matchesProfession = matchesTerm(vaccination.getVaccinationType().getDiseasesTreated(),
                    searchTerm);

            return matchesFullName || matchesEmail || matchesProfession;
        });
    }

    private boolean matchesTerm(String vaccinationTypeName, String searchTerm) {
        if (Objects.equals(vaccinationTypeName, searchTerm)) {
            return true;
        }
        if (vaccinationTypeName == null) {
            return false;
        }
        return vaccinationTypeName.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private boolean matchesTerm(LocalDate dateOfVaccination, String searchTerm) {
        if (dateOfVaccination == null) {
            return false;
        }
        return dateOfVaccination.toString().toLowerCase().contains(searchTerm.toLowerCase());
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
