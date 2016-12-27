package xyz.guutong.pdfviewerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import xyz.guutong.androidpdfviewer.PdfViewActivity;

public class MainActivity extends AppCompatActivity {
    private EditText mPdfUrl;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPdfUrl = (EditText) findViewById(R.id.pdfUrl);
        Button mOpenPdf = (Button) findViewById(R.id.openPdf);
        intent = new Intent(this, PdfViewActivity.class);

        mOpenPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(PdfViewActivity.EXTRA_PDF_URL, mPdfUrl.getText().toString());
                startActivity(intent);
            }
        });

    }
}
