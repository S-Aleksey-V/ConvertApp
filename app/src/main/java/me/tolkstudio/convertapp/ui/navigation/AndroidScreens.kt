package me.tolkstudio.convertapp.ui.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import me.tolkstudio.convertapp.mvp.navigator.IScreens
import me.tolkstudio.convertapp.ui.fragment.FragmentConverter

class AndroidScreens : IScreens {
    override fun converter() = FragmentScreen { FragmentConverter.newInstance() }
}