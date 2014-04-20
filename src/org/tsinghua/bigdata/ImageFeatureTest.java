package org.tsinghua.bigdata;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

public class ImageFeatureTest {

	public ImageFeature imgFeature;
	@Before
	public void setUp() throws Exception {
		BufferedImage img = ImageIO.read(new File("test_image.jpg"));
		this.imgFeature = new ImageFeature(img);
	}

	@Test
	public void test_to_json() {
		String json = this.imgFeature.toJSONString();
		assertTrue(json.startsWith("{\"opponentHistogram\":[0.0,0.0,0.0,0.0,0.0,7.0,"));
	}

}
