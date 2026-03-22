package com.xpf.p2p.utils.pay

import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec

object SignUtils {

    private const val ALGORITHM = "RSA"
    private const val SIGN_ALGORITHMS = "SHA1WithRSA"
    private const val DEFAULT_CHARSET = "UTF-8"

    @JvmStatic
    fun sign(content: String, privateKey: String): String? {
        try {
            val priPKCS8 = PKCS8EncodedKeySpec(Base64.decode(privateKey))
            val keyf = KeyFactory.getInstance(ALGORITHM)
            val priKey = keyf.generatePrivate(priPKCS8)

            val signature = java.security.Signature.getInstance(SIGN_ALGORITHMS)
            signature.initSign(priKey)
            signature.update(content.toByteArray(charset(DEFAULT_CHARSET)))

            val signed = signature.sign()
            return Base64.encode(signed)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
