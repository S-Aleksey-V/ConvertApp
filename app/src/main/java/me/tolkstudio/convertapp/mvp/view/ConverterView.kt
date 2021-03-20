package me.tolkstudio.convertapp.mvp.view

import android.net.Uri
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

@AddToEndSingle
interface ConverterView : MvpView {

    fun setImg(data: Uri)
    fun setText(data: String)
    fun showProgress()
    fun hideProgress()

    @OneExecution
    fun showSuccess()

    @OneExecution
    fun showError()

    @OneExecution
    fun showCancel()
}