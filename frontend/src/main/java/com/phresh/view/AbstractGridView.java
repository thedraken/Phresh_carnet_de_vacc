package com.phresh.view;

import com.phresh.presenter.AbstractLoggedinPresenter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractGridView<P extends AbstractLoggedinPresenter<?>, T> extends AbstractLoggedInView<P> {

    private final Class<T> classType;
    private Grid<T> grid;
    private TextField filterField;
    private List<Registration> registrations;
    private VerticalLayout windowLayout;

    public AbstractGridView(P presenter, Class<T> classType) {
        super(presenter);
        this.classType = classType;
        buildGrid();
    }

    @Override
    public VerticalLayout buildLayout() {
        windowLayout = new VerticalLayout();
        return windowLayout;
    }

    private void buildGrid() {
        grid = new Grid<>(classType);
        grid.setEmptyStateText("No vaccinations found");

        filterField = new TextField();
        filterField.setWidth("50%");
        filterField.setPlaceholder("Filter");
        filterField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filterField.setValueChangeMode(ValueChangeMode.EAGER);

        Component layOutAboveGrid = addLayoutAboveGrid();
        if (layOutAboveGrid != null) {
            windowLayout.add(addLayoutAboveGrid(), filterField, grid);
        } else {
            windowLayout.add(filterField, grid);
        }
        windowLayout.setSizeFull();
        registrations = new ArrayList<>();
        doInitSearch();
    }

    protected abstract Component addLayoutAboveGrid();

    protected abstract void doInitSearch();


    protected void setGridModel(List<T> items) {
        if (registrations != null) {
            registrations.forEach(Registration::remove);
            registrations.clear();
        }
        grid.removeAllColumns();
        addColumnsToGrid(grid);
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        GridListDataView<T> dataView = grid.setItems(items);
        registrations.add(filterField.addValueChangeListener(e -> dataView.refreshAll()));
        dataView.addFilter(item -> {
            String searchTerm = filterField.getValue().trim();
            return checkMatches(item, searchTerm);
        });
    }

    protected abstract boolean checkMatches(T item, String searchTerm);

    protected abstract void addColumnsToGrid(Grid<T> grid);

    protected boolean matchesTerm(String columnData, String searchTerm) {
        if (Objects.equals(columnData, searchTerm)) {
            return true;
        }
        if (columnData == null) {
            return false;
        }
        return columnData.toLowerCase().contains(searchTerm.toLowerCase());
    }

    protected boolean matchesTerm(LocalDate columnData, String searchTerm) {
        if (columnData == null) {
            return false;
        }
        return columnData.toString().toLowerCase().contains(searchTerm.toLowerCase());
    }
}
