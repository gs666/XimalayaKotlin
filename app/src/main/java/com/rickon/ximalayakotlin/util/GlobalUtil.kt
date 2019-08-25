/*
 * Copyright (C) guolin, Suzhou Quxiang Inc. Open source codes for study only.
 * Do not use for commercial purpose.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rickon.ximalayakotlin.util

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.text.TextUtils
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*


/**
 * 应用程序全局的通用工具类，功能比较单一，经常被复用的功能，应该封装到此工具类当中，从而给全局代码提供方面的操作。
 *
 * @author guolin
 * @since 17/2/18
 */
object GlobalUtil {

    private var TAG = "GlobalUtil"

    /**
     * 获取当前应用程序的包名。
     *
     * @return 当前应用程序的包名。
     */
    val appPackage: String
        get() = XimalayaKotlin.context.packageName

    /**
     * 获取当前时间的字符串，格式为yyyyMMddHHmmss。
     * @return 当前时间的字符串。
     */
    val currentDateString: String
        get() {
            val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.US)
            return sdf.format(Date())
        }

    /**
     * 将当前线程睡眠指定毫秒数。
     *
     * @param millis
     * 睡眠的时长，单位毫秒。
     */
    fun sleep(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    fun formatNum(num: String, b: Boolean): StringBuffer {
        val sb = StringBuffer()
        val b0 = BigDecimal("100")
        val b1 = BigDecimal("10000")
        val b2 = BigDecimal("100000000")
        val b3 = BigDecimal(num)

        var formatNumStr = ""
        var unit = ""

        // 以百为单位处理
        if (b) {
            return if (b3.compareTo(b0) == 0 || b3.compareTo(b0) == 1) {
                sb.append("99+")
            } else sb.append(num)
        }

        // 以万为单位处理
        if (b3.compareTo(b1) == -1) {
            formatNumStr = b3.toString()
        } else if (b3.compareTo(b1) == 0 && b3.compareTo(b1) == 1 || b3.compareTo(b2) == -1) {
            unit = "万"

            formatNumStr = b3.divide(b1).toString()
        } else if (b3.compareTo(b2) == 0 || b3.compareTo(b2) == 1) {
            unit = "亿"
            formatNumStr = b3.divide(b2).toString()

        }
        if ("" != formatNumStr) {
            var i = formatNumStr.indexOf(".")
            if (i == -1) {
                sb.append(formatNumStr).append(unit)
            } else {
                i++
                val v = formatNumStr.substring(i, i + 1)
                if (v != "0") {
                    sb.append(formatNumStr.substring(0, i + 1)).append(unit)
                } else {
                    sb.append(formatNumStr.substring(0, i - 1)).append(unit)
                }
            }
        }
        return if (sb.isEmpty()) sb.append("0") else sb
    }

    /**
     * 获取资源文件中定义的字符串。
     *
     * @param resId
     * 字符串资源id
     * @return 字符串资源id对应的字符串内容。
     */
    fun getString(resId: Int): String {
        return XimalayaKotlin.context.resources.getString(resId)
    }

    /**
     * 获取指定资源名的资源id。
     *
     * @param name
     * 资源名
     * @param type
     * 资源类型
     * @return 指定资源名的资源id。
     */
    fun getResourceId(name: String, type: String): Int {
        return XimalayaKotlin.context.resources.getIdentifier(name, type, appPackage)
    }

    /**
     * 获取AndroidManifest.xml文件中，<application>标签下的meta-data值。
     *
     * @param key
     *  <application>标签下的meta-data健
     */
    fun getApplicationMetaData(key: String): String? {
        var applicationInfo: ApplicationInfo? = null
        try {
            applicationInfo = XimalayaKotlin.context.packageManager.getApplicationInfo(appPackage, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
//            logWarn(TAG, e.message, e)
        }
        if (applicationInfo == null) return ""
        return applicationInfo.metaData.getString(key)
    }

    /**
     * 获取传入通用资源标识符的后缀名。
     *
     * @param uri
     * 通用资源标识符
     * @return 通用资源标识符的后缀名。
     */
    fun getUriSuffix(uri: String): String {
        if (!TextUtils.isEmpty(uri)) {
            val doubleSlashIndex = uri.indexOf("//")
            val slashIndex = uri.lastIndexOf("/")
            if (doubleSlashIndex != -1 && slashIndex != -1) {
                if (doubleSlashIndex + 1 == slashIndex) {
                    return ""
                }
            }
            val dotIndex = uri.lastIndexOf(".")
            if (dotIndex != -1 && dotIndex > slashIndex) {
                return uri.substring(dotIndex + 1)
            }
        }
        return ""
    }

    /**
     * 生成资源对应的key。
     *
     * @param uri
     * 通用资源标识符
     * @param dir
     * 生成带有指定目录结构的key
     * @return 资源对应的key。
     */
    fun generateKey(uri: String, dir: String): String {
        val suffix = getUriSuffix(uri)
        val uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase()
        return if (TextUtils.isEmpty(dir)) {
            var key = "$currentDateString-$uuid"
            if (!TextUtils.isEmpty(suffix)) {
                key = "$key.$suffix"
            }
            key
        } else {
            var key = "$dir/$currentDateString-$uuid"
            if (!TextUtils.isEmpty(suffix)) {
                key = "$key.$suffix"
            }
            key.replace("//", "/")
        }
    }

    /**
     * 获取转换之后的数字显示，如123456会被转换成12.3万。
     * @param number
     * 原始数字
     * @return 转换之后的数字。
     */
    fun getConvertedNumber(number: Int) = when {
        number < 10000 -> number.toString()
        number < 1000000 -> {
            var converted = String.format(Locale.ENGLISH, "%.1f", number / 10000.0)
            if (converted.endsWith(".0")) {
                converted = converted.replace(".0", "")
            }
            converted + "万"
        }
        number < 100100100 -> {
            val converted = number / 10000
            converted.toString() + "万"
        }
        else -> {
            var converted = String.format(Locale.ENGLISH, "%.1f", number / 100_000_000.0)
            if (converted.endsWith(".00")) {
                converted = converted.replace(".00", "")
            }
            converted + "亿"
        }
    }

    /**
     * 判断某个应用是否安装。
     * @param packageName
     * 要检查是否安装的应用包名
     * @return 安装返回true，否则返回false。
     */
    fun isInstalled(packageName: String): Boolean {
        val packageInfo: PackageInfo? = try {
            XimalayaKotlin.context.packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
        return packageInfo != null
    }

    /**
     * 获取当前应用程序的图标。
     */
    fun getAppIcon(): Drawable {
        val packageManager = XimalayaKotlin.context.packageManager
        val applicationInfo = packageManager.getApplicationInfo(appPackage, 0)
        return packageManager.getApplicationIcon(applicationInfo)
    }

    /**
     * 判断手机是否安装了QQ。
     */
    fun isQQInstalled() = isInstalled("com.tencent.mobileqq")

    /**
     * 判断手机是否安装了微信。
     */
    fun isWechatInstalled() = isInstalled("com.tencent.mm")

    /**
     * 判断手机是否安装了微博。
     * */
    fun isWeiboInstalled() = isInstalled("com.sina.weibo")

}