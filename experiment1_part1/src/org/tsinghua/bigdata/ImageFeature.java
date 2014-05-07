package org.tsinghua.bigdata;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.semanticmetadata.lire.imageanalysis.ColorLayout;
import net.semanticmetadata.lire.imageanalysis.EdgeHistogram;
import net.semanticmetadata.lire.imageanalysis.OpponentHistogram;
import net.semanticmetadata.lire.imageanalysis.Tamura;
import net.semanticmetadata.lire.imageanalysis.joint.JointHistogram;

public class ImageFeature {
	public BufferedImage image;

	public ImageFeature(BufferedImage image) {
		this.image = image;
	}

	public double[] colorLayout() {
		ColorLayout colorLayout = new ColorLayout();
		colorLayout.extract(this.image);
		return colorLayout.getDoubleHistogram();
	}

	public double[] edgeHistogram() {
		EdgeHistogram eh = new EdgeHistogram();
		eh.extract(this.image);
		return eh.getDoubleHistogram();
	}

	public double[] jointHistogram() {
		JointHistogram jh = new JointHistogram();
		jh.extract(this.image);
		return jh.getDoubleHistogram();
	}

	public double[] opponentHistogram() {
		OpponentHistogram oh = new OpponentHistogram();
		oh.extract(this.image);
		return oh.getDoubleHistogram();
	}

	public double[] tamura() {
		Tamura t = new Tamura();
		t.extract(this.image);
		return t.getDoubleHistogram();
	}

	private JSONArray ToJSONArray(double[] x) {
		JSONArray array = new JSONArray();
		for (double d : x) {
			array.add(d);
		}
		return array;
	}

	public String toJSONString() {
		Map<String, double[]> map = new HashMap<String, double[]>();

		JSONObject json = new JSONObject();
		json.put("colorLayout", ToJSONArray(colorLayout()));
		json.put("edgeHistogram", ToJSONArray(edgeHistogram()));
		json.put("jointHistogram", ToJSONArray(jointHistogram()));
		json.put("opponentHistogram", ToJSONArray(opponentHistogram()));
		json.put("tamura", ToJSONArray(tamura()));
		return json.toJSONString();

	}
}
