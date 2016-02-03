package com.example.pie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Credits;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.addon.charts.model.style.Color;
import com.vaadin.addon.charts.model.style.SolidColor;

@SuppressWarnings("serial")
public class TopSixTheatersChart extends Chart {

    public TopSixTheatersChart() {
        super(ChartType.PIE);
        getConfiguration().setTitle("Popular Movies");
        getConfiguration().getChart().setType(ChartType.PIE);
        setWidth("100%");
        setHeight("90%");

        DataSeries series = new DataSeries();
        
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
        int i = 0;
        for (String movie : movies.keySet()) {
            Integer score = movies.get(movie);
            DataSeriesItem item = new DataSeriesItem(movie, score);
            series.add(item);
            item.setColor(chartColors[5 - i]);
            i++;
        }
        getConfiguration().setSeries(series);

        PlotOptionsPie opts = new PlotOptionsPie();
        opts.setBorderWidth(0);
        opts.setShadow(false);
        opts.setAnimation(false);
        getConfiguration().setPlotOptions(opts);

        Credits c = new Credits("");
        getConfiguration().setCredits(c);
    }

}
