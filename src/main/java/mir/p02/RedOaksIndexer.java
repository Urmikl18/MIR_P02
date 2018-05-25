package mir.p02;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import net.semanticmetadata.lire.builders.GlobalDocumentBuilder;
import net.semanticmetadata.lire.imageanalysis.features.global.SimpleColorHistogram;
import net.semanticmetadata.lire.utils.FileUtils;

/**
 * Indexes image database using color histogram. <br>
 * Group: <br>
 * Tetiana Lavynska <br>
 * Jan-Ole Perschewski <br>
 * Pavlo Shevchenko
 */
public class RedOaksIndexer {

    /**
     * Creates index for image database.
     */
    public static void indexFolder(File imgDatabase, File idxPath) {
        // check if imgDatabase is a folder
        if (!(imgDatabase.exists() && imgDatabase.isDirectory())) {
            System.out.println(imgDatabase.getAbsolutePath() + " is not a folder! Try again.");
            System.exit(1);
        }
        // check if index already exists
        File tmp = new File(idxPath.getAbsolutePath() + "index");
        if (tmp.exists()) {
            System.out.println("Index at " + idxPath.getAbsolutePath() + " already exists.");
            try {
                org.apache.commons.io.FileUtils.deleteDirectory(tmp);
            } catch (IOException e) {
                System.out.println("Cannot delete index.");
                e.printStackTrace();
                System.exit(1);
            }
            System.out.println("Old index deleted successfully.");
        }
        try {
            // load all images from the database
            ArrayList<String> images = FileUtils.getAllImages(imgDatabase, true);

            // use color histogram to create index
            GlobalDocumentBuilder globalDocumentBuilder = new GlobalDocumentBuilder(false, false);
            globalDocumentBuilder.addExtractor(SimpleColorHistogram.class);

            // init index writer
            IndexWriterConfig conf = new IndexWriterConfig(new WhitespaceAnalyzer());
            IndexWriter iw = new IndexWriter(FSDirectory.open(Paths.get(idxPath + "/index")), conf);
            // add all images to the index
            int idxCount = 1;
            System.out.println("\nStart indexing image database:");
            for (Iterator<String> it = images.iterator(); it.hasNext();) {
                String imageFilePath = it.next();
                System.out.println("[" + (idxCount++) + " / " + (images.size()) + "]: " + imageFilePath);
                try {
                    BufferedImage img = ImageIO.read(new FileInputStream(imageFilePath));
                    Document document = globalDocumentBuilder.createDocument(img, imageFilePath);
                    iw.addDocument(document);
                } catch (Exception e) {
                    System.err.println("Error while indexing image database.");
                    e.printStackTrace();
                }
            }
            iw.close();
            System.out.println("Finished indexing image database.");
        } catch (IOException e) {
            System.out.println("Cannot access image database.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
