package com.xpf.p2p.utils.pay

object Base64 {

    private const val BASELENGTH = 128
    private const val LOOKUPLENGTH = 64
    private const val TWENTYFOURBITGROUP = 24
    private const val EIGHTBIT = 8
    private const val SIXTEENBIT = 16
    private const val FOURBYTE = 4
    private const val SIGN = -128
    private val PAD = '='
    private val base64Alphabet = ByteArray(BASELENGTH)
    private val lookUpBase64Alphabet = CharArray(LOOKUPLENGTH)

    init {
        for (i in 0 until BASELENGTH) {
            base64Alphabet[i] = -1
        }
        for (i in 'Z'.code downTo 'A'.code) {
            base64Alphabet[i] = (i - 'A'.code).toByte()
        }
        for (i in 'z'.code downTo 'a'.code) {
            base64Alphabet[i] = (i - 'a'.code + 26).toByte()
        }
        for (i in '9'.code downTo '0'.code) {
            base64Alphabet[i] = (i - '0'.code + 52).toByte()
        }
        base64Alphabet['+'.code] = 62
        base64Alphabet['/'.code] = 63

        for (i in 0..25) {
            lookUpBase64Alphabet[i] = ('A'.code + i).toChar()
        }
        var j = 0
        for (i in 26..51) {
            lookUpBase64Alphabet[i] = ('a'.code + j).toChar()
            j++
        }
        j = 0
        for (i in 52..61) {
            lookUpBase64Alphabet[i] = ('0'.code + j).toChar()
            j++
        }
        lookUpBase64Alphabet[62] = '+'
        lookUpBase64Alphabet[63] = '/'
    }

    private fun isWhiteSpace(octect: Char): Boolean {
        return octect.code == 0x20 || octect.code == 0xd || octect.code == 0xa || octect.code == 0x9
    }

    private fun isPad(octect: Char): Boolean {
        return octect == PAD
    }

    private fun isData(octect: Char): Boolean {
        return octect.code < BASELENGTH && base64Alphabet[octect.code].toInt() != -1
    }

