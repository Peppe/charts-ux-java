package com.example.timeline;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Movie {
	private String title;
	private Collection<MovieRevenue> revenues;
	
	private static Random rand = new Random();

	public Movie(String title) {
		this.title = title;
		this.revenues = countMovieRevenue();
	}
	
	public String getTitle() {
		return title;
	}
	
	public Collection<MovieRevenue> getRevenues() {
		return revenues;
	}
	
	private Collection<MovieRevenue> countMovieRevenue() {
        Map<Date, Double> dailyIncome = new HashMap<Date, Double>();
        List<Transaction> transactions = generateTransactions();
        for (Transaction transaction : transactions) {
            Date day = getDay(transaction.getTime());

            Double currentValue = dailyIncome.get(day);
            if (currentValue == null) {
                currentValue = 0.0;
            }
            dailyIncome.put(day, currentValue + transaction.getPrice());
        }

        Collection<MovieRevenue> result = new ArrayList<MovieRevenue>();

        List<Date> dates = new ArrayList<Date>(dailyIncome.keySet());
        Collections.sort(dates);

        double revenueSoFar = 0.0;
        for (Date date : dates) {
            revenueSoFar += dailyIncome.get(date);
            MovieRevenue movieRevenue = new MovieRevenue(date, revenueSoFar);
            result.add(movieRevenue);
        }

        return result;
    }
	   
    private List<Transaction> generateTransactions() {
    	List<Transaction> transactions = new ArrayList<Transaction>();
    	
        Calendar cal = Calendar.getInstance();
        int daysSubtractor = rand.nextInt(150) + 30;
        cal.add(Calendar.DAY_OF_YEAR, -daysSubtractor);

        Calendar lastDayOfWeek = Calendar.getInstance();
        lastDayOfWeek.add(Calendar.DAY_OF_YEAR,
                Calendar.SATURDAY - cal.get(Calendar.DAY_OF_WEEK));

        while (cal.before(lastDayOfWeek)) {

            int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
            if (hourOfDay > 10 && hourOfDay < 22) {
                Transaction transaction = new Transaction();
                transaction.setTime(cal.getTime());
                int seats = (int) (1 + rand.nextDouble() * 3);
                double price = seats * (2 + (rand.nextDouble() * 8));
                transaction.setPrice(price);
                transactions.add(transaction);
            }

            cal.add(Calendar.SECOND, rand.nextInt(500000) + 5000);
        }
        return transactions;
	}
	
    private Date getDay(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTime();
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

    public final class MovieRevenue {

        private Date timestamp;
        private Double revenue;
        
        public MovieRevenue(Date timestamp, Double revenue) {
			this.timestamp = timestamp;
			this.revenue = revenue;
		}
        
        public Date getTimestamp() {
			return timestamp;
		}
        
        public Double getRevenue() {
			return revenue;
		}
    }
}
