package com.kglabs28.netkut.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.kglabs28.netkut.VpnActionActivity
import com.kglabs28.netkut.ui.theme.Strings

object ShortcutUtils {
    const val ACTION_START_VPN = "com.kglabs28.netkut.ACTION_START_VPN"
    const val ACTION_STOP_VPN = "com.kglabs28.netkut.ACTION_STOP_VPN"

    fun setupAppShortcuts(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) return

        val startIntent = Intent(context, VpnActionActivity::class.java).apply {
            action = ACTION_START_VPN
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val stopIntent = Intent(context, VpnActionActivity::class.java).apply {
            action = ACTION_STOP_VPN
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val startShortcut = ShortcutInfoCompat.Builder(context, "shortcut_start_vpn")
            .setShortLabel(Strings.StartVpn)
            .setLongLabel(Strings.StartVpnLong)
            .setIcon(createShortcutIcon(context, isStart = true))
            .setIntent(startIntent)
            .build()

        val stopShortcut = ShortcutInfoCompat.Builder(context, "shortcut_stop_vpn")
            .setShortLabel(Strings.StopVpn)
            .setLongLabel(Strings.StopVpnLong)
            .setIcon(createShortcutIcon(context, isStart = false))
            .setIntent(stopIntent)
            .build()

        ShortcutManagerCompat.removeAllDynamicShortcuts(context)
        ShortcutManagerCompat.setDynamicShortcuts(context, listOf(startShortcut, stopShortcut))
    }

    private fun createShortcutIcon(context: Context, isStart: Boolean): IconCompat {
        val density = context.resources.displayMetrics.density
        val size = (48 * density).toInt()
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = if (isStart) Color.parseColor("#2E9E66") else Color.parseColor("#D97706")
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, bgPaint)

        val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }

        if (isStart) {
            val path = Path().apply {
                moveTo(size * 0.38f, size * 0.30f)
                lineTo(size * 0.72f, size * 0.50f)
                lineTo(size * 0.38f, size * 0.70f)
                close()
            }
            canvas.drawPath(path, iconPaint)
        } else {
            val rect = RectF(size * 0.32f, size * 0.32f, size * 0.68f, size * 0.68f)
            canvas.drawRoundRect(rect, 4 * density, 4 * density, iconPaint)
        }

        return IconCompat.createWithBitmap(bitmap)
    }
}
