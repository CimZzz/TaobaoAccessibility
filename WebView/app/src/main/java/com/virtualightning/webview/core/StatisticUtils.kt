package com.virtualightning.webview.core

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/09/09 11:03:53
 *  Project : WebView
 *  Since Version : Alpha
 */
object StatisticUtils {
    private var totalCount: Long = 0L
    private var phoneCount: Long = 0L
    private var phoneErrorCount: Long = 0L
    private var successCount: Long = 0L
    private var errorCount: Long = 0L

    fun newPhone() {
        phoneCount ++
    }

    fun success() {
        totalCount ++
        phoneCount ++
        successCount ++
        log()
    }

    fun phoneError() {
        phoneErrorCount ++
        log()
    }

    fun error() {
        totalCount ++
        errorCount ++
        log()
    }

    private fun formatPercent(num :Float): String {
        val numStr = (num * 100).toString()
        val pointIdx = numStr.indexOf(".")
        return if(pointIdx < numStr.length - 3)
            numStr.substring(0, pointIdx + 3)
        else numStr
    }

    private fun log() {
        val successRate = successCount.toFloat() / totalCount
        val errorRate = errorCount.toFloat() / totalCount
//        val phoneErrorRate = if(phoneCount != 0L) phoneErrorCount.toFloat() / phoneCount else 0f
        val phoneSuccessRate = if(phoneCount != 0L) successCount.toFloat() / phoneCount else 0f

        LogUtils.log("[总计] 总数 -> $totalCount，手机号成功率 -> ${formatPercent(phoneSuccessRate)}%，" +
                "总成功率 -> ${formatPercent(successRate)}%，" +
                "总失败率 -> ${formatPercent(errorRate)}%，" +
                "尝试手机总数 -> $phoneCount，成功次数 -> $successCount，手机号失败次数 -> $phoneErrorCount，总失败次数 -> $errorCount")
    }
}