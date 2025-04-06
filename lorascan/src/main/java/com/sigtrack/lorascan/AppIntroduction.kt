package com.sigtrack.lorascan

import android.os.Bundle
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.sigtrack.lorascan.model.UIViewModel
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment

class AppIntroduction : AppIntro() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(
            AppIntroFragment.createInstance(
            title = resources.getString(R.string.intro_welcome),
            description = resources.getString(R.string.intro_welcome_text),
            imageDrawable = R.mipmap.loralogo,
            backgroundColorRes = R.color.appintro_background_color,
            descriptionColorRes = R.color.colorOnPrimary
        ))
        //addSlide(SlideTwoFragment())
    }

    private fun done() {
        val prefs = UIViewModel.getPreferences(this)
        prefs.edit { putBoolean("app_intro_completed", true) }
        finish()
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        done()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        done()
    }
}