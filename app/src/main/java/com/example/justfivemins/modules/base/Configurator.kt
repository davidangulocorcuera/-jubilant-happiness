package com.example.justfivemins.modules.base

import androidx.annotation.ColorRes
import com.example.justfivemins.R

class Configurator {
    @ColorRes
    var toolbarColor: Int = 0
        internal set
    var isShowOnlyToolbarTitle: Boolean = false
        internal set
    @ColorRes
    var titleColor: Int = 0
        internal set
    var title: String? = null
        internal set
    var hasToolbar: Boolean = false
        internal set
    var hasToolbarBackButton: Boolean = false
        internal set
    var hasOwnToolbar: Boolean = false
        internal set
    var hasCloseButton: Boolean = false

    var onCloseClicked: (() -> Unit)? = null

    @ColorRes
    var backButtonColor: Int = 0
        internal set

    fun toolbarColor(toolbarColor: Int): Configurator {
        this.toolbarColor = toolbarColor
        return this
    }

    fun showOnlyToolbarTitle(showOnlyToolbarTitle: Boolean): Configurator {
        this.isShowOnlyToolbarTitle = showOnlyToolbarTitle
        return this
    }

    fun titleColor(titleColor: Int): Configurator {
        this.titleColor = titleColor
        return this
    }

    fun title(title: String?): Configurator {
        this.title = title
        return this
    }

    fun hasToolbar(hasToolbar: Boolean): Configurator {
        this.hasToolbar = hasToolbar
        return this
    }

    fun hasCloseButton(hasCloseButton: Boolean): Configurator {
        this.hasCloseButton = hasCloseButton
        hasToolbar(hasCloseButton)
        return this
    }

    fun hasToolbarBackButton(hasToolbarBackButton: Boolean): Configurator {
        this.hasToolbarBackButton = hasToolbarBackButton
        this.hasToolbar = hasToolbarBackButton
        return this
    }

    fun hasOwnToolbar(hasOwnToolbar: Boolean): Configurator {
        this.hasOwnToolbar = hasOwnToolbar
        return this
    }

    fun checkDefaults() {

        if (toolbarColor == 0) {
            toolbarColor = R.color.colorPrimaryDark
        }

        if (titleColor == 0) {
            titleColor = R.color.dark_blue
        }

        if (backButtonColor == 0) {
            backButtonColor = R.color.dark_blue
        }
    }

    fun setOnCloseListener(function: () -> Unit) {
        hasToolbarBackButton = false
        hasToolbar = true
        this.onCloseClicked = function
        hasCloseButton = true
    }

    companion object {

        val instance: Configurator
            get() = Configurator()
    }
}