package com.smartyvpn.tfsmarty.view

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.net.VpnService
import android.os.Bundle
import android.os.RemoteException
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.smartyvpn.tfsmarty.utils.CheckInternetConnection
import com.smartyvpn.tfsmarty.R
import com.smartyvpn.tfsmarty.SharedPreference
import com.smartyvpn.tfsmarty.adapter.PaidServerListRVAdapter
import com.smartyvpn.tfsmarty.databinding.FragmentMainBinding
import com.smartyvpn.tfsmarty.interfaces.ChangeServer
import com.smartyvpn.tfsmarty.model.Server
import com.smartyvpn.tfsmarty.utils.AppSettings
import com.smartyvpn.tfsmarty.utils.ads.BannerManager
import com.smartyvpn.tfsmarty.utils.ads.InterstitialManager
import com.smartyvpn.tfsmarty.utils.ads.InterstitialManager.MyInterstitialAdListener
import com.smartyvpn.tfsmarty.view.screens.InAppPurchaseScreen
import com.smartyvpn.tfsmarty.view.serverlist.FreeServerListActivity
import com.smartyvpn.tfsmarty.view.serverlist.PaidFreeServerListActivity
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView
import com.onesignal.OneSignal
import de.blinkt.openvpn.OpenVpnApi
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.OpenVPNThread
import de.blinkt.openvpn.core.VpnStatus
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class MainFragment : Fragment(), ChangeServer,
    NavigationView.OnNavigationItemSelectedListener  {

    private var server: Server? = null
    private var connection: CheckInternetConnection? = null
    private val vpnThread = OpenVPNThread()
    private val vpnService = OpenVPNService()
    var vpnStart = false
    private var preference: SharedPreference? = null
    private var binding: FragmentMainBinding? = null
    var connection_button_block: ConstraintLayout? = null
    var disconnect_button: ConstraintLayout? = null
    var logTxt: TextView? = null

    private var mAdView: AdView? = null
    private var layout: FrameLayout? = null
    private var admobInterstitial: InterstitialManager? = null


    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        val view = binding!!.getRoot()

        initializeAll()

        OneSignal.promptForPushNotifications();


        navController = findNavController()

        initNavigation()
        initClicks()


        connection_button_block = view.findViewById(R.id.connection_button_block)
        disconnect_button = view.findViewById(R.id.disconnect_button)
        logTxt = view.findViewById(R.id.logTv)
        disconnect_button!!.setOnClickListener(View.OnClickListener {
            if (server!!.country.equals("Change Server")){
                Toast.makeText(context!!,"Select Server First...",Toast.LENGTH_SHORT).show()
            }else{
                if (vpnStart) {
                    confirmDisconnect()
                } else {
                    prepareVpn()
                }
            }

        })
        binding!!.serverSelectionBlock.setOnClickListener {
            val listItems = arrayOf("Free Server", "Paid Server")
            val mBuilder = AlertDialog.Builder(context!!)
            mBuilder.setTitle("Choose Server Type")
            mBuilder.setSingleChoiceItems(listItems, -1) { dialogInterface, i ->

                if (listItems[i].equals("Free Server")){
                    val serverActivity = Intent(context, FreeServerListActivity::class.java)
                    startActivity(serverActivity)
                    activity!!.finish()
                }else  if (listItems[i].equals("Paid Server")){
                    val serverActivity = Intent(context, PaidFreeServerListActivity::class.java)
                    startActivity(serverActivity)
                    activity!!.finish()
                }

//                    txtView.text = listItems[i]
                dialogInterface.dismiss()
            }
            // Set the neutral/cancel button click listener
            mBuilder.setPositiveButton("Cancel") { dialog, which ->
                // Do something when click the neutral button
                dialog.cancel()
            }

            val mDialog = mBuilder.create()
            mDialog.show()
        }
        connection_button_block!!.setOnClickListener(View.OnClickListener {
            if (vpnStart) {
                confirmDisconnect()
            } else {
                prepareVpn()
            }
        })


        // Ads
        MobileAds.initialize(activity!!) { }
        if (!AppSettings.isUserPaid) {
            mAdView = AdView(context!!)
            layout = view.findViewById(R.id.adsBanner)
            val bannerManager = BannerManager()
            bannerManager.loadAdmobBanner(layout!!, mAdView, activity!!)
            loadInterstitialAds()
            binding!!.subscriptionButton.setVisibility(View.VISIBLE)

            binding!!.subscriptionButton.setOnClickListener{
                val subscriptionActivity = Intent(context, InAppPurchaseScreen::class.java)
                startActivity(subscriptionActivity)
            }
        } else {
            layout = view.findViewById(R.id.adsBanner)
            layout!!.setVisibility(View.GONE)
            binding!!.subscriptionButton.setVisibility(View.GONE)
        }



        return view
    }

    /**
     * Initialize all variable and object
     */
    private fun initializeAll() {
        preference = SharedPreference(context)
        server = preference!!.server


        // Update current selected server icon
        updateCurrentServerIcon(server!!.getFlagUrl(), server!!.getCountry())
        connection = CheckInternetConnection()
        LocalBroadcastManager.getInstance(activity!!)
            .registerReceiver(broadcastReceiver, IntentFilter("connectionState"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Checking is vpn already running or not
        isServiceRunning
        VpnStatus.initLogCache(activity!!.cacheDir)
    }

    /**
     * Show show disconnect confirm dialog
     */
    fun confirmDisconnect() {
        val builder = AlertDialog.Builder(
            activity!!
        )
        builder.setMessage(activity!!.getString(R.string.connection_close_confirm))
        builder.setPositiveButton(activity!!.getString(R.string.yes)) { dialog, id -> stopVpn() }
        builder.setNegativeButton(activity!!.getString(R.string.no)) { dialog, id ->
            // User cancelled the dialog
        }

        // Create the AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    /**
     * Prepare for vpn connect with required permission
     */
    private fun prepareVpn() {
        if (!vpnStart) {
            if (internetStatus) {

                // Checking permission for network monitor
                val intent = VpnService.prepare(context)
                if (intent != null) {
                    startActivityForResult(intent, 1)
                } else startVpn() //have already permission

                // Update confection status
                status("connecting")
            } else {

                // No internet connection available
                showToast("you have no internet connection !!")
            }
        } else if (stopVpn()) {

            // VPN is stopped, show a Toast message.
            showToast("Disconnect Successfully")
        }
    }

    /**
     * Stop vpn
     *
     * @return boolean: VPN status
     */
    fun stopVpn(): Boolean {
        try {
            OpenVPNThread.stop()
            status("connect")
            vpnStart = false
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * Taking permission for network access
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            //Permission granted, start the VPN
            startVpn()
        } else {
            showToast("Permission Deny !! ")
        }
    }

    /**
     * Internet connection status.
     */
    val internetStatus: Boolean
        get() = connection!!.isOnline(context!!)

    /**
     * Get service status
     */
    val isServiceRunning: Unit
        get() {
            setStatus(OpenVPNService.getStatus())
        }

    /**
     * Start the VPN
     */
    private fun startVpn() {

        if(server!!.chkPaid.equals("free")){
            try {
                // .ovpn file
                val conf = activity!!.assets.open(server!!.ovpn)
                val isr = InputStreamReader(conf)
                val br = BufferedReader(isr)
                var config = ""
                var line: String?
                while (true) {
                    line = br.readLine()
                    if (line == null) break
                    config += """
                    $line
                    
                    """.trimIndent()
                }
                br.readLine()
                OpenVpnApi.startVpn(
                    context,
                    config,
                    server!!.country,
                    server!!.ovpnUserName,
                    server!!.ovpnUserPassword
                )

                // Update log
                binding!!.logTv.text = "Connecting..."
                vpnStart = true
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }else if(server!!.chkPaid.equals("paid")){
            try {
                // .ovpn file
                val conf = activity!!.assets.open("paid/"+server!!.ovpn)
                val isr = InputStreamReader(conf)
                val br = BufferedReader(isr)
                var config = ""
                var line: String?
                while (true) {
                    line = br.readLine()
                    if (line == null) break
                    config += """
                    $line
                    
                    """.trimIndent()
                }
                br.readLine()
                OpenVpnApi.startVpn(
                    context,
                    config,
                    server!!.country,
                    server!!.ovpnUserName,
                    server!!.ovpnUserPassword
                )

                // Update log
                binding!!.logTv.text = "Connecting..."
                vpnStart = true
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }


    }

    /**
     * Status change with corresponding vpn connection status
     *
     * @param connectionState
     */
    fun setStatus(connectionState: String?) {
        if (connectionState != null) when (connectionState) {
            "DISCONNECTED" -> {
                status("connect")
                vpnStart = false
                OpenVPNService.setDefaultStatus()
            }

            "CONNECTED" -> {
                vpnStart = true // it will use after restart this activity
                if (admobInterstitial != null) {
                    if (admobInterstitial!!.isAdLoaded()) {
                        admobInterstitial!!.showAdmobInterstitial();
                    }else{
                        status("connected")
                        binding!!.logTv.text = "Connected"
                        logTxt!!.setTextColor(resources.getColor(R.color.colorPrimary))
                    }
                }else{
                    status("connected")
                    binding!!.logTv.text = "Connected"
                    logTxt!!.setTextColor(resources.getColor(R.color.colorPrimary))
                }

            }

            "WAIT" -> binding!!.logTv.text = "waiting for server connection!!"
            "AUTH" -> binding!!.logTv.text = "server authenticating!!"
            "RECONNECTING" -> {
                status("connecting")
                binding!!.logTv.text = "Reconnecting..."
            }

            "NONETWORK" -> binding!!.logTv.text = "No network connection"
        }
    }

    /**
     * Change button background color and text
     *
     * @param status: VPN current status
     */
    //If You are Using button then you can use setText() method
    fun status(status: String) {
        if (status == "connect") {

//            powerButton.setImageResource(R.drawable.power_off);
            disconnect_button!!.visibility = View.GONE
            connection_button_block!!.visibility = View.VISIBLE
            binding!!.vpnConnectionTime.visibility = View.GONE
            logTxt!!.text = getString(R.string.not_connect)
            logTxt!!.setTextColor(resources.getColor(R.color.red))
        } else if (status == "connecting") {

//            powerButton.setImageResource(R.drawable.power_connecting);
        } else if (status == "connected") {
            disconnect_button!!.visibility = View.VISIBLE
            connection_button_block!!.visibility = View.GONE
            binding!!.vpnConnectionTime.visibility = View.VISIBLE

//            powerButton.setImageResource(R.drawable.power_on);
        } else if (status == "tryDifferentServer") {

//            powerButton.setImageResource(R.drawable.power_on);

            // binding.vpnBtn.setText("Try Different\nServer");
        } else if (status == "loading") {
//            powerButton.setImageResource(R.drawable.power_off);

            // binding.vpnBtn.setText("Loading Server..");
        } else if (status == "invalidDevice") {
//            powerButton.setImageResource(R.drawable.power_on);

            // binding.vpnBtn.setText("Invalid Device");
        } else if (status == "authenticationCheck") {

//            powerButton.setImageResource(R.drawable.power_connecting);


            // binding.vpnBtn.setText("Authentication \n Checking...");
        }
    }

    /**
     * Receive broadcast message
     */
    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                setStatus(intent.getStringExtra("state"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                var duration = intent.getStringExtra("duration")
                var lastPacketReceive = intent.getStringExtra("lastPacketReceive")
                var byteIn = intent.getStringExtra("byteIn")
                var byteOut = intent.getStringExtra("byteOut")
                if (duration == null) duration = "00:00:00"
                if (lastPacketReceive == null) lastPacketReceive = "0"
                if (byteIn == null) byteIn = " "
                if (byteOut == null) byteOut = " "
                updateConnectionStatus(duration, lastPacketReceive, byteIn, byteOut)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Update status UI
     *
     * @param duration:          running time
     * @param lastPacketReceive: last packet receive time
     * @param byteIn:            incoming data
     * @param byteOut:           outgoing data
     */
    fun updateConnectionStatus(
        duration: String?,
        lastPacketReceive: String?,
        byteIn: String?,
        byteOut: String?
    ) {
        binding!!.vpnConnectionTime.text = duration
        //        binding.lastPacketReceiveTv.setText(lastPacketReceive + " sec.");

        //Speed Download
        binding!!.byteInTv.text = byteIn
        //Speed upload
        binding!!.byteOutTv.text = byteOut
    }

    /**
     * Show toast message
     *
     * @param message: toast message
     */
    fun showToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * VPN server country icon change
     *
     * @param serverIcon : icon URL
     * @param country
     * @param ip_address
     */
    fun updateCurrentServerIcon(serverIcon: String?, country: String) {
        Glide.with(context!!)
            .load("https://flagcdn.com/w320/"+serverIcon+".png")
            .placeholder(R.drawable.ic_server_flag_icon)
            .into(binding!!.connectedCountry)
        binding!!.serverFlagName.text = "" + country
//        binding!!.serverFlagDes.text = "" + ip_address
    }

    /**
     * Change server when user select new server
     *
     * @param server ovpn server details
     */
    override fun newServer(server: Server) {
        this.server = server
        updateCurrentServerIcon(server.flagUrl, server.country)

        // Stop previous connection
        if (vpnStart) {
            stopVpn()
        }
        prepareVpn()
    }

    override fun onResume() {
        if (server == null) {
            server = preference!!.server
        }
        super.onResume()
    }

    /**
     * Save current selected server on local shared preference
     */
    override fun onStop() {
        if (server != null) {
            preference!!.saveServer(server)
        }
        super.onStop()
    }

    private fun initClicks() {
        binding!!.btnDrawer.setOnClickListener {
            binding!!.homeDrawerLayout.openDrawer(Gravity.LEFT)
        }
    }

    private fun initNavigation() {

        // removed AdvanceDrawerLayout-specific methods (using standard DrawerLayout)
        // binding!!.homeDrawerLayout.setViewScale(Gravity.START, 0.96f)
        // binding!!.homeDrawerLayout.setRadius(Gravity.START, 20f)
        // binding!!.homeDrawerLayout.setViewElevation(Gravity.START, 8f)

        navController.let {
            binding!!.navView.setupWithNavController(it)
            binding!!.navView.setNavigationItemSelectedListener(this)
        }



    }


    /*
   * Drawer Navigation Menu Buttons
   * and Click Listener Implementation
   * */
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.homeFragment -> {
                //start main fragment
                navController.navigate(R.id.homeFragment)
                binding!!.homeDrawerLayout.closeDrawers()
                return true
            }
            R.id.serversFragment -> {
                //start servers fragment
                binding!!.homeDrawerLayout.closeDrawers()

                val listItems = arrayOf("Free Server", "Paid Server")
                val mBuilder = AlertDialog.Builder(context!!)
                mBuilder.setTitle("Choose Server Type")
                mBuilder.setSingleChoiceItems(listItems, -1) { dialogInterface, i ->

                    if (listItems[i].equals("Free Server")){
                        val serverActivity = Intent(context, FreeServerListActivity::class.java)
                        startActivity(serverActivity)
                    }else  if (listItems[i].equals("Paid Server")){
                        val serverActivity = Intent(context, PaidServerListRVAdapter::class.java)
                        startActivity(serverActivity)
                    }

//                    txtView.text = listItems[i]
                    dialogInterface.dismiss()
                }
                // Set the neutral/cancel button click listener
                mBuilder.setPositiveButton("Cancel") { dialog, which ->
                    // Do something when click the neutral button
                    dialog.cancel()
                }

                val mDialog = mBuilder.create()
                mDialog.show()


                return true
            }
            R.id.nav_unlock -> {
                //start premium fragment
                binding!!.homeDrawerLayout.closeDrawers()
                val subscriptionActivity = Intent(context, InAppPurchaseScreen::class.java)
                startActivity(subscriptionActivity)
                return true
            }

            R.id.nav_rate -> {
                //show rate us dialog
                navController.navigate(R.id.action_homeFragment_to_rateus_dialog)
                binding!!.homeDrawerLayout.closeDrawers()
                return true
            }
            R.id.nav_share -> {
                //show share dialog
                shareApp()
                binding!!.homeDrawerLayout.closeDrawers()
                return true
            }

            R.id.nav_faq -> {
                //start faqs fragment
                binding!!.homeDrawerLayout.closeDrawers()
                navController.navigate(R.id.action_homeFragment_to_faqFragment)
                return true
            }
            R.id.nav_about -> {
                //start about fragment
                navController.navigate(R.id.action_homeFragment_to_about_dialog)
                binding!!.homeDrawerLayout.closeDrawers()
                return true
            }
            R.id.nav_policy -> {
                //start privacy policy fragment
                loadPrivacyPolicy(getString(R.string.privacy_policy_link))
                binding!!.homeDrawerLayout.closeDrawers()
                return true
            }
        }
        return false
    }

    // privacy policy
    private fun loadPrivacyPolicy(link: String) {
        if (TextUtils.isEmpty(link)) {

            Toast.makeText(context!!, resources.getString(R.string.privacy_policy_error), Toast.LENGTH_SHORT)
                .show()

        } else {

            // missing 'http://' will cause crashed
            try {
                val uri = Uri.parse(link)
                val intent_policy = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent_policy)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    //share the application...
    private fun shareApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_app_subject))
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.share_app_desp) + context!!.applicationContext.packageName
            )
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app_title)))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun loadInterstitialAds() {
        admobInterstitial = InterstitialManager(context!!, activity!!,
            resources.getString(R.string.interstitial_ad),
            object : MyInterstitialAdListener {
                override fun OnAdLoaded() {}
                override fun OnAdFailedToLoad() {}
                override fun OnAdFailedToShowContent() {}
                override fun OnAdDismissed() {
                    status("connected")
                    binding!!.logTv.text = "Connected"
                    logTxt!!.setTextColor(resources.getColor(R.color.colorPrimary))
                }
            })
        admobInterstitial!!.loadAdmobInterstitial(resources.getString(R.string.interstitial_ad))
    }

}