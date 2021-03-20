package me.tolkstudio.convertapp.mvp.prezenter

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import me.tolkstudio.convertapp.ConverterLogik
import me.tolkstudio.convertapp.mvp.view.ConverterView
import moxy.MvpPresenter

class ConverterPrezenter(val scheduler: Scheduler, val router: Router) :
    MvpPresenter<ConverterView>() {

    var imageConverter: ConverterLogik? = null
    private var disposable: Disposable? = null

    fun btnConvertClick() {
        convertJpg2Png()
    }

    private fun convertJpg2Png() = imageConverter?.let { converter ->
        viewState.showProgress()
        disposable = converter.convert()
            .observeOn(scheduler)
            .subscribe({
                viewState.hideProgress()
                viewState.showSuccess()
                viewState.setImg(converter.getTargetUri())
                viewState.setText(converter.getTargetTextName())

            }, { error ->
                println("onError: ${error.message}")
                viewState.hideProgress()
                viewState.showError()
            })
    }

    fun setData(imageConverter: ConverterLogik) {
        this.imageConverter = imageConverter
        viewState.setImg(imageConverter.getSourceUri())
        viewState.setText(imageConverter.getSourceTextName())
    }

    fun cancelPressed() {
        disposable?.dispose()
        viewState.hideProgress()
        viewState.showCancel()
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}