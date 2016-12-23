package th.co.dtac.worldclasspdf;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;

import th.co.dtac.worldclasspdf.Utils.DownloadFile;
import th.co.dtac.worldclasspdf.Utils.DownloadFileUrlConnectionImpl;
import th.co.dtac.worldclasspdf.Utils.FileUtil;


public class PdfViewActivity extends AppCompatActivity implements DownloadFile.Listener, OnPageChangeListener, OnLoadCompleteListener {

    private static final String EXTRA_PDF_URL = "pdfUrl";
    /**
     * Default PDF url
     */
    private final String pdfUrlDefault = "http://www.axmag.com/download/pdfurl-guide.pdf";
    private Toolbar toolbar;
    private PDFView pdfView;
    private Intent intentUrl;
    private String pdfUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        intentUrl = getIntent();
        pdfUrl = intentUrl.getStringExtra(EXTRA_PDF_URL) == null ? this.pdfUrlDefault : intentUrl.getStringExtra(EXTRA_PDF_URL);

        pdfView = (PDFView) findViewById(R.id.pdfView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        downloadPdf(pdfUrl);
    }

    private void downloadPdf(String inPdfUrl) {
        DownloadFile downloadFile = new DownloadFileUrlConnectionImpl(this, new Handler(), this);
        downloadFile.download(inPdfUrl, new File(this.getCacheDir(), FileUtil.extractFileNameFromURL(inPdfUrl)).getAbsolutePath());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.menu_close) {
            finish();
        } else if (itemId == R.id.menu_share) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            CharSequence[] itemsAlert = {"Copy link", "Open browser"};

            builder.setItems(itemsAlert, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int itemIndex) {
                    final int COPY_LINK = 0;
                    final String label = "URL";

                    if (itemIndex == COPY_LINK) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText(label, pdfUrl);
                        clipboard.setPrimaryClip(clip);
                        return;
                    }

                    Intent intentBrowser = new Intent(Intent.ACTION_VIEW);
                    intentBrowser.setData(Uri.parse(pdfUrl));
                    startActivity(intentBrowser);
                }
            });
            builder.show();

        }
        return true;
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