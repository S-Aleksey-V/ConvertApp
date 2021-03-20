package me.tolkstudio.convertapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.terrakok.cicerone.androidx.AppNavigator
import me.tolkstudio.convertapp.App
import me.tolkstudio.convertapp.IBackClickListener
import me.tolkstudio.convertapp.R
import me.tolkstudio.convertapp.databinding.ActivityMainBinding
import me.tolkstudio.convertapp.mvp.prezenter.MainPresenter
import me.tolkstudio.convertapp.mvp.view.MainView
import me.tolkstudio.convertapp.ui.navigation.AndroidScreens
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class MainActivity : MvpAppCompatActivity(), MainView {
    val navigator = AppNavigator(this, R.id.container)
    private var ui: ActivityMainBinding? = null

    private val presenter by moxyPresenter {
        MainPresenter(App.instance.router, AndroidScreens())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui?.root)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        App.instance.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        App.instance.navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is IBackClickListener && it.backPressed()) {
                return
            }
        }
        presenter.backPressed()
    }
}