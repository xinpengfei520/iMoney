package com.xpf.p2p.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xpf.p2p.R;


/**
 * A simple {@link Fragment} subclass that displays its page number in a ViewPager.
 * <p>
 * <p>
 * Use the {@link GuidePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuidePageFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_IMG_ID = "param1";

    @Nullable
    private int mResId;

    public GuidePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param resId Parameter 1.
     * @return A new instance of fragment GuidePageFragment.
     */
    public static GuidePageFragment newInstance(@NonNull final int resId) {
        GuidePageFragment fragment = new GuidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMG_ID, resId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mResId = getArguments().getInt(ARG_IMG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guide_page, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        if (imageView != null) {
            imageView.setBackgroundResource(mResId);
        }

        return view;
    }
}
