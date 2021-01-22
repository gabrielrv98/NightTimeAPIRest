package com.esei.grvidal.nightTimeApi.encryptation

import com.esei.grvidal.nightTimeApi.NightTimeApiApplication
import com.esei.grvidal.nightTimeApi.exception.ServiceException
import jdk.internal.dynalink.support.NameCodec.encode
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.encoders.Base64
import org.slf4j.LoggerFactory
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.Security
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec


const val algorithm = "AES"

class Encoding {



    companion object getInstance {

        fun to16Chars(pass: String): String{

            var string = pass
            while (string.length < 16 ){
                string = string.plus(string)
            }
            return string.substring(0,16)
        }


        fun encrypt(strToEncrypt: String, secret_key: String): String {
            Security.addProvider(BouncyCastleProvider())
            val keyBytes: ByteArray

            val sub_secret_key = to16Chars(secret_key)


            try {

                keyBytes = sub_secret_key.toByteArray(charset("UTF8"))
                val skey = SecretKeySpec(keyBytes, algorithm)
                val input = strToEncrypt.toByteArray(charset("UTF8"))

                synchronized(Cipher::class.java) {
                    val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
                    cipher.init(Cipher.ENCRYPT_MODE, skey)

                    val cipherText = ByteArray(cipher.getOutputSize(input.size))
                    var ctLength = cipher.update(
                        input, 0, input.size,
                        cipherText, 0
                    )
                    ctLength += cipher.doFinal(cipherText, ctLength)
                    return String(
                        Base64.encode(cipherText)
                    )
                }

            } catch (uee: UnsupportedEncodingException) {
                throw ServiceException("Unsupported encoding exception ${uee.printStackTrace()}")

            } catch (ibse: IllegalBlockSizeException) {
                throw ServiceException("Illegal BlockSize Exception ${ibse.printStackTrace()}")

            } catch (bpe: BadPaddingException) {
                throw ServiceException("Bad Padding Exception ${bpe.printStackTrace()}")

            } catch (ike: InvalidKeyException) {
                throw ServiceException("Bad Padding Exception ${ike.printStackTrace()}")

            } catch (nspe: NoSuchPaddingException) {
                throw ServiceException("Bad Padding Exception ${nspe.printStackTrace()}")

            } catch (nsae: NoSuchAlgorithmException) {
                throw ServiceException("Bad Padding Exception ${nsae.printStackTrace()}")

            } catch (e: ShortBufferException) {
                throw ServiceException("Bad Padding Exception ${e.printStackTrace()}")

            }

        }

        fun decrypt(key: String, strToDecrypt: String?): String {
            Security.addProvider(BouncyCastleProvider())
            val keyBytes: ByteArray

            val sub_secret_key = to16Chars(key)

            try {
                keyBytes = sub_secret_key.toByteArray(charset("UTF8"))
                val skey = SecretKeySpec(keyBytes, algorithm)
                val input = Base64
                    .decode(strToDecrypt?.trim { it <= ' ' }?.toByteArray(charset("UTF8")))

                synchronized(Cipher::class.java) {
                    val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
                    cipher.init(Cipher.DECRYPT_MODE, skey)

                    val plainText = ByteArray(cipher.getOutputSize(input.size))
                    var ptLength = cipher.update(input, 0, input.size, plainText, 0)
                    ptLength += cipher.doFinal(plainText, ptLength)
                    val decryptedString = String(plainText)
                    return decryptedString.trim { it <= ' ' }
                }
            } catch (uee: UnsupportedEncodingException) {
                throw ServiceException("Unsupported encoding exception ${uee.printStackTrace()}")

            } catch (ibse: IllegalBlockSizeException) {
                throw ServiceException("Illegal BlockSize Exception ${ibse.printStackTrace()}")

            } catch (bpe: BadPaddingException) {
                throw ServiceException("Bad Padding Exception ${bpe.printStackTrace()}")

            } catch (ike: InvalidKeyException) {
                throw ServiceException("Bad Padding Exception ${ike.printStackTrace()}")

            } catch (nspe: NoSuchPaddingException) {
                throw ServiceException("Bad Padding Exception ${nspe.printStackTrace()}")

            } catch (nsae: NoSuchAlgorithmException) {
                throw ServiceException("Bad Padding Exception ${nsae.printStackTrace()}")

            } catch (e: ShortBufferException) {
                throw ServiceException("Bad Padding Exception ${e.printStackTrace()}")

            }
        }


    }


}