package com.example.onlineshop.ui.Payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.onlineshop.MainActivity.MainActivity
import com.example.onlineshop.R
import com.example.onlineshop.utils.ViewModelFactory
import com.example.onlineshop.data.entity.orderGet.GetOrders
import com.example.onlineshop.data.remoteDataSource.RemoteDataSourceImpl
import com.example.onlineshop.data.roomData.RoomDataSourceImpl
import com.example.onlineshop.data.roomData.RoomService.Companion.getInstance
import com.example.onlineshop.databinding.ActivityCheckoutBinding
import com.example.onlineshop.repository.RepositoryImpl
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData
import com.stash.myapplication.viewmodel.CheckoutViewModel
import org.json.JSONException
import org.json.JSONObject



class CheckoutActivity : AppCompatActivity() {
    // Arbitrarily-picked constant integer you define to track a request for payment data activity.
    private val loadPaymentDataRequestCode = 991
    var amount: String? = null
    var order: GetOrders.Order? = null
    var paymentViewModel: PaymentViewModel? = null
    var repositoryImpl: RepositoryImpl? = null
    private val model: CheckoutViewModel by viewModels()
    private var mContext: Context? = null

    private lateinit var layoutBinding: ActivityCheckoutBinding
    private lateinit var googlePayButton: View



    /**
     * Initialize the Google Pay API on creation of the activity
     *
     * @see AppCompatActivity.onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        // Use view binding to access the UI elements
        layoutBinding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(layoutBinding.root)
        amount = intent.getStringExtra("amount")
        order = intent.getSerializableExtra("order") as GetOrders.Order?

        googlePayButton = layoutBinding.googlePayButton.root
        googlePayButton.setOnClickListener { requestPayment() }

        // Check whether Google Pay can be used to complete a payment
        model.canUseGooglePay.observe(this, Observer(::setGooglePayAvailable))

        repositoryImpl = RepositoryImpl(
            RemoteDataSourceImpl(), RoomDataSourceImpl(
                getInstance(
                    application
                )
            )
        )

        val viewModelFactory = ViewModelFactory(repositoryImpl!!, application)
        paymentViewModel =
            ViewModelProvider(this, viewModelFactory).get(PaymentViewModel::class.java)
        layoutBinding.detailPrice.text=amount
        layoutBinding.detailTitle.text= "Total Price"
       

    }

    /**
     * If isReadyToPay returned `true`, show the button and hide the "checking" text. Otherwise,
     * notify the user that Google Pay is not available. Please adjust to fit in with your current
     * user flow. You are not required to explicitly let the user know if isReadyToPay returns `false`.
     *
     * @param available isReadyToPay API response.
     */
    private fun setGooglePayAvailable(available: Boolean) {
        if (available) {
            googlePayButton.visibility = View.VISIBLE
        } else {
            Toast.makeText(
                this,
                "Unfortunately, Google Pay is not available on this device",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun requestPayment() {

        // Disables the button to prevent multiple clicks.
        googlePayButton.isClickable = false

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        val dummyPriceCents = (amount!!.toDouble()*100).toLong()

        val task = model.getLoadPaymentDataTask(dummyPriceCents )

        // Shows the payment sheet and forwards the result to the onActivityResult method.
        AutoResolveHelper.resolveTask(task, this, loadPaymentDataRequestCode)
    }

    /**
     * Handle a resolved activity from the Google Pay payment sheet.
     *
     * @param requestCode Request code originally supplied to AutoResolveHelper in requestPayment().
     * @param resultCode Result code returned by the Google Pay API.
     * @param data Intent from the Google Pay API containing payment or error data.
     * @see [Getting a result
     * from an Activity](https://developer.android.com/training/basics/intents/result)
     */
    @Suppress("Deprecation")
    // Suppressing deprecation until `registerForActivityResult` is available on the Google Pay API.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // Value passed in AutoResolveHelper
            loadPaymentDataRequestCode -> {
                when (resultCode) {
                    RESULT_OK ->
                        data?.let { intent ->
                            PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                        }

                    RESULT_CANCELED -> {
                        order!!.id?.let { paymentViewModel!!.cancelOrder(it) }
                        val intent = Intent(
                           mContext,
                            MainActivity::class.java
                        )
                       mContext!!.startActivity(intent)

                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            handleError(it.statusCode)
                        }
                    }
                }

                // Re-enables the Google Pay payment button.
                googlePayButton.isClickable = true
            }
        }
    }

    /**
     * PaymentData response object contains the payment information, as well as any additional
     * requested information, such as billing and shipping address.
     *
     * @param paymentData A response object returned by Google after a payer approves payment.
     * @see [Payment
     * Data](https://developers.google.com/pay/api/android/reference/object.PaymentData)
     */
    private fun handlePaymentSuccess(paymentData: PaymentData) {
        val paymentInformation = paymentData.toJson() ?: return

        try {
            // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
            val paymentMethodData = JSONObject(paymentInformation).getJSONObject("paymentMethodData")
            val billingName = paymentMethodData.getJSONObject("info")
                .getJSONObject("billingAddress").getString("name")


            Toast.makeText(this, getString(R.string.payments_show_name, billingName), Toast.LENGTH_LONG).show()
            order?.let { paymentViewModel!!.createOrderInPayment(it) }

            Log.d(
                 "GooglePaymentToken", paymentMethodData
                     .getJSONObject("tokenizationData")
                     .getString("token")
             )
        } catch (error: JSONException) {
            Log.e("handlePaymentSuccess", "Error: $error")
        }
    }

    /**
     * At this stage, the user has already seen a popup informing them an error occurred. Normally,
     * only logging is required.
     *
     * @param statusCode will hold the value of any constant from CommonStatusCode or one of the
     * WalletConstants.ERROR_CODE_* constants.
     * @see [
     * Wallet Constants Library](https://developers.google.com/android/reference/com/google/android/gms/wallet/WalletConstants.constant-summary)
     */
    private fun handleError(statusCode: Int) {
        Log.w("loadPaymentData failed", "Error code: $statusCode")
    }
}