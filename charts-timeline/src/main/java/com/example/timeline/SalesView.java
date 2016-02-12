package com.example.timeline;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.timeline.Timeline;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SalesView extends VerticalLayout implements View {

	private final Timeline timeline;

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

	Random random = new Random(12345678);
	
	public SalesView() {
		Label titleLabel = new Label("Revenue by Movie");
		titleLabel.addStyleName(ValoTheme.LABEL_H1);

		timeline = new Timeline();
		timeline.setDateSelectVisible(false);
		timeline.setChartModesVisible(false);
		timeline.setGraphShadowsEnabled(false);
		timeline.setZoomLevelsVisible(false);
		timeline.setSizeFull();
		timeline.setNoDataSourceCaption("<span class=\"v-label h2 light\">Add a data set from the dropdown above</span>");
		for (String movie : movies) {
			addDataSet(movie);
		}

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -2);
		if (timeline.getGraphDatasources().size() > 0) {
			timeline.setVisibleDateRange(calendar.getTime(), new Date());
		}

		setSizeFull();
		addComponents(titleLabel);
		addComponent(timeline);
		setExpandRatio(timeline, 1);
		
	}

	private void addDataSet(String movie) {
		List<Transaction> dailyRevenue = generateTransactions();

		BeanItemContainer<Transaction> dailyRevenueContainer = new BeanItemContainer<SalesView.Transaction>(Transaction.class, dailyRevenue);

		dailyRevenueContainer.sort(new Object[] { "time" },
				new boolean[] { true });

		timeline.addGraphDataSource(dailyRevenueContainer, "time",
				"price");
		
		timeline.setGraphOutlineColor(dailyRevenueContainer, COLORS[colorIndex]);
		timeline.setBrowserOutlineColor(dailyRevenueContainer,
				COLORS[colorIndex]);
		timeline.setBrowserFillColor(dailyRevenueContainer,
				COLORS_ALPHA[colorIndex]);
		timeline.setGraphCaption(dailyRevenueContainer, movie);
		timeline.setEventCaptionPropertyId("date");
		timeline.setVerticalAxisLegendUnit(dailyRevenueContainer, "$");
		colorIndex++;
	}

	private List<Transaction> generateTransactions() {
		List<Transaction> transactions = new ArrayList<Transaction>();

		double multiplier = random.nextDouble() * 2;
		Calendar date = Calendar.getInstance();
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		int daysSubtractor = random.nextInt(150) + 30;
		date.add(Calendar.DAY_OF_YEAR, -daysSubtractor);

		Calendar today = Calendar.getInstance();
		double total = 0;
		while (date.before(today)) {
			Transaction transaction = new Transaction();
			total = total + random.nextDouble() * 500 * multiplier + 500;

			transaction.setTime(date.getTime());
			transaction.setPrice(total);

			transactions.add(transaction);
			date.add(Calendar.DAY_OF_MONTH, random.nextInt(3) + 1);
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