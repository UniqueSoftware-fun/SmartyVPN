package com.smartyvpn.tfsmarty.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.material.snackbar.Snackbar
import com.pixplicity.easyprefs.library.Prefs
import com.smartyvpn.tfsmarty.R
import com.smartyvpn.tfsmarty.utils.ads.AppOpenManager
import com.smartyvpn.tfsmarty.utils.AppSettings
import com.smartyvpn.tfsmarty.utils.AppSettings.Companion.LICENSE_KEY
import com.smartyvpn.tfsmarty.utils.AppSettings.Companion.isUserPaid
import com.smartyvpn.tfsmarty.utils.AppSettings.Companion.one_month_subscription_id
import com.smartyvpn.tfsmarty.utils.AppSettings.Companion.one_year_subscription_id
import com.smartyvpn.tfsmarty.utils.AppSettings.Companion.three_month_subscription_id
import com.smartyvpn.tfsmarty.utils.CheckInternetConnection
import com.smartyvpn.tfsmarty.databinding.ActivitySplashBinding
import com.smartyvpn.tfsmarty.view.screens.ControllerActivity
import com.smartyvpn.tfsmarty.view.screens.IntroductionScreen
import games.moisoni.google_iab.BillingConnector
import games.moisoni.google_iab.BillingEventListener
import games.moisoni.google_iab.enums.ErrorType
import games.moisoni.google_iab.enums.ProductType
import games.moisoni.google_iab.models.BillingResponse
import games.moisoni.google_iab.models.ProductInfo
import games.moisoni.google_iab.models.PurchaseInfo


/*
 * Created by Androi Jutt Dev. on 1/10/22.
 */


class SplashScreen : AppCompatActivity() {

    private var binding: ActivitySplashBinding?= null // binding activity
    private var skuListSubscriptionsList: ArrayList<String>? = null //purchase item arraylists
    private var checkingAccount = false // check method variable app run first time or not
    private var billingConnector: BillingConnector? = null // billing connection variable

