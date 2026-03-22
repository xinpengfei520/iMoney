package com.xpf.p2p.utils.pay

import android.text.TextUtils

class PayResult(rawResult: String?) {

    var resultStatus: String? = null
        private set
    var result: String? = null
        private set
    var memo: String? = null
        private set

    init {
        if (!TextUtils.isEmpty(rawResult)) {
            val resultParams = rawResult!!.split(";")
            for (resultParam in resultParams) {
                if (resultParam.startsWith("resultStatus")) {
                    resultStatus = gatValue(resultParam, "resultStatus")
                }
                if (resultParam.startsWith("result")) {
                    result = gatValue(resultParam, "result")
                }
                if (resultParam.startsWith("memo")) {
                    memo = gatValue(resultParam, "memo")
                }
            }
        }
    }

    override fun toString(): String {
        return "resultStatus={$resultStatus};memo={$memo};result={$result}"
    }

    private fun gatValue(content: String, key: String): String {
        val prefix = "$key={"
        return content.substring(content.indexOf(prefix) + prefix.length, content.lastIndexOf("}"))
    }
}
