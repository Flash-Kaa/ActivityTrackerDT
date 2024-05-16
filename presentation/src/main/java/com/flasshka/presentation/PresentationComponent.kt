package com.flasshka.presentation

import com.flasshka.presentation.navigation.Router
import dagger.Component

@Component
interface PresentationComponent {
    fun getToast(): Toast

    fun getRouter(): Router
}