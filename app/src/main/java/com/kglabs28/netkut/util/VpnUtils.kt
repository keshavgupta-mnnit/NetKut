package com.kglabs28.netkut.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.VpnService
import android.os.PowerManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.kglabs28.netkut.vpn.NetCutVpnService

object VpnUtils {
    fun startVpnService(context: Context) {
        val intent = Intent(context, NetCutVpnService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }

    fun prepareVpnIntent(context: Context): Intent? {
        return VpnService.prepare(context)
    }

    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return pm.isIgnoringBatteryOptimizations(context.packageName)
    }

    fun openBatteryOptimizationSettings(context: Context) {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
            data = Uri.parse("package:${context.packageName}")
        }
        context.startActivity(intent)
    }
}
