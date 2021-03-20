package me.tolkstudio.convertapp.mvp.prezenter

import com.github.terrakok.cicerone.Router
import me.tolkstudio.convertapp.mvp.navigator.IScreens
import me.tolkstudio.convertapp.mvp.view.MainView
import moxy.MvpPresenter

class MainPresenter(val router: Router, val screens: IScreens) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(screens.converter())
    }

    fun backPressed() {
        router.exit()
    }
}