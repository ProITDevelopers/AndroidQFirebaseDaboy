package proitdevelopers.com.androidquizfirebase;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import proitdevelopers.com.androidquizfirebase.Common.Common;
import proitdevelopers.com.androidquizfirebase.Interface.ItemClickListener;
import proitdevelopers.com.androidquizfirebase.Model.Category;
import proitdevelopers.com.androidquizfirebase.ViewHolder.CategoryViewHolder;

public class CategoryFragment extends Fragment {

    View myFragment;
    RecyclerView listCategory;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter ;
    FirebaseDatabase database;
    DatabaseReference categories;

    public static CategoryFragment newInstance(){
        CategoryFragment categoryFragment = new CategoryFragment();
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        categories = database.getReference("Category");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_category,container,false);
        //Carrega o menu
        listCategory =  myFragment.findViewById(R.id.listCategory);
        listCategory.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        listCategory.setLayoutManager(layoutManager);
    
        loadCategories();

        return myFragment;
    }

    private void loadCategories() {

        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(categories, Category.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull final Category model) {
                holder.category_name.setText(model.getName());;
                Picasso.get().load(model.getImage()).into(holder.category_image);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(getActivity(),String.format("%s|%s",adapter.getRef(position).getKey(),model.getName()),Toast.LENGTH_SHORT).show();
                        Intent startGame = new Intent(getActivity(),Start.class);
                        Common.categoryId = adapter.getRef(position).getKey();
                        Common.categoryName = model.getName();
                        startActivity(startGame);
                    }
                });
            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.category_layout, viewGroup, false);
                return new CategoryViewHolder(view);
            }
        };
        listCategory.setAdapter(adapter);
        adapter.startListening();

    }

}

