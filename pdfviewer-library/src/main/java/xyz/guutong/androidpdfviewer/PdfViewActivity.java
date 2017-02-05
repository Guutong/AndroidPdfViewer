package xyz.guutong.androidpdfviewer;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;

import xyz.guutong.androidpdfviewer.Utils.DownloadFile;
import xyz.guutong.androidpdfviewer.Utils.DownloadFileUrlConnectionImpl;
import xyz.guutong.androidpdfviewer.Utils.FileUtil;

public class PdfViewActivity extends AppCompatActivity implements DownloadFile.Listener, OnPageChangeListener, OnLoadCompleteListener {

    public static final String EXTRA_PDF_URL = "EXTRA_PDF_URL";
    public static final String EXTRA_PDF_TITLE = "EXTRA_PDF_TITLE";
    public static final String EXTRA_SHOW_SCROLL = "EXTRA_SHOW_SCROLL";
    public static final String EXTRA_SWIPE_HORIZONTAL = "EXTRA_SWIPE_HORIZONTAL";
    public static final String EXTRA_SHOW_SHARE_BUTTON = "EXTRA_SHOW_SHARE_BUTTON";
    public static final String EXTRA_SHOW_CLOSE_BUTTON = "EXTRA_SHOW_CLOSE_BUTTON";
    public static final String EXTRA_TOOLBAR_COLOR = "EXTRA_TOOLBAR_COLOR";

    private static final int MENU_CLOSE = Menu.FIRST;
    private static final int MENU_SHARE = Menu.FIRST + 1;

    private Toolbar toolbar;
    private com.github.barteksc.pdfviewer.PDFView pdfView;
    private Intent intentUrl;
    private ProgressBar progressBar;
    private String pdfUrl;
    private Boolean showScroll;
    private Boolean swipeHorizontal;
    private String toolbarColor = "#1191d5";
    private String toolbarTitle;
    private Boolean showShareButton;
    private Boolean showCloseButton;

    private DefaultScrollHandle scrollHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        intentUrl = getIntent();
        pdfUrl = intentUrl.getStringExtra(EXTRA_PDF_URL);
        toolbarTitle = intentUrl.getStringExtra(EXTRA_PDF_TITLE) == null ? "" : intentUrl.getStringExtra(EXTRA_PDF_TITLE);
        toolbarColor = intentUrl.getStringExtra(EXTRA_TOOLBAR_COLOR) == null ? toolbarColor : intentUrl.getStringExtra(EXTRA_TOOLBAR_COLOR);
        showScroll = intentUrl.getBooleanExtra(EXTRA_SHOW_SCROLL, false);
        swipeHorizontal = intentUrl.getBooleanExtra(EXTRA_SWIPE_HORIZONTAL, false);
        showShareButton = intentUrl.getBooleanExtra(EXTRA_SHOW_SHARE_BUTTON, true);
        showCloseButton = intentUrl.getBooleanExtra(EXTRA_SHOW_CLOSE_BUTTON, true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        pdfView = (com.github.barteksc.pdfviewer.PDFView) findViewById(R.id.pdfView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        /* set color colorPrimaryDark*/
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(toolbarColor), hsv);
        hsv[2] *= 0.8f;
        int colorPrimaryDark = Color.HSVToColor(hsv);
        if (Build.VERSION.SDK_INT >= 21) {
            this.getWindow().setStatusBarColor(colorPrimaryDark);
        }

        toolbar.setBackgroundColor(Color.parseColor(toolbarColor));
        toolbar.setTitle(toolbarTitle);

        if (showScroll) {
            scrollHandle = new DefaultScrollHandle(this);
        }

        setSupportActionBar(toolbar);

        progressBar.setVisibility(View.VISIBLE);

        downloadPdf(pdfUrl);
    }

    private void downloadPdf(String inPdfUrl) {
        try {
            DownloadFile downloadFile = new DownloadFileUrlConnectionImpl(this, new Handler(), this);
            downloadFile.download(inPdfUrl, new File(this.getCacheDir(), FileUtil.extractFileNameFromURL(inPdfUrl)).getAbsolutePath());
        } catch (Exception e) {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (showShareButton)
            menu.add(0, MENU_SHARE, Menu.NONE, R.string.share)
                    .setIcon(R.drawable.ic_share)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        if (showCloseButton)
            menu.add(0, 1, MENU_CLOSE, R.string.close)
                    .setIcon(R.drawable.ic_close)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == MENU_CLOSE) {
            finish();
        } else if (i == MENU_SHARE) {
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
                .scrollHandle(scrollHandle)
                .swipeHorizontal(swipeHorizontal)
                .load();
    }

    @Override
    public void onFailure(Exception e) {
        progressBar.setVisibility(View.GONE);
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setMessage("Cannot open file!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        alert.show();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }

    @Override
    public void loadComplete(int nbPages) {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }
}