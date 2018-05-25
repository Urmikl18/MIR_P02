package mir.p02;

import java.io.File;
import java.io.IOException;

/**
 * 
 * Simple image retrieval system that uses information about the dye
 * distribution in pictures. <br>
 * Group: <br>
 * Tetiana Lavynska <br>
 * Jan-Ole Perschewski <br>
 * Pavlo Shevchenko
 */
public class RedOaks {
    public static void main(String[] args) throws IOException {
        // Requires 3 input parameters (folder with images + folder to store index +
        // example to query)
        if (args.length != 3) {
            System.out.println("Wrong parameters! Try again:");
            System.out.println("java -jar MIR_P02.jar [image_data_base_path] [index_path] [query_file_path]");
            return;
        }
        File imgDatabase = new File(args[0]);
        File idxPath = new File(args[1]);
        File queryPath = new File(args[2]);

        RedOaksIndexer.indexFolder(imgDatabase, idxPath);
        RedOaksSearcher.search(queryPath, idxPath);
    }
}
