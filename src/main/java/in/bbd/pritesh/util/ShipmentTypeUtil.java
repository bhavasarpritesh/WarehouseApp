package in.bbd.pritesh.util;

import java.awt.Color;
import java.io.File;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Component;

@Component
public class ShipmentTypeUtil {

	//1. generate Pie chart
	public void generatePie(String path,List<Object[]> data) {
		//a. Create data set
		DefaultPieDataset dataset = new DefaultPieDataset();
		//add data to dataset key(String),value(Double)
		for(Object[] ob:data) {
			dataset.setValue(String.valueOf(ob[0]), 
					Double.valueOf(ob[1].toString()));
		}

		//b. create JFreeChart object using ChartFactory
		JFreeChart chart = ChartFactory.createPieChart("SHIPMENT TYPE MODE COUNT", dataset);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);
		/*

		//plot.setSectionPaint("AIR", Color.ORANGE);
		plot.setSectionPaint("AIR", new Color(182, 147, 250));
		plot.setSectionPaint("TRUCK", Color.RED);
		//plot.setSectionPaint("TRUCK", new Color(115, 204, 251));
		plot.setSectionPaint("SHIP", Color.PINK);
		plot.setSectionPaint("TRAIN", Color.MAGENTA);
		 */
		//c. convert JFreeChart into image
		try {
			ChartUtils.saveChartAsJPEG(new File(path+"/shipmntA.jpg"), chart, 500, 400);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	//2. generate Bar chart
	public void generateBar(String path,List<Object[]> data) {
		//a. Create data set
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		//add data to dataset  value(Double),key(String),label(String)
		for(Object[] ob:data) {
			dataset.setValue(
					Double.valueOf(ob[1].toString()),
					String.valueOf(ob[0]),"");
		}
		//b. create JFreeChart object using ChartFactory
		JFreeChart chart = ChartFactory.createBarChart("SHIPMENT TYPE MODE COUNT", "TYPE","COUNT", dataset);
		
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.LIGHT_GRAY);
		
		//c. convert JFreeChart into image
		try {
			ChartUtils.saveChartAsPNG(new File(path+"/shipmntB.png"), chart, 500, 500);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}
