package com.xpf.p2p.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.xpf.p2p.R

/**
 * A simple Fragment subclass that displays its page number in a ViewPager.
 *
 * Use the [GuidePageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GuidePageFragment : Fragment() {

    private var mResId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mResId = it.getInt(ARG_IMG_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_guide_page, container, false)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        imageView?.setBackgroundResource(mResId)
        return view
    }

    companion object {
        private const val ARG_IMG_ID = "param1"

        @JvmStatic
        fun newInstance(resId: Int): GuidePageFragment {
            return GuidePageFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_IMG_ID, resId)
                }
            }
        }
    }
}
