package me.tolkstudio.convertapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.net.toUri
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import me.tolkstudio.convertapp.mvp.model.Iconverter
import java.io.File
import java.io.FileOutputStream

class ConverterLogik(private val context: Context, private val uri: Uri) : Iconverter {

    private var targetFile: File
    private var targetUri: Uri

    init {
        targetFile = File(context.getExternalFilesDir(null), getTargetTextName())
        targetUri = targetFile.toUri()
    }

    override fun convert(): Completable = Completable.fromAction {
        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            return@fromAction
        }

        val bytes = context.contentResolver?.openInputStream(uri)?.buffered()?.use {
            it.readBytes()
        }

        bytes?.let { byteArray ->
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            FileOutputStream(targetFile).use { foStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, foStream)
            }
        }
    }.subscribeOn(Schedulers.io())

    override fun getSourceUri() = uri
    override fun getTargetUri() = targetUri

    @SuppressLint("Recycle")
    override fun getSourceTextName(): String {
        var path: String = ""
        context.contentResolver.query(uri, null, null, null, null, null)?.apply {
            if (moveToFirst()) {
                path = getString(getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        return path
    }

    override fun getTargetTextName() = getSourceTextName().substringBeforeLast(".") + ".jpg"
}