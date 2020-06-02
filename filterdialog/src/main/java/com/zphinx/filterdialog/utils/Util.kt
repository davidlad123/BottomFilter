package com.zphinx.filterdialog.utils

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset


/**
 *
 * @author David Ladapo
 *
 * Created  on 02 Jun 2020 16:43
 * @copyright Zphinx Software Solutions 2020
 *
 */
object Util {
    @Throws(IOException::class)
    fun loadAssetFile(filename: String, context: Context): String {

        val manager = context.assets

        val inputStream = manager.open(filename)
        return readInputStream(inputStream)
    }


    private fun readInputStream(inputStream: InputStream): String {
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, Charset.forName("UTF-8"))
    }


}