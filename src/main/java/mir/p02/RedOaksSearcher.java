package mir.p02;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import net.semanticmetadata.lire.builders.DocumentBuilder;
import net.semanticmetadata.lire.imageanalysis.features.global.SimpleColorHistogram;
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher;
import net.semanticmetadata.lire.searchers.ImageSearchHits;
import net.semanticmetadata.lire.searchers.ImageSearcher;

/**
 * Searches image database for images similar to queryImg. <br>
 * Group: <br>
 * Tetiana Lavynska <br>
 * Jan-Ole Perschewski <br>
 * Pavlo Shevchenko
 */
public class RedOaksSearcher {
    /**
     * Finds images similar to queryPath using index at idxPath.
     */
    public static void search(File queryPath, File idxPath) {
        BufferedImage queryImg = null;
        if (queryPath.exists()) {
            try {
                queryImg = ImageIO.read(queryPath);
            } catch (IOException e) {
                System.out.println("Cannot load image " + queryPath.getAbsolutePath() + ". Try again.");
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            System.out.println(queryPath.getAbsolutePath() + " does not exist. Try again.");
            System.exit(1);
        }
        try {
            // open index
            IndexReader ir = DirectoryReader.open(FSDirectory.open(Paths.get(idxPath + "/index")));
            // init searcher: return top 10 results + use color histogram to find results in
            // index
            SimpleColorHistogram.DEFAULT_DISTANCE_FUNCTION = SimpleColorHistogram.DistanceFunction.L1;
            ImageSearcher searcher = new GenericFastImageSearcher(10, SimpleColorHistogram.class);

            // retrieve and print out results
            System.out.println("\nResults:");
            ImageSearchHits hits = searcher.search(queryImg, ir);
            for (int i = 0; i < hits.length(); i++) {
                String fileName = ir.document(hits.documentID(i)).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
                System.out.println("\tRank: " + (i + 1));
                System.out.println("\tDistance: " + hits.score(i));
                System.out.println("\tFile: " + fileName);
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
