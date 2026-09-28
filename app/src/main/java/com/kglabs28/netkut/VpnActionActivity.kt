package com.kglabs28.netkut

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.Toast
import com.kglabs28.netkut.ui.theme.Strings
import com.kglabs28.netkut.util.AppUtils
import com.kglabs28.netkut.util.ShortcutUtils
import com.kglabs28.netkut.util.VpnUtils

class VpnActionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (intent?.action) {
            ShortcutUtils.ACTION_START_VPN -> {
                AppUtils.setVpnActive(this, true)
                val vpnIntent = VpnService.prepare(this)
                if (vpnIntent != null) {
                    vpnIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(vpnIntent)
                } else {
                    VpnUtils.startVpnService(this)
                    Toast.makeText(this, Strings.VpnStartedToast, Toast.LENGTH_SHORT).show()
                }
            }
            ShortcutUtils.ACTION_STOP_VPN, "com.kglabs28.netkut.ACTION_PAUSE_VPN" -> {
                AppUtils.setVpnActive(this, false)
                Toast.makeText(this, Strings.VpnStoppedToast, Toast.LENGTH_SHORT).show()
            }
        }

        finish()
    }
}
