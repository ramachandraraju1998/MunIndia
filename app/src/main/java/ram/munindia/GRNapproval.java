package ram.munindia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.TextView;
import android.widget.Toast;

import ram.munindia.validations.Validations;

public class GRNapproval extends AppCompatActivity  {

    protected WebView wvWebView;
    ImageView myimage_back;
    String from;
TextView centername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grnapproval);

        myimage_back=findViewById(R.id.myimage_back);
        centername=findViewById(R.id.centername);

        myimage_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        from = getIntent().getStringExtra("from");
        centername.setText(from);
        initView();
        // check Internet
        if (Validations.hasActiveInternetConnection(this))
        {

           // getData();
            //  Log.d("===========================", "Internet Present");
        }
        else
        {
            Toast.makeText(GRNapproval.this,"Please Check Internet Connection", Toast.LENGTH_LONG).show();
            //Log.d("===========================", "No Internet");
            this.registerReceiver(this.mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

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
        if(from.equals("GRNApproval")) {
            wvWebView.loadUrl(MainActivity.url + "index.php?r=grn-store%2Fapprovalindex&device=true");
        }else if(from.equals("RMDashboard")){
            wvWebView.loadUrl(MainActivity.url + "index.php?r=site%2Frm-dashboard&device=true");
        }else if(from.equals("FGDashboard")){
            wvWebView.loadUrl(MainActivity.url + "index.php?r=stock%2Ffg-index&device=true");
        }

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

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if (currentNetworkInfo.isConnected())
            {
                // Log.d("===========================", "Connected");
                finish();
                startActivity(getIntent());
                Toast.makeText(getApplicationContext(), "Connected",Toast.LENGTH_LONG).show();
            }
            else
            {
                //  Log.d("===========================", "Not Connected");
                Toast.makeText(getApplicationContext(), "internet Not Connected",
                        Toast.LENGTH_LONG).show();
            }
        }
    };


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
