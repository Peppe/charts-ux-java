package com.example.bar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Credits;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsBar;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.addon.charts.model.style.Color;
import com.vaadin.addon.charts.model.style.SolidColor;

@SuppressWarnings("serial")
public class TopGrossingMoviesChart extends Chart {
	
    public TopGrossingMoviesChart() {
        setCaption("Top Grossing Movies");
        getConfiguration().setTitle("");
        getConfiguration().getChart().setType(ChartType.BAR);
        getConfiguration().getChart().setAnimation(false);
        getConfiguration().getxAxis().getLabels().setEnabled(false);
        getConfiguration().getxAxis().setTickWidth(0);
        getConfiguration().getyAxis().setTitle("");
        setSizeFull();
        Color[] chartColors = new Color[] {
                new SolidColor("#3090F0"), new SolidColor("#18DDBB"),
                new SolidColor("#98DF58"), new SolidColor("#F9DD51"),
                new SolidColor("#F09042"), new SolidColor("#EC6464") };
            
        Map<String, Integer> movies = new HashMap<String, Integer>();
        movies.put("Left Behind", 2);
        movies.put("The Maze Runner", 63);
        movies.put("The Equalizer", 61);
        movies.put("Annabelle", 31);
        movies.put("Gone Girl", 87);
        movies.put("The Boxtrolls", 74);

        List<Series> series = new ArrayList<Series>();
        int i = 0;
        for (String movie : movies.keySet()) {
            Integer score = movies.get(movie);
            PlotOptionsBar opts = new PlotOptionsBar();
            opts.setColor(chartColors[5 - i]);
            i++;
            opts.setBorderWidth(0);
            opts.setShadow(false);
            opts.setPointPadding(0.4);
            opts.setAnimation(false);
            ListSeries item = new ListSeries(movie, score);
            item.setPlotOptions(opts);
            series.add(item);

        }
        getConfiguration().setSeries(series);

        Credits c = new Credits("");
        getConfiguration().setCredits(c);

        PlotOptionsBar opts = new PlotOptionsBar();
        opts.setGroupPadding(0);
        getConfiguration().setPlotOptions(opts);

    }
}
