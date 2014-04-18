package org.tsinghua.bigdata;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.imageanalysis.ColorLayout;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.Mapper;


public class ImageMapper extends MapReduceBase implements Mapper<Object, Text, Text, Text> {


	@Override
	public void map(Object key, Text value,
			OutputCollector<Text, Text> arg2, Reporter reporter)
			throws IOException {
		String imageFilePath = value.toString();
		FileSystem fs = FileSystem.get(new Configuration());
		
		String namenode = "hdfs://pc201:9000";
		FSDataInputStream fsd = fs.open(new Path(namenode + imageFilePath));
		BufferedImage image = ImageIO.read(fsd);

		ColorLayout colorLayout = new ColorLayout();
		colorLayout.extract(image);
		double[] histogram = colorLayout.getDoubleHistogram();
		reporter.setStatus("Test Sam: " + imageFilePath);
		
		String feature = imageFilePath.replaceFirst("-images", "-features");
		feature = feature.substring(0, feature.length() - 4);
		FSDataOutputStream fos = fs.create(new Path(namenode + feature));
		DataOutputStream dos = new DataOutputStream(fos);
		for (double d : histogram) {
			dos.writeDouble(d);
		}
		dos.flush();
		dos.close();
		fos.close();

	
	}
}
