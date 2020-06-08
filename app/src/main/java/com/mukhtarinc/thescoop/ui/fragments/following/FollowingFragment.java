package com.mukhtarinc.thescoop.ui.fragments.following;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import com.mukhtarinc.thescoop.R;
import com.mukhtarinc.thescoop.data.network.NetworkResource;
import com.mukhtarinc.thescoop.data.network.sources.SourceResponse;
import com.mukhtarinc.thescoop.databinding.FragmentFollowingBinding;
import com.mukhtarinc.thescoop.model.Source;
import com.mukhtarinc.thescoop.ui.activities.MoreSourcesActivity;
import com.mukhtarinc.thescoop.utils.CategoryListAdapter;
import com.mukhtarinc.thescoop.utils.Constants;
import com.mukhtarinc.thescoop.utils.SourceListAdapter;
import com.mukhtarinc.thescoop.viewmodels.ViewModelProviderFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class FollowingFragment extends DaggerFragment implements View.OnClickListener{

    private static final String TAG = "FollowingFragment";

    @Inject
    CategoryListAdapter categoryListAdapter;

    @Inject
    SourceListAdapter sourceListAdapter;

    FragmentFollowingBinding binding;

    FollowingViewModel viewModel;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    List<Source> sourcesList;


    public FollowingFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static FollowingFragment newInstance() {
        return new FollowingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: ");
        viewModel = ViewModelProviders.of(this,viewModelProviderFactory).get(FollowingViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: ");
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_following,container,false);


        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        binding.popularSourceRv.addItemDecoration(itemDecoration);
        binding.popularSourceRv.setLayoutManager(linearLayoutManager);
        binding.popularSourceRv.hasFixedSize();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        gridLayoutManager.setReverseLayout(false);
        binding.categoriesRv.setLayoutManager(gridLayoutManager);
        binding.categoriesRv.hasFixedSize();

         pullcategories();
        initSources(3);

        binding.linearLayout.setOnClickListener(this);

    }


    void pullcategories() {

        if (binding != null) {
            categoryListAdapter.setData(viewModel.getCategories());
            binding.categoriesRv.setAdapter(categoryListAdapter);
        } else {
            Log.d(TAG, "pullcategories: Binding is null ");

        }

    }


        void initSources(int num){

            viewModel.getSources(Constants.apiKey)
                    .observe(this, new Observer<NetworkResource<SourceResponse>>() {
                        @Override
                        public void onChanged(NetworkResource<SourceResponse> sourceResponseNetworkResource) {

                            if(sourceResponseNetworkResource !=null){



                                switch(sourceResponseNetworkResource.status)
                                {

                                    case LOADING: {

                                        break;
                                    }

                                    case SUCCESS: {


                                        if(sourceResponseNetworkResource.data!=null) {



                                           // sourceListAdapter.setOverflowClickListener(overflowClickListener);
                                           // sourceListAdapter.setArticleItemClickListener(articleItemClickListener);

                                            if(num!=0){
                                                sourceListAdapter.setCount(3);
                                            }
                                            sourcesList = sourceResponseNetworkResource.data.getSources();

                                            sourceListAdapter.setData(sourceResponseNetworkResource.data.getSources());
                                            binding.popularSourceRv.setAdapter(sourceListAdapter);

                                           binding.progressFollow.setVisibility(View.GONE);
                                            binding.constraintFollow.setVisibility(View.VISIBLE);
                                        }
                                        break;
                                    }

                                    case ERROR: {
                                        binding.progressFollow.setVisibility(View.GONE);
                                        binding.noConnection.setVisibility(View.VISIBLE);
                                        binding.constraintFollow.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), sourceResponseNetworkResource.message+"", Toast.LENGTH_SHORT).show();
                                        break;
                                    }

                                }

                            }
                        }
                    });

        }

    @Override
    public void onClick(View view) {


        Intent intent = new Intent(getActivity(), MoreSourcesActivity.class);
        intent.putParcelableArrayListExtra("sources",list2ArrayList(sourcesList));
        getActivity().startActivity(intent);
    }

    public static ArrayList<Source> list2ArrayList(List<Source> sourceList){



        return new ArrayList<>(sourceList);
    }
}
