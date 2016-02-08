package com.example.timeline;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.vaadin.maddon.ListContainer;

import com.example.timeline.Movie.MovieRevenue;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.ContainerDataSeries;
import com.vaadin.addon.charts.model.HorizontalAlign;
import com.vaadin.addon.charts.model.Legend;
import com.vaadin.addon.charts.model.PlotOptionsLine;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.addon.charts.model.VerticalAlign;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SalesView extends VerticalLayout implements View {

    private final Chart timeline;
    private ComboBox movieSelect;
    private List<Series> series = new ArrayList<Series>();

    private static final SolidColor[] COLORS = new SolidColor[] {
            new SolidColor(52, 154, 255), new SolidColor(242, 81, 57),
            new SolidColor(255, 201, 35), new SolidColor(83, 220, 164) };
    private static final SolidColor[] COLORS_ALPHA = new SolidColor[] {
            new SolidColor(52, 154, 255, 0.3),
            new SolidColor(242, 81, 57, 0.3),
            new SolidColor(255, 201, 35, 0.3),
            new SolidColor(83, 220, 164, 0.3) };
    private int colorIndex = -1;
    
    private static List<Movie> movies;
   
	public void buildMovies(){
		movies = new ArrayList<Movie>();
        movies.add(new Movie("Left Behind"));
        movies.add(new Movie("The Maze Runner"));
        movies.add(new Movie("The Equalizer"));
        movies.add(new Movie("Annabelle"));
        movies.add(new Movie("Gone Girl"));
        movies.add(new Movie("The Boxtrolls"));
	}
	
    public SalesView() {
    	buildMovies();
        setSizeFull();
        addStyleName("sales");

        addComponent(buildHeader());

        timeline = buildTimeline();
        addComponent(timeline);
        setExpandRatio(timeline, 1);

        initMovieSelect();

        // Add first 4 by default        
        List<Movie> visible = new ArrayList<Movie>(movies.subList(0, 4));
        List<Movie> disabled = new ArrayList<Movie>(movies.subList(4, 6));
        
        for (Movie m : visible) {
            addDataSet(m, true);
        }
        for (Movie m : disabled){
        	System.out.println("adding " + m.getTitle());
        	addDataSet(m, false);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -2);
        if (series.size() > 0) {
        	//timeline.setVisibleDateRange(calendar.getTime(), new Date());
        	timeline.getConfiguration().getxAxis().setMin(calendar.getTimeInMillis());
        	timeline.getConfiguration().getxAxis().setMax(new Date().getTime());
        }
    }

    private void initMovieSelect() {
        Container movieContainer = new ListContainer<Movie>(Movie.class, movies);
        movieSelect.setContainerDataSource(movieContainer);
    }

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label titleLabel = new Label("Revenue by Movie");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponents(titleLabel, buildToolbar());

        return header;
    }

    private Component buildToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("toolbar");
        toolbar.setSpacing(true);

        movieSelect = new ComboBox();
        movieSelect.setItemCaptionPropertyId("title");
        movieSelect.addShortcutListener(new ShortcutListener("Add",
                KeyCode.ENTER, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                addDataSet((Movie) movieSelect.getValue(), true);
            }
        });

        final Button add = new Button("Add");
        add.setEnabled(false);
        add.addStyleName(ValoTheme.BUTTON_PRIMARY);

        CssLayout group = new CssLayout(movieSelect, add);
        group.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        toolbar.addComponent(group);

        movieSelect.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(final ValueChangeEvent event) {
                add.setEnabled(event.getProperty().getValue() != null);
            }
        });

        final Button clear = new Button("Clear");
        clear.addStyleName("clearbutton");
        clear.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                removeAllSeries();
                initMovieSelect();
                clear.setEnabled(false);
            }
        });
        toolbar.addComponent(clear);

        add.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                addDataSet((Movie) movieSelect.getValue(), true);
                clear.setEnabled(true);
            }
        });

        return toolbar;
    }
    
    private void removeAllSeries(){
    	series.clear();
    	timeline.getConfiguration().setSeries(series);
    }

    private Chart buildTimeline() {
        Chart result = new Chart(ChartType.LINE);
        result.setTimeline(true);
        result.getConfiguration().getTooltip().setValueDecimals(2);
        
        //result.setDateSelectVisible(false);
        result.getConfiguration().getRangeSelector().setEnabled(false);
        
        Legend legend = new Legend();
        legend.setEnabled(true);
        legend.setVerticalAlign(VerticalAlign.TOP);
        legend.setAlign(HorizontalAlign.RIGHT);
        
        result.getConfiguration().setLegend(legend);
        
        // result.setChartModesVisible(false);
        //result.setGraphShadowsEnabled(false);
        //result.setZoomLevelsVisible(false);
        result.setSizeFull();
        //result.setNoDataSourceCaption("<span class=\"v-label h2 light\">Add a data set from the dropdown above</span>");
        return result;
    }

    private void addDataSet(final Movie movie, boolean visible) {
        movieSelect.removeItem(movie);
        movieSelect.setValue(null);

        Collection<MovieRevenue> dailyRevenue = movie.getRevenues();

        ListContainer<MovieRevenue> dailyRevenueContainer = new TempMovieRevenuesContainer(
                dailyRevenue);
        

        dailyRevenueContainer.sort(new Object[] { "timestamp" },
                new boolean[] { true });

        ContainerDataSeries containerSeries = new ContainerDataSeries(dailyRevenueContainer);
        containerSeries.setYPropertyId("revenue");
        containerSeries.setXPropertyId("timestamp");
        containerSeries.setVisible(visible);
        // timeline.addGraphDataSource(dailyRevenueContainer, "timestamp", "revenue");
        series.add(containerSeries);
        timeline.getConfiguration().setSeries(series);
        
        PlotOptionsLine options = new PlotOptionsLine();
        containerSeries.setPlotOptions(options);
        colorIndex = (colorIndex >= COLORS.length - 1 ? 0 : ++colorIndex);
        // timeline.setGraphOutlineColor(dailyRevenueContainer, COLORS[colorIndex]);
        options.setColor(COLORS[colorIndex]);
//        timeline.setBrowserOutlineColor(dailyRevenueContainer,
//                COLORS[colorIndex]);
//        timeline.setBrowserFillColor(dailyRevenueContainer,
//                COLORS_ALPHA[colorIndex]);
        containerSeries.setName(movie.getTitle());
//        timeline.setEventCaptionPropertyId("date");
//        timeline.setVerticalAxisLegendUnit(dailyRevenueContainer, "$");
    }
   

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    private class TempMovieRevenuesContainer extends
            ListContainer<MovieRevenue> {

        public TempMovieRevenuesContainer(
                final Collection<MovieRevenue> collection) {
            super(MovieRevenue.class, collection);
        }

        // This is only temporarily overridden until issues with
        // BeanComparator get resolved.
        @Override
        public void sort(final Object[] propertyId, final boolean[] ascending) {
            final boolean sortAscending = ascending[0];
            Collections.sort(getBackingList(), new Comparator<MovieRevenue>() {
                @Override
                public int compare(final MovieRevenue o1, final MovieRevenue o2) {
                    int result = o1.getTimestamp().compareTo(o2.getTimestamp());
                    if (!sortAscending) {
                        result *= -1;
                    }
                    return result;
                }
            });
        }
    }
}
