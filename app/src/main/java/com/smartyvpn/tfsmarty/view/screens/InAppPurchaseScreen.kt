package com.smartyvpn.tfsmarty.view.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smartyvpn.tfsmarty.R
import com.smartyvpn.tfsmarty.databinding.ActivitySubscriptionBinding
import com.smartyvpn.tfsmarty.utils.AppSettings
import com.smartyvpn.tfsmarty.utils.AppSettings.Companion.LICENSE_KEY
import com.smartyvpn.tfsmarty.utils.AppSettings.Companion.one_month_subscription_id
import com.smartyvpn.tfsmarty.utils.AppSettings.Companion.one_year_subscription_id
import com.smartyvpn.tfsmarty.utils.AppSettings.Companion.three_month_subscription_id
import games.moisoni.google_iab.BillingConnector
import games.moisoni.google_iab.BillingEventListener
import games.moisoni.google_iab.enums.ErrorType
import games.moisoni.google_iab.enums.ProductType
import games.moisoni.google_iab.models.BillingResponse
import games.moisoni.google_iab.models.ProductInfo
import games.moisoni.google_iab.models.PurchaseInfo

/*
 * Created by BanoStudio. on 15/10/2022.
 */
class InAppPurchaseScreen : AppCompatActivity() {
    private var binding: ActivitySubscriptionBinding? = null
    private var oneMonthSubscriptionFlag = false
    private var threeMonthSubscriptionFlag = false
    private var oneYearSubscriptionFlag = false


