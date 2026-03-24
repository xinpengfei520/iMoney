package com.xpf.p2p.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xpf.p2p.databinding.FragmentGuidePageBinding

/**
 * A simple Fragment subclass that displays its page number in a ViewPager.
 *
 * Use the [GuidePageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GuidePageFragment : Fragment() {

    private var _binding: FragmentGuidePageBinding? = null
    private val binding get() = _binding!!
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
    ): View {
        _binding = FragmentGuidePageBinding.inflate(inflater, container, false)
        binding.imageView.setBackgroundResource(mResId)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
