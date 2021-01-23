package dev.clean.rocket.booster.Volume

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import dev.clean.rocket.booster.inapp.DiamondFrag
import dev.clean.rocket.booster.presentation.battery.BatterySaverFrag
import dev.clean.rocket.booster.presentation.booster.PhoneBoosterFrag
import dev.clean.rocket.booster.presentation.cpu.CPUCoolerFrag
import dev.clean.rocket.booster.presentation.files.JunkCleanerFrag

/**
 * Created by intag pc on 2/12/2017.
 */

class MyPagerAdapter(fm: FragmentManager, internal var mNumOfTabs: Int) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> {
                return PhoneBoosterFrag()
            }
            1 -> {
                return BatterySaverFrag()
            }
            2 -> {
                return CPUCoolerFrag()
            }
            3 -> {
                return JunkCleanerFrag()
            }
            else -> {
                //return NoAdsFrag.MyFragment()
                return DiamondFrag.newInstance("kek")
            }
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }
}
