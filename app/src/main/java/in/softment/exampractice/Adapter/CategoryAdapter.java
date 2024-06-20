package in.softment.exampractice.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import in.softment.exampractice.AllUserSubcategoriesActivity;
import in.softment.exampractice.Model.CategoryModel;
import in.softment.exampractice.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<CategoryModel> categoryModels;
    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        CategoryModel categoryModel = categoryModels.get(position);

        if (!categoryModel.getThumbnail().isEmpty()) {
            Glide.with(context).load(categoryModel.getThumbnail()).placeholder(R.drawable.placeholder).into(holder.thumbnail);
        }
        holder.catName.setText(categoryModel.getName());
        holder.catQuiz.setText(categoryModel.getTotalQuiz()+" Quiz");



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(context, AllUserSubcategoriesActivity.class);
               intent.putExtra("category",categoryModel);
               context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView catName, catQuiz;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.categoryImage);
            catName = itemView.findViewById(R.id.categoryName);
            catQuiz = itemView.findViewById(R.id.categoryQuiz);

        }
    }
}
