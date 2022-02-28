package in.bbd.pritesh.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Component;

import in.bbd.pritesh.model.Uom;

@Component
public class UomUtil {

	//1. create Pie Chart
	public void generatePie(String path,List<Object[]> data) {
		//a. Create DataSet for Pie and add data to it
		DefaultPieDataset dataset = new DefaultPieDataset();

		for(Object[] ob:data) {
			//setValue(key[string],val[double])
			dataset.setValue(
					String.valueOf(ob[0]), 
					Double.valueOf(ob[1].toString())
					);
		}

		//b. Create JFreeCharts object with dataset and other details
		JFreeChart chart = ChartFactory.createPieChart3D("UOM TYPE AND COUNT", dataset);

		//read chart area object
		PiePlot plot = (PiePlot) chart.getPlot();

		PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator("{0} : {1} ({2}) ",new DecimalFormat("0"),new DecimalFormat("0%"));
		plot.setLabelGenerator(gen);

		//c. convert JFreeCharts object as image
		try {
			ChartUtils.saveChartAsJPEG(
					new File(path+"/uomA.jpg"), //path + file name
					chart, 500, 400); // chart obj, width, height
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//2. create Bar Chart
	public void generateBar(String path,List<Object[]> data) {
		//a. Create DataSet for Bar and add data to it
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for(Object[] ob:data) {
			//val,key, label to display
			dataset.setValue(
					Double.valueOf(ob[1].toString()),  //value
					String.valueOf(ob[0]),  //key
					""); // label
		}

		//b. Create JFreeCharts object with dataset and other details
		// title, x-axis label, y-axis label, dataset
		JFreeChart chart = ChartFactory.createBarChart("UOM TYPE COUNT", "UOM TYPE", "COUNT", dataset);

		//c. convert JFreeCharts object as image
		try {
			ChartUtils.saveChartAsJPEG(
					new File(path+"/uomB.jpg"), //path + file name
					chart, 500, 500); // chart obj, width, height
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void copyNonNullValues(Uom dbUom, Uom uom) {
		if(uom.getUomModel()!=null) dbUom.setUomModel(uom.getUomModel());
		if(uom.getUomType()!=null) dbUom.setUomType(uom.getUomType());
		if(uom.getDescription()!=null) dbUom.setDescription(uom.getDescription());
	}
}
