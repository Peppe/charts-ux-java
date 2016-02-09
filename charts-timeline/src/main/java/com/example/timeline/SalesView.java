package com.example.timeline;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.vaadin.maddon.ListContainer;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.ContainerDataSeries;
import com.vaadin.addon.charts.model.HorizontalAlign;
import com.vaadin.addon.charts.model.Legend;
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.MarkerSymbolEnum;
import com.vaadin.addon.charts.model.PlotOptionsLine;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.addon.charts.model.VerticalAlign;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SalesView extends VerticalLayout implements View {

	private final Chart timeline;

	private static final SolidColor[] COLORS = new SolidColor[] {
			new SolidColor(52, 154, 255), 
			new SolidColor(242, 81, 57),
			new SolidColor(255, 201, 35), 
			new SolidColor(83, 220, 164),
			new SolidColor(30, 125, 204),
			new SolidColor(150, 10, 85)
			};
	private static final SolidColor[] COLORS_ALPHA = new SolidColor[] {
			new SolidColor(52, 154, 255, 0.3),
			new SolidColor(242, 81, 57, 0.3),
			new SolidColor(255, 201, 35, 0.3),
			new SolidColor(83, 220, 164, 0.3),
			new SolidColor(30, 125, 204, 0.3),
			new SolidColor(150, 10, 85, 0.3)
			};
	private int colorIndex = 0;

	String[] movies = new String[] { "Left Behind", "The Maze Runner",
			"The Equalizer", "Annabelle", "Gone Girl", "The Boxtrolls" };

	public SalesView() {
		Label titleLabel = new Label("Revenue by Movie");
		titleLabel.addStyleName(ValoTheme.LABEL_H1);

		timeline = new Chart(ChartType.LINE);
		timeline.setTimeline(true);
		timeline.getConfiguration().getTooltip().setValueDecimals(2);
		timeline.getConfiguration().getRangeSelector().setEnabled(false);

		Legend legend = new Legend();
		legend.setEnabled(true);
		legend.setVerticalAlign(VerticalAlign.TOP);
		legend.setAlign(HorizontalAlign.RIGHT);
		timeline.getConfiguration().setLegend(legend);
		
		PlotOptionsLine plotOptionsLine = new PlotOptionsLine();
		Marker marker = new Marker();
		marker.setEnabled(false);
		marker.setSymbol(MarkerSymbolEnum.CIRCLE);
		plotOptionsLine.setMarker(marker);
		timeline.getConfiguration().setPlotOptions(plotOptionsLine);
		timeline.setSizeFull();
		for (String movie : movies) {
			addDataSet(movie);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -2);
		timeline.getConfiguration().getxAxis().setMin(calendar.getTimeInMillis());
		timeline.getConfiguration().getxAxis().setMax(new Date().getTime());
		setSizeFull();
		addComponents(titleLabel);
		addComponent(timeline);
		setExpandRatio(timeline, 1);
		
	}

	private void addDataSet(String movie) {
		List<Transaction> dailyRevenue = generateTransactions();

		ListContainer<Transaction> dailyRevenueContainer = new ListContainer<Transaction>(
				dailyRevenue);

		dailyRevenueContainer.sort(new Object[] { "time" },
				new boolean[] { true });

		ContainerDataSeries containerSeries = new ContainerDataSeries(dailyRevenueContainer);
		containerSeries.setYPropertyId("price");
		containerSeries.setXPropertyId("time");
		List<Series> series = new ArrayList<Series>(timeline.getConfiguration().getSeries());
		series.add(containerSeries);
		
		timeline.getConfiguration().setSeries(series);
		 
		PlotOptionsLine options = new PlotOptionsLine();
		containerSeries.setPlotOptions(options);
		options.setColor(COLORS[colorIndex]);
		containerSeries.setName(movie); 
		colorIndex++;
	}

	private List<Transaction> generateTransactions() {
		List<Transaction> transactions = new ArrayList<Transaction>();
		Random rand = new Random();

		double multiplier = rand.nextDouble() * 2;
		Calendar date = Calendar.getInstance();
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		int daysSubtractor = rand.nextInt(150) + 30;
		date.add(Calendar.DAY_OF_YEAR, -daysSubtractor);

		Calendar today = Calendar.getInstance();
		double total = 0;
		while (date.before(today)) {
			Transaction transaction = new Transaction();
			total = total + rand.nextDouble() * 500 * multiplier + 500;

			transaction.setTime(date.getTime());
			transaction.setPrice(total);

			transactions.add(transaction);
			date.add(Calendar.DAY_OF_MONTH, rand.nextInt(3) + 1);
		}
		return transactions;
	}

	public final class Transaction {
		private Date time;
		private double price;

		public Date getTime() {
			return time;
		}

		public void setTime(final Date time) {
			this.time = time;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(final double price) {
			this.price = price;
		}

	}

	@Override
	public void enter(final ViewChangeEvent event) {
	}
}