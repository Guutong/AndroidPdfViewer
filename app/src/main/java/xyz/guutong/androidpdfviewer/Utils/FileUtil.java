package xyz.guutong.androidpdfviewer.Utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* credit by https://github.com/voghDev/PdfViewPager */
public class FileUtil {
    public static boolean copyAsset(Context ctx, String assetName, String destinationPath) throws IOException {
        InputStream in = ctx.getAssets().open(assetName);
        File f = new File(destinationPath);
        f.createNewFile();
        OutputStream out = new FileOutputStream(new File(destinationPath));

        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
        in.close();
        out.close();

        return true;
    }

    public static String extractFileNameFromURL(String url){
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
