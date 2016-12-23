package xyz.guutong.androidpdfviewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mPdfUrl;
    private Toolbar toolbar;
    private Button mOpenPdf;

    public final static String EXTRA_PDF_URL = "pdfUrl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPdfUrl = (EditText) findViewById(R.id.mPdfUrl);
        mOpenPdf = (Button) findViewById(R.id.mOpenPdf);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        mOpenPdf.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mOpenPdf) {
            Intent intent = new Intent(this, PdfViewActivity.class);
            intent.putExtra(EXTRA_PDF_URL, mPdfUrl.getText().toString());
            startActivity(intent);
        }
    }
}
