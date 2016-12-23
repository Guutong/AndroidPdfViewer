package xyz.guutong.androidpdfviewer;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;

import xyz.guutong.androidpdfviewer.Utils.DownloadFile;
import xyz.guutong.androidpdfviewer.Utils.DownloadFileUrlConnectionImpl;
import xyz.guutong.androidpdfviewer.Utils.FileUtil;

public class PdfViewActivity extends AppCompatActivity implements DownloadFile.Listener, OnPageChangeListener, OnLoadCompleteListener {

    private PDFView pdfView;
    /**
     * Default PDF url
     */
    private String pdfUrl = "http://www.axmag.com/download/pdfurl-guide.pdf";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        pdfView = (PDFView) findViewById(R.id.pdfView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String inPdfUrl = intent.getStringExtra(MainActivity.EXTRA_PDF_URL) == null ? pdfUrl : intent.getStringExtra("pdfUrl");
        DownloadFile downloadFile = new DownloadFileUrlConnectionImpl(this, new Handler(), this);
        downloadFile.download(inPdfUrl, new File(this.getCacheDir(), FileUtil.extractFileNameFromURL(inPdfUrl)).getAbsolutePath());
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        File pdf = new File(destinationPath);

        pdfView.fromFile(pdf)
                .defaultPage(0)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    public void onFailure(Exception e) {

    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }
}