    private var skuListSubscriptionsList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        skuListSubscriptionsList = ArrayList()
        skuListSubscriptionsList!!.add(one_month_subscription_id)
        skuListSubscriptionsList!!.add(three_month_subscription_id)
        skuListSubscriptionsList!!.add(one_year_subscription_id)
        setupBilling()
        initClickListeners()
    }

    /*
     * Change to background of the cards to show unselect the card...
     * */
    private fun unselectCards() {
        binding!!.oneMonthCard.background =
            resources.getDrawable(R.drawable.shape_vpn_package_selection)
        binding!!.threeMonthsCard.background =
            resources.getDrawable(R.drawable.shape_vpn_package_selection)
        binding!!.oneYearCard.background =
            resources.getDrawable(R.drawable.shape_vpn_package_selection)
    }

    /*
     * Change the Corresponding criteria after card1 Selection
     * */
    private fun card1Details() {
        //One Month Card Details
        binding!!.oneMonthText.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneSub1Text.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneMonthPayment.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneMonthPaymentText.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneMonthCheck.setImageResource(R.drawable.ic_check_circle)
    }

    /*
     * Change the Corresponding criteria after card2 Selection
     * */
    private fun card2Details() {
        //        Three Months Card Details
        binding!!.threeMonthsText.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneSub2Text.setTextColor(this.resources.getColor(R.color.black))
        binding!!.threeMonthsPayment.setTextColor(this.resources.getColor(R.color.black))
        binding!!.threeMonthsPaymentText.setTextColor(this.resources.getColor(R.color.black))
        binding!!.threeMonthsCheck.setImageResource(R.drawable.ic_check_circle)
    }

    /*
     * Change the Corresponding criteria after card3 Selection
     * */
    private fun card3Details() {
        //        One Year Card Details
        binding!!.oneYearText.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneSub3Text.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneYearPayment.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneYearPaymentText.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneYearCheck.setImageResource(R.drawable.ic_check_circle)
    }

    /*
     * Change the Corresponding criteria of the cards
     * after the unselection of the cards
     * */
    private fun cardsDetails() {
        //One Month Card Details
        binding!!.oneMonthText.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneSub1Text.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneMonthPayment.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneMonthPaymentText.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneMonthCheck.setImageResource(R.drawable.ic_selection)

        //Three Months Card Details
        binding!!.threeMonthsText.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneSub2Text.setTextColor(this.resources.getColor(R.color.black))
        binding!!.threeMonthsPayment.setTextColor(this.resources.getColor(R.color.black))
        binding!!.threeMonthsPaymentText.setTextColor(this.resources.getColor(R.color.black))
        binding!!.threeMonthsCheck.setImageResource(R.drawable.ic_selection)

        //One Year Card Details
        binding!!.oneYearText.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneSub3Text.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneYearPayment.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneYearPaymentText.setTextColor(this.resources.getColor(R.color.black))
        binding!!.oneYearCheck.setImageResource(R.drawable.ic_selection)
    }

    private fun initClickListeners() {

        /*
         * Clicks Implementation
         * */
        binding!!.oneMonthCard.setOnClickListener { view: View? ->
            oneMonthSubscriptionFlag = true
            threeMonthSubscriptionFlag = false
            oneYearSubscriptionFlag = false
            unselectCards()
            cardsDetails()
            card1Details()
            binding!!.oneMonthCard.background =
                resources.getDrawable(R.drawable.subscription_card_bg_dark)
        }
        binding!!.threeMonthsCard.setOnClickListener { view: View? ->
            oneMonthSubscriptionFlag = false
            threeMonthSubscriptionFlag = true
            oneYearSubscriptionFlag = false
            unselectCards()
            cardsDetails()
            card2Details()
            binding!!.threeMonthsCard.background =
                resources.getDrawable(R.drawable.subscription_card_bg_dark)
        }
        binding!!.oneYearCard.setOnClickListener { view: View? ->
            oneMonthSubscriptionFlag = false
            threeMonthSubscriptionFlag = false
            oneYearSubscriptionFlag = true
            unselectCards()
            cardsDetails()
            card3Details()
            binding!!.oneYearCard.background =
                resources.getDrawable(R.drawable.subscription_card_bg_dark)
        }
        binding!!.noThanksText.setOnClickListener { view: View? -> purchaseItem() }
        binding!!.subscriptionBackBtn.setOnClickListener { v: View? -> finish() }
    }

    private var billingConnector: BillingConnector? = null
    fun setupBilling() {
        billingConnector =
            BillingConnector(this, LICENSE_KEY)
                //.setConsumableIds(consumableIds)
                //.setNonConsumableIds(skuListOneTimeProductsList)
                .setSubscriptionIds(skuListSubscriptionsList)
                .autoAcknowledge()
                .autoConsume()
                .enableLogging()
                .connect()

        billingConnector!!.setBillingEventListener(object : BillingEventListener {
            override fun onProductsFetched(productDetails: List<ProductInfo>) {
                /*Provides a list with fetched products*/

                if (productDetails.isEmpty()){
                    Toast.makeText(this@InAppPurchaseScreen,"No Product Key Added From Google Play Console",Toast.LENGTH_SHORT).show();
                }else{
                    for (item in productDetails) {
                        Log.e("SKU=====>", item.toString())
                        if (item.product.equals(one_month_subscription_id)) {
                            binding!!.oneMonthText.text = item.title
                            binding!!.oneSub1Text.text = "Subscription Payment: "+item.getSubscriptionOfferPrice(0, 0)
                        }
                        if (item.product.equals(three_month_subscription_id)) {
                            binding!!.threeMonthsText.text = item.title
                            binding!!.oneSub2Text.text = "Subscription Payment: "+item.getSubscriptionOfferPrice(0, 0)
                        }
                        if (item.product.equals(one_year_subscription_id)) {
                            binding!!.oneYearText.text = item.title
                            binding!!.oneSub3Text.text = "Subscription Payment: "+item.getSubscriptionOfferPrice(0, 0)
                        }
                    }
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

                AppSettings.isUserPaid = purchases.isNotEmpty()

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
                    ErrorType.CLIENT_NOT_READY ->                         //TODO - client is not ready yet
                        Log.e("Error Code====>", "CLIENT_NOT_READY")
                    ErrorType.CLIENT_DISCONNECTED ->                         //TODO - client has disconnected
                        Log.e("Error Code====>", "CLIENT_DISCONNECTED")
                    ErrorType.PRODUCT_NOT_EXIST ->                         //TODO - product does not exist
                        Log.e("Error Code====>", "PRODUCT_NOT_EXIST")
                    ErrorType.CONSUME_ERROR ->                         //TODO - error during consumption
                        Log.e("Error Code====>", "CONSUME_ERROR")
                    ErrorType.CONSUME_WARNING ->                         /*
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
                    ErrorType.ACKNOWLEDGE_ERROR ->                         //TODO - error during acknowledgment
                        Log.e("Error Code====>", "ACKNOWLEDGE_ERROR")
                    ErrorType.ACKNOWLEDGE_WARNING ->                         /*
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
                    ErrorType.FETCH_PURCHASED_PRODUCTS_ERROR ->                         //TODO - error occurred while querying purchased products
                        Log.e("Error Code====>", "FETCH_PURCHASED_PRODUCTS_ERROR")
                    ErrorType.BILLING_ERROR ->                         //TODO - error occurred during initialization / querying product details
                        Log.e("Error Code====>", "BILLING_ERROR")
                    ErrorType.USER_CANCELED ->                         //TODO - user pressed back or canceled a dialog
                        Log.e("Error Code====>", "USER_CANCELED")
                    ErrorType.SERVICE_UNAVAILABLE ->                         //TODO - network connection is down
                        Log.e("Error Code====>", "SERVICE_UNAVAILABLE")
                    ErrorType.BILLING_UNAVAILABLE ->                         //TODO - billing API version is not supported for the type requested
                        Log.e("Error Code====>", "BILLING_UNAVAILABLE")
                    ErrorType.ITEM_UNAVAILABLE ->                         //TODO - requested product is not available for purchase
                        Log.e("Error Code====>", "ITEM_UNAVAILABLE")
                    ErrorType.DEVELOPER_ERROR ->                         //TODO - invalid arguments provided to the API
                        Log.e("Error Code====>", "DEVELOPER_ERROR")
                    ErrorType.ERROR ->                         //TODO - fatal error during the API action
                        Log.e("Error Code====>", "ERROR")
                    ErrorType.ITEM_ALREADY_OWNED ->                         //TODO - failure to purchase since item is already owned
                        Log.e("Error Code====>", "ITEM_ALREADY_OWNED")
                    ErrorType.ITEM_NOT_OWNED ->                         //TODO - failure to consume since item is not owned
                        Log.e("Error Code====>", "ITEM_NOT_OWNED")
                }
            }
        })
    }

    //Click Event Method Finalize it
    private fun purchaseItem() {
        if (oneMonthSubscriptionFlag) {
            billingConnector!!.subscribe(this, one_month_subscription_id)
        } else if (threeMonthSubscriptionFlag) {
            billingConnector!!.subscribe(this, three_month_subscription_id)
        } else if (oneYearSubscriptionFlag) {
            billingConnector!!.subscribe(this, one_year_subscription_id)
        } else {
            Toast.makeText(this, "Please select a subscription first.", Toast.LENGTH_SHORT)
                .show()
        }
    }

}