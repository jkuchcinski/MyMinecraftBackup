package kuchcinski.com.myminecraftbackup;

import android.app.NotificationManager;
import android.content.Context;

import net.lingala.zip4j.core.*;
import net.lingala.zip4j.model.*;
import net.lingala.zip4j.util.*;

import java.io.File;

/**
 * Created by Jaroslaw on 31.10.2017.
 */

public class ZIPUtils {

    private ZIPUtils() {
    }

    public static boolean zipFileAtPath(String folderToAdd, String toLocation) {
        try {

            // Initiate ZipFile object with the path/name of the zip file.
            ZipFile zipFile = new ZipFile(toLocation);

            // Initiate Zip Parameters which define various properties such
            // as compression method, etc.
            ZipParameters parameters = new ZipParameters();

            // set compression method to store compression
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

            // Set the compression level
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            // Add folder to the zip file
            zipFile.addFolder(folderToAdd, parameters);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new File(toLocation).exists();
    }
}


