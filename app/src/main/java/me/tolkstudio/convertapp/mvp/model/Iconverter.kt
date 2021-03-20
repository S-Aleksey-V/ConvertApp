package me.tolkstudio.convertapp.mvp.model

import android.net.Uri
import io.reactivex.rxjava3.core.Completable

interface Iconverter {
    fun convert(): Completable
    fun getSourceUri(): Uri
    fun getTargetUri(): Uri
    fun getSourceTextName(): String
    fun getTargetTextName(): String
}