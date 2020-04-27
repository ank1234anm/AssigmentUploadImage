package com.example.ankit.assigment.fragment;

        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import androidx.lifecycle.Observer;
        import androidx.lifecycle.ViewModelProviders;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import com.example.ankit.assigment.R;
        import com.example.ankit.assigment.activity.ImageDetailActivity;
        import com.example.ankit.assigment.adapter.UploadedImageList;
        import com.example.ankit.assigment.framework.ImageViewModel;
        import com.example.ankit.assigment.model.ImageUploadInfo;

        import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UploadedList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UploadedList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadedList extends Fragment implements UploadedImageList.OnitemClickListner {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView rvHistoryVideoList;
    LinearLayoutManager linearLayoutManager;
    private Context context;
    private UploadedImageList adapter;
    private TextView txtNoHistory;
    private ImageViewModel imageViewModel;

    public UploadedList() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UploadedList newInstance() {
        UploadedList fragment = new UploadedList();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_history_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        getData();
    }

    private void getData() {
        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        imageViewModel.getUploadedImageFromFireBase().observe(this, new Observer<List<ImageUploadInfo>>() {
            @Override
            public void onChanged(List<ImageUploadInfo> imageUploadInfos) {
                setData(imageUploadInfos);
            }

        });
    }

    private void setData(List<ImageUploadInfo> response) {
        if (response != null && !response.isEmpty()) {
            txtNoHistory.setVisibility(View.GONE);
            rvHistoryVideoList.setVisibility(View.VISIBLE);
            adapter = new UploadedImageList(context, this, response);
            rvHistoryVideoList.setAdapter(adapter);
        } else {
            rvHistoryVideoList.setVisibility(View.GONE);
            txtNoHistory.setVisibility(View.VISIBLE);
        }

    }

    private void findView(View view) {
        rvHistoryVideoList = (RecyclerView) view.findViewById(R.id.rvHistoryVideoList);
        txtNoHistory = (TextView) view.findViewById(R.id.txtNoHistory);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvHistoryVideoList.setLayoutManager(linearLayoutManager);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void detail(ImageUploadInfo imageUploadInfo) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra("uri",imageUploadInfo.imageURL);
        startActivity(intent);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
