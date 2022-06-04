package com.example.health_iot_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.health_iot_app.R;
import com.example.health_iot_app.models.Article;
import com.example.health_iot_app.models.CategoryRvModel;
import com.example.health_iot_app.news.NewsApiClient;
import com.example.health_iot_app.news.NewsApiResponse;
import com.example.health_iot_app.utils.CategoriesRvAdapter;
import com.example.health_iot_app.utils.NewsRvAdapter;
import com.example.health_iot_app.utils.SelectListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements SelectListener {

    public static final String ARTICLE_KEY = "ARTICLE_KEY";
    private RecyclerView recyclerViewCategories, newsRecyclerView;
    private NewsRvAdapter newsRvAdapter;
    private CategoriesRvAdapter staticRvAdapter;
    private ArrayList<CategoryRvModel> categories;
    private List<Article> articles = new ArrayList<>();
    private NewsRvAdapter.OnItemClickListener clickListener;
    private Fragment fragment;

    public HomeFragment() {
        // Required empty public constructor
    }

    //Fragment Logic
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        addCategories();
        recyclerViewCategories = view.findViewById(R.id.recycler_categories);
        showCategories(view);
        //NEWS RECYCLER VIEW
        newsRecyclerView = view.findViewById(R.id.recycler_news);
        showNews(view);
        fetchNews();
    }


    //==============================================================CATEGORIES RV==============================================================
    private void showCategories(View view) {
        staticRvAdapter = new CategoriesRvAdapter(categories, this);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategories.setAdapter(staticRvAdapter);
    }

    private void addCategories() {
        categories = new ArrayList<>();
        categories.add(new CategoryRvModel(R.drawable.cardiology, "Cardiologie"));
        categories.add(new CategoryRvModel(R.drawable.pneumology, "Pneumologie"));
        categories.add(new CategoryRvModel(R.drawable.neurology, "Neurologie"));
        categories.add(new CategoryRvModel(R.drawable.reumatology, "Reumatologie"));
        categories.add(new CategoryRvModel(R.drawable.pediatry, "Pediatrie"));
        categories.add(new CategoryRvModel(R.drawable.dermatology, "Dermatologie"));
        categories.add(new CategoryRvModel(R.drawable.orl, "ORL"));
    }

    @Override
    public void onItemClicked(Object object) {
        CategoryRvModel category = (CategoryRvModel) object;
//        Toast.makeText(getContext(), category.getText(), Toast.LENGTH_SHORT).show();
        fragment = DoctorsFragment.newInstance(category);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout_homePage, fragment)
                .commit();
    }

    //==============================================================NEWS RV==============================================================
    private void showNews(View view) {
        newsRecyclerView.setHasFixedSize(true);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        newsRvAdapter = new NewsRvAdapter(articles, getContext(), clickListener);
        newsRecyclerView.setAdapter(newsRvAdapter);

    }

//    private void setOnClickListener() {
//        clickListener = new NewsRvAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(getActivity(), NewsActivity.class);
//                intent.putExtra(ARTICLE_KEY, articles.get(position).getUrl());
//                startActivity(intent);
//            }
//        };
//    }

    private void fetchNews() {
        NewsApiClient.getService().fetchNews().enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(),
                            "Error fetching news",
                            Toast.LENGTH_LONG);
                }
                articles.addAll(response.body().getArticles());
                newsRvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


//    @Override
//    public void (Article article) {
//        Intent intent = new Intent(getActivity(), NewsActivity.class);
//        intent.putExtra(ARTICLE_KEY, article.getUrl());
//        startActivity(intent);
//    }
}