    private var connection: CheckInternetConnection? = null // internet connection variable


    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        MobileAds.initialize(this) { initializationStatus: InitializationStatus? -> }
        //Initialize Internet Met
        connection = CheckInternetConnection()
        //Check Internet Connection
        if (connection!!.isOnline(this)) {
            //init billing class
            skuListSubscriptionsList = ArrayList()
            skuListSubscriptionsList!!.add(one_month_subscription_id)
            skuListSubscriptionsList!!.add(three_month_subscription_id)
            skuListSubscriptionsList!!.add(one_year_subscription_id)

            setupBilling()
        } else {
            //Intenet Connection Error
            val snackBar = Snackbar.make(
                binding!!.mainRoot,
                "\nNo Internet Connection Available.........\n\n",
                Snackbar.LENGTH_INDEFINITE
            )
            snackBar.setActionTextColor(Color.WHITE)
            snackBar.setBackgroundTint(resources.getColor(R.color.colorPrimary))
            snackBar.setAction("Exit") {
                snackBar.dismiss().apply {
                    finish()
                }
            }
            snackBar.show()
        }




    }

    private fun moveToNextActivity() {

        val appOpenManager =
            AppOpenManager(this@SplashScreen)
        appOpenManager.fetchAd(resources.getString(R.string.admob_openads_id))

        countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (AppOpenManager.adsisLoaded() === true) {
                    appOpenManager.showAdIfAvailable()
                    countDownTimer!!.cancel()
                    Log.d("mmmm", "ads is show")
                }
            }

            override fun onFinish() {
                if (AppOpenManager.adsisLoaded() !== true) {
                    startMainActivity()
                }
            }
        }.start()

    }

    /**
     * Start Method to Move Next Activitysss
     * */
    public fun startMainActivity() {

        checkingAccount = true

        Handler(Looper.myLooper()!!).postDelayed({
          if (Prefs.contains(AppSettings.FIRST_TIME_KEY)) {
            val intent = Intent(this, ControllerActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, IntroductionScreen::class.java)
            startActivity(intent)
            finish()
        }
        }, 3000)

    }

    public fun stopCountdown() {
        countDownTimer!!.cancel()
        Log.d("mmmm", "stop countdown")
    }


    /*
    * Billing Setup Method
    * */
    fun setupBilling() {
        billingConnector =
            BillingConnector(this, LICENSE_KEY)
//              .setConsumableIds(consumableIds)
//              .setNonConsumableIds(skuListOneTimeProductsList)
                .setSubscriptionIds(skuListSubscriptionsList)
                .autoAcknowledge()
                .autoConsume()
                .enableLogging()
                .connect()

        billingConnector!!.setBillingEventListener(object : BillingEventListener {
            override fun onProductsFetched(productDetails: List<ProductInfo>) {
                /*Provides a list with fetched products*/
                for (item in productDetails) {
                    Log.e(
                        "SKU=====>", """$item""".trimIndent()
                    )
                }
            }

            override fun onPurchasedProductsFetched(
                productType: ProductType,
                purchases: List<PurchaseInfo>
            ) {
                /*Provides a list with fetched purchased products*/

                /*
                         * This will be called even when no purchased products are returned by the API
                         * */

                when (productType) {
                    ProductType.INAPP -> {}
                    ProductType.SUBS ->                         //TODO - subscription products
                        if (!purchases.isEmpty()) {
                            for (purchaseInfo in purchases) {
                                if (purchaseInfo.product == one_month_subscription_id) {
                                    isUserPaid = true
                                    if (!checkingAccount) {
                                        startMainActivity()
                                    }

//                                    sharedPref.setIsRemoveAd(true);
                                } else if (purchaseInfo.product == three_month_subscription_id) {
                                    isUserPaid = true
                                    if (!checkingAccount) {
                                        startMainActivity()
                                    }
                                } else if (purchaseInfo.product == one_year_subscription_id) {
                                    isUserPaid = true
                                    if (!checkingAccount) {
                                        startMainActivity()
                                    }
                                }
                            }
                        } else {
                            isUserPaid = false
                            if (!checkingAccount) {
                                moveToNextActivity()
                            }
                        }
                    ProductType.COMBINED -> {}
                }

            }

            override fun onProductsPurchased(purchases: List<PurchaseInfo>) {
                /*Callback after a product is purchased*/
            }

            override fun onPurchaseAcknowledged(purchase: PurchaseInfo) {
                /*Callback after a purchase is acknowledged*/

                /*
                         * Grant user entitlement for NON-CONSUMABLE products and SUBSCRIPTIONS here
                         *
                         * Even though onProductsPurchased is triggered when a purchase is successfully made
                         * there might be a problem along the way with the payment and the purchase won't be acknowledged
                         *
                         * Google will refund users purchases that aren't acknowledged in 3 days
                         *
                         * To ensure that all valid purchases are acknowledged the library will automatically
                         * check and acknowledge all unacknowledged products at the startup
                         * */
            }

            override fun onPurchaseConsumed(purchase: PurchaseInfo) {
                /*Callback after a purchase is consumed*/

                /*
                         * Grant user entitlement for CONSUMABLE products here
                         *
                         * Even though onProductsPurchased is triggered when a purchase is successfully made
                         * there might be a problem along the way with the payment and the user will be able consume the product
                         * without actually paying
                         * */
            }

            override fun onBillingError(
                billingConnector: BillingConnector,
                response: BillingResponse
            ) {
                /*Callback after an error occurs*/
                when (response.errorType) {
                    ErrorType.CLIENT_NOT_READY ->                                 //TODO - client is not ready yet
                        Log.e("Error Code====>", "CLIENT_NOT_READY")
                    ErrorType.CLIENT_DISCONNECTED ->                                 //TODO - client has disconnected
                        Log.e("Error Code====>", "CLIENT_DISCONNECTED")
                    ErrorType.PRODUCT_NOT_EXIST ->                                 //TODO - product does not exist
                        Log.e("Error Code====>", "PRODUCT_NOT_EXIST")
                    ErrorType.CONSUME_ERROR ->                                 //TODO - error during consumption
                        Log.e("Error Code====>", "CONSUME_ERROR")
                    ErrorType.CONSUME_WARNING ->                                 /*
                                 * This will be triggered when a consumable purchase has a PENDING state
                                 * User entitlement must be granted when the state is PURCHASED
                                 *
                                 * PENDING transactions usually occur when users choose cash as their form of payment
                                 *
                                 * Here users can be informed that it may take a while until the purchase complete
                                 * and to come back later to receive their purchase
                                 * */
                        //TODO - warning during consumption
                        Log.e("Error Code====>", "CONSUME_WARNING")
                    ErrorType.ACKNOWLEDGE_ERROR ->                                 //TODO - error during acknowledgment
                        Log.e("Error Code====>", "ACKNOWLEDGE_ERROR")
                    ErrorType.ACKNOWLEDGE_WARNING ->                                 /*
                                 * This will be triggered when a purchase can not be acknowledged because the state is PENDING
                                 * A purchase can be acknowledged only when the state is PURCHASED
                                 *
                                 * PENDING transactions usually occur when users choose cash as their form of payment
                                 *
                                 * Here users can be informed that it may take a while until the purchase complete
                                 * and to come back later to receive their purchase
                                 * */
                        //TODO - warning during acknowledgment
                        Log.e("Error Code====>", "ACKNOWLEDGE_WARNING")
                    ErrorType.FETCH_PURCHASED_PRODUCTS_ERROR ->                                 //TODO - error occurred while querying purchased products
                        Log.e("Error Code====>", "FETCH_PURCHASED_PRODUCTS_ERROR")
                    ErrorType.BILLING_ERROR ->                                 //TODO - error occurred during initialization / querying product details
                        Log.e("Error Code====>", "BILLING_ERROR")
                    ErrorType.USER_CANCELED ->                                 //TODO - user pressed back or canceled a dialog
                        Log.e("Error Code====>", "USER_CANCELED")
                    ErrorType.SERVICE_UNAVAILABLE ->                                 //TODO - network connection is down
                        Log.e("Error Code====>", "SERVICE_UNAVAILABLE")
                    ErrorType.BILLING_UNAVAILABLE ->                                 //TODO - billing API version is not supported for the type requested
                        Log.e("Error Code====>", "BILLING_UNAVAILABLE")
                    ErrorType.ITEM_UNAVAILABLE ->                                 //TODO - requested product is not available for purchase
                        Log.e("Error Code====>", "ITEM_UNAVAILABLE")
                    ErrorType.DEVELOPER_ERROR ->                                 //TODO - invalid arguments provided to the API
                        Log.e("Error Code====>", "DEVELOPER_ERROR")
                    ErrorType.ERROR ->                                 //TODO - fatal error during the API action
                        Log.e("Error Code====>", "ERROR")
                    ErrorType.ITEM_ALREADY_OWNED ->                                 //TODO - failure to purchase since item is already owned
                        Log.e("Error Code====>", "ITEM_ALREADY_OWNED")
                    ErrorType.ITEM_NOT_OWNED ->                                 //TODO - failure to consume since item is not owned
                        Log.e("Error Code====>", "ITEM_NOT_OWNED")
                }
                if (!checkingAccount) {
                    moveToNextActivity()
                }
            }
        })
    }

    /*
    * Destroy billing method
    * */
    override fun onDestroy() {
        binding = null
        super.onDestroy()
        if (billingConnector != null) {
            billingConnector!!.release()
        }
    }



}