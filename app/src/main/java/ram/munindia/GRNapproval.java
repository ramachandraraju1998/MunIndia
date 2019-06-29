package ram.munindia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class GRNapproval extends AppCompatActivity  {

    protected WebView wvWebView;
    ImageView myimage_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grnapproval);

        myimage_back=findViewById(R.id.myimage_back);

        myimage_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initView();
    }

    private void initView() {
        wvWebView = (WebView) findViewById(R.id.wvWebView);
        WebSettings webSettings = wvWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        wvWebView.getSettings().setJavaScriptEnabled(true);
        wvWebView.getSettings().setDomStorageEnabled(true);
        wvWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT >= 21) {
            wvWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().setAcceptThirdPartyCookies(wvWebView, true);
        }
        wvWebView.loadUrl(MainActivity.url+"index.php?r=grn-store%2Fapprovalindex");
        wvWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                //M.showLoadingDialog(AddRecceWeb.this);

            }
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }
            public void onPageFinished(WebView view, String url) {

                view.getUrl();
                super.onPageFinished(view, url);
                //M.hideLoadingDialog();
            }
            @Override
            public void onReceivedHttpError(
                    WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                //Toast.makeText(getApplicationContext(),"httperror",Toast.LENGTH_SHORT).show();
                //  M.hideLoadingDialog();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                           SslError error) {
                // Toast.makeText(getApplicationContext(),"SslError",Toast.LENGTH_SHORT).show();
                //M.hideLoadingDialog();
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                //Your code to do
                Toast.makeText(getApplicationContext(), "Your Internet Connection May not be active Or " + error , Toast.LENGTH_LONG).show();
                //M.hideLoadingDialog();
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        //db.execSQL("delete from install");
       /* db.execSQL("delete from recce");
        db.close();*/
        Intent it=new Intent(GRNapproval.this,Dashboard.class);
        startActivity(it);
        finish();
    }

}
