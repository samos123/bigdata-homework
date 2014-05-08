package org.tsinghua.bigdata;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.ImageSearcherFactory;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

public class Searcher {
	public ImageSearcher ceddSearcher;
	public ImageSearcher edgeSearcher;
	public ImageSearcher opponentSearcher;
	public BufferedImage img;
	public IndexReader ir;
	Map<String, Double> dm;
	
	public Searcher(BufferedImage img) throws IOException {
        ceddSearcher = ImageSearcherFactory.createCEDDImageSearcher(100);
        edgeSearcher = ImageSearcherFactory.createEdgeHistogramImageSearcher(100);
        opponentSearcher = ImageSearcherFactory.createOpponentHistogramSearcher(100);
        ir = DirectoryReader.open(FSDirectory.open(new File("index")));
        this.img = img;
        dm = new HashMap<String, Double>();
	}

	
	private String extractVideoName(String fileName) {
        String[] parts = fileName.split("/");
        fileName = parts[parts.length - 1];
        return fileName.split("\\.")[0];		
	}
	
	private void extractScores(ImageSearchHits hits) {
        for (int i = 0; i < hits.length(); i++) {
            String fileName = hits.doc(i).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
            String videoName = this.extractVideoName(fileName);
            float score = hits.score(i);
            double newValue;
            if (dm.containsKey(videoName)) {
            	newValue = dm.get(videoName) + (1.0 / (score + 0.5));
            } else {
            	newValue = (1.0 / (score + 1));
            }
            dm.put(videoName, newValue);
            
            //System.out.println(hits.score(i) + ": \t" + this.extractVideoName(fileName));
        }
	}
	
	public void topMovies() throws IOException {
        extractScores(ceddSearcher.search(img, ir));
        extractScores(edgeSearcher.search(img, ir));
        extractScores(opponentSearcher.search(img, ir));
        dm = MapUtil.sortByValue(dm);
        for(Map.Entry<String, Double> entry : dm.entrySet()) {
        	System.out.println(entry.getKey() +": \t" + entry.getValue().toString());
        }
	}
	
	
	
	
    public static void main(String[] args) throws IOException {
        // Checking if arg[0] is there and if it is an image.
        BufferedImage img = null;
        boolean passed = false;
        if (args.length > 0) {
            File f = new File(args[0]);
            if (f.exists()) {
                try {
                    img = ImageIO.read(f);
                    passed = true;
                } catch (IOException e) {
                    e.printStackTrace();  
                }
            }
        }
        if (!passed) {
            System.out.println("No image given as first argument.");
            System.out.println("Run \"Searcher <query image>\" to search for <query image>.");
            System.exit(1);
        }
        
        new Searcher(img).topMovies();

 

    }
}
