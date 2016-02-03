package com.example.sparklines;

import javax.servlet.annotation.WebServlet;

import com.vaadin.addon.charts.model.style.Color;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.example.sparklines.MyAppWidgetset")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);
        layout.addComponent(buildSparklines());
    }
    
    private Component buildSparklines() {
        CssLayout sparks = new CssLayout();
        sparks.addStyleName("sparks");
        sparks.setWidth("100%");
        Responsive.makeResponsive(sparks);

        Color[] chartColors = new Color[] {
                new SolidColor("#3090F0"), new SolidColor("#18DDBB"),
                new SolidColor("#98DF58"), new SolidColor("#F9DD51"),
                new SolidColor("#F09042"), new SolidColor("#EC6464") };
        
        SparklineChart s = new SparklineChart("Traffic", "K", "",
                chartColors[0], 22, 20, 80);
        sparks.addComponent(s);

        s = new SparklineChart("Revenue / Day", "M", "$",
                chartColors[2], 8, 89, 150);
        sparks.addComponent(s);

        s = new SparklineChart("Checkout Time", "s", "",
                chartColors[3], 10, 30, 120);
        sparks.addComponent(s);

        s = new SparklineChart("Theater Fill Rate", "%", "",
                chartColors[5], 50, 34, 100);
        sparks.addComponent(s);

        return sparks;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
