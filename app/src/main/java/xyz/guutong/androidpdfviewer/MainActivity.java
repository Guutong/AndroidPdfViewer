package xyz.guutong.androidpdfviewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mPdfUrl;
    private Button mOpenPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mPdfUrl = (EditText) findViewById(R.id.mPdfUrl);
        mOpenPdf = (Button) findViewById(R.id.mOpenPdf);
        mOpenPdf.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==mOpenPdf) {
            Intent i = new Intent(this, PdfViewActivity.class);
            i.putExtra("pdfUrl",mPdfUrl.getText().toString());
            startActivity(i);
        }
    }
}
