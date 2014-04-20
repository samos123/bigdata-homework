package org.tsinghua.bigdata;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.imageanalysis.ColorLayout;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("rawtypes")
public class ImageMapper extends MapReduceBase implements
		Mapper<Object, Text, NullOutputFormat, NullOutputFormat> {

	@Override
	public void map(Object key, Text value,
			OutputCollector<NullOutputFormat, NullOutputFormat> arg2,
			Reporter reporter) throws IOException {
		String imageFilePath = value.toString();
		FileSystem fs = FileSystem.get(new Configuration());

		// Download the image from hdfs and store it into memory
		String namenode = "hdfs://pc201:9000";
		FSDataInputStream fsd = fs.open(new Path(namenode + imageFilePath));
		ImageIO.setUseCache(false);
		BufferedImage image = ImageIO.read(fsd);
		
		// Extract 5 features see ImageFeature for details
		ImageFeature imgFeature = new ImageFeature(image);
		
		// Replace the word images with features in the directory path
		// of the newly created feature files. Also append .json
		String feature = imageFilePath.replaceFirst("-images", "-features");
		feature = feature.substring(0, feature.length() - 4);
		feature = feature + ".json";
		// Create a new file on HDFS
		FSDataOutputStream fos = fs.create(new Path(namenode + feature));
		
		// Write the JSON string to our created file on HDFS
		BufferedWriter br = new BufferedWriter( new OutputStreamWriter( fos, "UTF-8" ) );
		br.write(imgFeature.toJSONString());
		br.close();
		fos.close();
	}

	public static void main(String[] args) {
		JobConf conf = new JobConf(ImageMapper.class);
		conf.setJobName("ImageMapper");

		conf.setOutputKeyClass(NullOutputFormat.class);
		conf.setOutputValueClass(NullOutputFormat.class);

		FileInputFormat.addInputPath(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		conf.setMapperClass(ImageMapper.class);
		conf.setNumReduceTasks(0);

		try {
			JobClient.runJob(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
