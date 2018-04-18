package com.pentateuch.watersupply.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pentateuch.watersupply.App;
import com.pentateuch.watersupply.model.Product;
import com.pentateuch.watersupply.model.User;
import com.pentateuch.watersupply.utils.Helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class PaymentGateway extends AppCompatActivity implements OnCompleteListener<Void> {
    private static final String merchantKey = "pgjjGS8P"; // test
    private static final String salt = "eP6c86GKYm"; // test
    private static final String base_url = "https://secure.payu.in";
    private static final String SUCCESS_URL = "https://www.payumoney.com/mobileapp/payumoney/success.php"; // failed
    private static final String FAILED_URL = "https://www.payumoney.com/mobileapp/payumoney/failure.php";
    ProgressDialog progressDialog;
    private String action = "";
    private String hashString = "";
    private Map<String, String> params;
    private Handler mHandler;
    private String getFirstName;
    private String getNumber;
    private String getEmailAddress;
    private String totalAmount;
    private String tag = "PaymentGateway";
    private String hash;
    private Product product;
    private String transactionID;

    public PaymentGateway() {
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        WebView webView = new WebView(this);
        setContentView(webView);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            getFirstName = extras.getString("FIRST_NAME");
            getNumber = extras.getString("PHONE_NUMBER");
            getEmailAddress = extras.getString("EMAIL_ADDRESS");
            totalAmount = extras.getString("TEXT_PRICE");
            product = extras.getParcelable("product");
        }

        params = new HashMap<>();
        params.put("key", merchantKey);
        params.put("amount", totalAmount);
        params.put("firstname", getFirstName);
        params.put("email", getEmailAddress);
        params.put("phone", getNumber);
        params.put("productinfo", "Recharge Wallet");
        params.put("surl", SUCCESS_URL);
        params.put("furl", FAILED_URL);
        params.put("service_provider", "payu_paisa");
        params.put("lastname", "");
        params.put("address1", "");
        params.put("address2", "");
        params.put("city", "");
        params.put("state", "");
        params.put("country", "");
        params.put("zipcode", "");
        params.put("udf1", "");
        params.put("udf2", "");
        params.put("udf3", "");
        params.put("udf4", "");
        params.put("udf5", "");
        params.put("pg", "");

        transactionID = new Helper().getTransactionID();
        String txnId;
        if (empty(params.get("txnId"))) {
            Random rand = new Random();
            String randString = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
            txnId = hashCal("SHA-256", randString).substring(0, 20);
            params.put("txnId", txnId);
        } else
            txnId = params.get("txnId");
        hash = "";
        String hashSequence = "key|txnId|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";
        if (empty(params.get("hash")) && params.size() > 0) {
            if (empty(params.get("key"))
                    || empty(params.get("txnId"))
                    || empty(params.get("amount"))
                    || empty(params.get("firstname"))
                    || empty(params.get("email"))
                    || empty(params.get("phone"))
                    || empty(params.get("productinfo"))
                    || empty(params.get("surl"))
                    || empty(params.get("furl"))
                    || empty(params.get("service_provider"))

                    ) {
                return;
            } else {
                String[] hashVarSeq = hashSequence.split("\\|");

                for (String part : hashVarSeq) {
                    hashString = (empty(params.get(part))) ? hashString.concat("") : hashString.concat(params.get(part));
                    hashString = hashString.concat("|");
                }
                hashString = hashString.concat(salt);


                hash = hashCal("SHA-512", hashString);
                action = base_url.concat("/_payment");
            }
        } else if (!empty(params.get("hash"))) {
            hash = params.get("hash");
            action = base_url.concat("/_payment");
        }

        webView.setWebViewClient(new MyWebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.startsWith("https://checkout.citruspay.com/payu/"))
                    progressDialog.dismiss();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //make sure dialog is showing
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                progressDialog.dismiss();
            }
        });
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);

        webView.addJavascriptInterface(new PayUJavaScriptInterface(this), "PayUMoney");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("key", merchantKey);
        mapParams.put("hash", PaymentGateway.this.hash);
        mapParams.put("txnId", (empty(PaymentGateway.this.params.get("txnId"))) ? "" : PaymentGateway.this.params.get("txnId"));
        Log.d(tag, "txnId: " + PaymentGateway.this.params.get("txnId"));
        mapParams.put("service_provider", "payu_paisa");

        mapParams.put("amount", (empty(PaymentGateway.this.params.get("amount"))) ? "" : PaymentGateway.this.params.get("amount"));
        mapParams.put("firstname", (empty(PaymentGateway.this.params.get("firstname"))) ? "" : PaymentGateway.this.params.get("firstname"));
        mapParams.put("email", (empty(PaymentGateway.this.params.get("email"))) ? "" : PaymentGateway.this.params.get("email"));
        mapParams.put("phone", (empty(PaymentGateway.this.params.get("phone"))) ? "" : PaymentGateway.this.params.get("phone"));

        mapParams.put("productinfo", (empty(PaymentGateway.this.params.get("productinfo"))) ? "" : PaymentGateway.this.params.get("productinfo"));
        mapParams.put("surl", (empty(PaymentGateway.this.params.get("surl"))) ? "" : PaymentGateway.this.params.get("surl"));
        mapParams.put("furl", (empty(PaymentGateway.this.params.get("furl"))) ? "" : PaymentGateway.this.params.get("furl"));
        mapParams.put("lastname", (empty(PaymentGateway.this.params.get("lastname"))) ? "" : PaymentGateway.this.params.get("lastname"));

        mapParams.put("address1", (empty(PaymentGateway.this.params.get("address1"))) ? "" : PaymentGateway.this.params.get("address1"));
        mapParams.put("address2", (empty(PaymentGateway.this.params.get("address2"))) ? "" : PaymentGateway.this.params.get("address2"));
        mapParams.put("city", (empty(PaymentGateway.this.params.get("city"))) ? "" : PaymentGateway.this.params.get("city"));
        mapParams.put("state", (empty(PaymentGateway.this.params.get("state"))) ? "" : PaymentGateway.this.params.get("state"));

        mapParams.put("country", (empty(PaymentGateway.this.params.get("country"))) ? "" : PaymentGateway.this.params.get("country"));
        mapParams.put("zipcode", (empty(PaymentGateway.this.params.get("zipcode"))) ? "" : PaymentGateway.this.params.get("zipcode"));
        mapParams.put("udf1", (empty(PaymentGateway.this.params.get("udf1"))) ? "" : PaymentGateway.this.params.get("udf1"));
        mapParams.put("udf2", (empty(PaymentGateway.this.params.get("udf2"))) ? "" : PaymentGateway.this.params.get("udf2"));

        mapParams.put("udf3", (empty(PaymentGateway.this.params.get("udf3"))) ? "" : PaymentGateway.this.params.get("udf3"));
        mapParams.put("udf4", (empty(PaymentGateway.this.params.get("udf4"))) ? "" : PaymentGateway.this.params.get("udf4"));
        mapParams.put("udf5", (empty(PaymentGateway.this.params.get("udf5"))) ? "" : PaymentGateway.this.params.get("udf5"));
        mapParams.put("pg", (empty(PaymentGateway.this.params.get("pg"))) ? "" : PaymentGateway.this.params.get("pg"));
        webViewClientPost(webView, action, mapParams.entrySet());

    }

    public void webViewClientPost(WebView webView, String url, Collection<Map.Entry<String, String>> postData) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>", url, "post"));
        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format("<input name='%s' type='hidden' value='%s' />", item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");
        Log.d(tag, "webViewClientPost called");

        //setup and load the progress bar
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading. Please wait...");
        mHandler = new Handler();
        webView.loadData(sb.toString(), "text/html", "utf-8");
    }

    public boolean empty(String s) {
        return s == null || s.trim().equals("");
    }

    public String hashCal(String type, String str) {
        byte[] hasSeq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hasSeq);
            byte messageDigest[] = algorithm.digest();


            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) hexString.append("0");
                hexString.append(hex);
            }

        } catch (NoSuchAlgorithmException ignored) {
        }

        return hexString.toString();


    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        Toast.makeText(getApplicationContext(), "Successfully payment", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(PaymentGateway.this, OrderSummaryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("transactionID", transactionID);
        intent.putExtra("amount", String.format(Locale.ENGLISH, "%s Rs", totalAmount));
        startActivity(intent);


    }

    private final class PayUJavaScriptInterface {
        private Context context;

        PayUJavaScriptInterface(Context context) {
            this.context = context;
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void success(long id, final String paymentId) {
            mHandler.post(new Runnable() {
                public void run() {
                    mHandler = null;
                    User user = App.getInstance().getUser();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    reference.child("MyOrder").child(user.getUid()).child(transactionID).setValue(product).addOnCompleteListener(PaymentGateway.this);

                }
            });
        }

        @JavascriptInterface
        public void failure(final String id, String error) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //cancelPayment();
                    Toast.makeText(context, "Cancel payment", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PaymentGateway.this, OrderSummaryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("transactionID", transactionID);
                    intent.putExtra("amount", String.format(Locale.ENGLISH, "%s Rs", totalAmount));
                    intent.putExtra("status", "Canceled");
                    startActivity(intent);

                }
            });
        }

        @JavascriptInterface
        public void failure() {
            failure("");
        }

        @JavascriptInterface
        void failure(final String params) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Failed payment", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http")) {
                view.loadUrl(url);
                return true;
            } else {
                return false;
            }
        }
    }

}