    @JvmStatic
    fun encode(binaryData: ByteArray?): String? {
        if (binaryData == null) return null

        val lengthDataBits = binaryData.size * EIGHTBIT
        if (lengthDataBits == 0) return ""

        val fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP
        val numberTriplets = lengthDataBits / TWENTYFOURBITGROUP
        val numberQuartet = if (fewerThan24bits != 0) numberTriplets + 1 else numberTriplets
        val encodedData = CharArray(numberQuartet * 4)

        var encodedIndex = 0
        var dataIndex = 0

        for (i in 0 until numberTriplets) {
            val b1 = binaryData[dataIndex++]
            val b2 = binaryData[dataIndex++]
            val b3 = binaryData[dataIndex++]

            val l = (b2.toInt() and 0x0f).toByte()
            val k = (b1.toInt() and 0x03).toByte()

            val val1 = if (b1.toInt() and SIGN == 0) (b1.toInt() shr 2).toByte()
            else (b1.toInt() shr 2 xor 0xc0).toByte()
            val val2 = if (b2.toInt() and SIGN == 0) (b2.toInt() shr 4).toByte()
            else (b2.toInt() shr 4 xor 0xf0).toByte()
            val val3 = if (b3.toInt() and SIGN == 0) (b3.toInt() shr 6).toByte()
            else (b3.toInt() shr 6 xor 0xfc).toByte()

            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1.toInt()]
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2.toInt() or (k.toInt() shl 4)]
            encodedData[encodedIndex++] = lookUpBase64Alphabet[(l.toInt() shl 2) or val3.toInt()]
            encodedData[encodedIndex++] = lookUpBase64Alphabet[b3.toInt() and 0x3f]
        }

        if (fewerThan24bits == EIGHTBIT) {
            val b1 = binaryData[dataIndex]
            val k = (b1.toInt() and 0x03).toByte()
            val val1 = if (b1.toInt() and SIGN == 0) (b1.toInt() shr 2).toByte()
            else (b1.toInt() shr 2 xor 0xc0).toByte()
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1.toInt()]
            encodedData[encodedIndex++] = lookUpBase64Alphabet[k.toInt() shl 4]
            encodedData[encodedIndex++] = PAD
            encodedData[encodedIndex++] = PAD
        } else if (fewerThan24bits == SIXTEENBIT) {
            val b1 = binaryData[dataIndex]
            val b2 = binaryData[dataIndex + 1]
            val l = (b2.toInt() and 0x0f).toByte()
            val k = (b1.toInt() and 0x03).toByte()
            val val1 = if (b1.toInt() and SIGN == 0) (b1.toInt() shr 2).toByte()
            else (b1.toInt() shr 2 xor 0xc0).toByte()
            val val2 = if (b2.toInt() and SIGN == 0) (b2.toInt() shr 4).toByte()
            else (b2.toInt() shr 4 xor 0xf0).toByte()
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1.toInt()]
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2.toInt() or (k.toInt() shl 4)]
            encodedData[encodedIndex++] = lookUpBase64Alphabet[l.toInt() shl 2]
            encodedData[encodedIndex++] = PAD
        }

        return String(encodedData)
    }

    @JvmStatic
    fun decode(encoded: String?): ByteArray? {
        if (encoded == null) return null

        val base64Data = encoded.toCharArray()
        val len = removeWhiteSpace(base64Data)

        if (len % FOURBYTE != 0) return null

        val numberQuadruple = len / FOURBYTE
        if (numberQuadruple == 0) return ByteArray(0)

        val decodedData = ByteArray(numberQuadruple * 3)
        var b1: Byte
        var b2: Byte
        var b3: Byte
        var b4: Byte
        var d1: Char
        var d2: Char
        var d3: Char
        var d4: Char

        var i = 0
        var encodedIndex = 0
        var dataIndex = 0

        while (i < numberQuadruple - 1) {
            d1 = base64Data[dataIndex++]
            d2 = base64Data[dataIndex++]
            d3 = base64Data[dataIndex++]
            d4 = base64Data[dataIndex++]
            if (!isData(d1) || !isData(d2) || !isData(d3) || !isData(d4)) return null

            b1 = base64Alphabet[d1.code]
            b2 = base64Alphabet[d2.code]
            b3 = base64Alphabet[d3.code]
            b4 = base64Alphabet[d4.code]

            decodedData[encodedIndex++] = (b1.toInt() shl 2 or (b2.toInt() shr 4)).toByte()
            decodedData[encodedIndex++] = ((b2.toInt() and 0xf) shl 4 or ((b3.toInt() shr 2) and 0xf)).toByte()
            decodedData[encodedIndex++] = (b3.toInt() shl 6 or b4.toInt()).toByte()
            i++
        }

        d1 = base64Data[dataIndex++]
        d2 = base64Data[dataIndex++]
        if (!isData(d1) || !isData(d2)) return null

        b1 = base64Alphabet[d1.code]
        b2 = base64Alphabet[d2.code]

        d3 = base64Data[dataIndex++]
        d4 = base64Data[dataIndex++]
        if (!isData(d3) || !isData(d4)) {
            if (isPad(d3) && isPad(d4)) {
                if (b2.toInt() and 0xf != 0) return null
                val tmp = ByteArray(i * 3 + 1)
                System.arraycopy(decodedData, 0, tmp, 0, i * 3)
                tmp[encodedIndex] = (b1.toInt() shl 2 or (b2.toInt() shr 4)).toByte()
                return tmp
            } else if (!isPad(d3) && isPad(d4)) {
                b3 = base64Alphabet[d3.code]
                if (b3.toInt() and 0x3 != 0) return null
                val tmp = ByteArray(i * 3 + 2)
                System.arraycopy(decodedData, 0, tmp, 0, i * 3)
                tmp[encodedIndex++] = (b1.toInt() shl 2 or (b2.toInt() shr 4)).toByte()
                tmp[encodedIndex] = ((b2.toInt() and 0xf) shl 4 or ((b3.toInt() shr 2) and 0xf)).toByte()
                return tmp
            } else {
                return null
            }
        } else {
            b3 = base64Alphabet[d3.code]
            b4 = base64Alphabet[d4.code]
            decodedData[encodedIndex++] = (b1.toInt() shl 2 or (b2.toInt() shr 4)).toByte()
            decodedData[encodedIndex++] = ((b2.toInt() and 0xf) shl 4 or ((b3.toInt() shr 2) and 0xf)).toByte()
            decodedData[encodedIndex++] = (b3.toInt() shl 6 or b4.toInt()).toByte()
        }

        return decodedData
    }

    private fun removeWhiteSpace(data: CharArray?): Int {
        if (data == null) return 0
        var newSize = 0
        for (i in data.indices) {
            if (!isWhiteSpace(data[i])) {
                data[newSize++] = data[i]
            }
        }
        return newSize
    }
}
