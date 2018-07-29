package com.theffh.test4114;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

public class IAPTestActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    private BillingClient mBillingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iaptest);
    }

    public void buttonClick(View v) {
        switch (v.getId()) {
            case R.id.idBtnSetupBilling:
                setUpBilling();
                break;
            case R.id.idBtnBuyItem:
                buyItem();
                break;
        }
    }

    private void setUpBilling() {
        mBillingClient = BillingClient.newBuilder(this).setListener(this).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The billing client is ready. You can query purchases here.
                    queryProductDetail();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    private void queryProductDetail() {
        List skuList = new ArrayList<>();
        skuList.add("android.test.purchased");
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        mBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                        if (responseCode == BillingClient.BillingResponse.OK && skuDetailsList != null && !skuDetailsList.isEmpty()) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                // Here show user to some description
                                System.out.println("Info : id    : " + skuDetails.getSku());
                                System.out.println("Info : title : " + skuDetails.getTitle());
                                System.out.println("Info : descr : " + skuDetails.getDescription());
                                System.out.println("Info : price : " + skuDetails.getPrice());
                            }
                        }

                    }
                });
    }

    public void buyItem() {
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSku("android.test.purchased")
                .setType(BillingClient.SkuType.INAPP)
                .build();
        int respCode = mBillingClient.launchBillingFlow(this, flowParams);
        System.out.println("Info : Purchase resp code : " + respCode);
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

    }

}
