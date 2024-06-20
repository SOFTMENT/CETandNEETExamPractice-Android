package in.softment.exampractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import in.softment.exampractice.Adapter.SubcategoryAdapter;
import in.softment.exampractice.Interface.SubcategoriesListeners;
import in.softment.exampractice.Model.CategoryModel;
import in.softment.exampractice.Model.SubcategoryModel;
import in.softment.exampractice.Util.ProgressHud;
import in.softment.exampractice.Util.Services;

public class AllUserSubcategoriesActivity extends AppCompatActivity {
    SubcategoryAdapter subcategoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user_subcategories);

        CategoryModel categoryModel = (CategoryModel) getIntent().getSerializableExtra("category");
        if (categoryModel == null) {
            finish();
            return;
        }
        TextView no_subcategories_available = findViewById(R.id.no_subcategories_available);
        TextView category_name = findViewById(R.id.categoryName);
        category_name.setText(categoryModel.getName());

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<SubcategoryModel> subcategoryModels = new ArrayList<>();
        subcategoryAdapter = new SubcategoryAdapter(this,categoryModel.getName(),subcategoryModels);
        recyclerView.setAdapter(subcategoryAdapter);

        ProgressHud.show(this,"");

        Services.getAllSubcategory(categoryModel.getId(), new SubcategoriesListeners() {
            @Override
            public void onCallback(ArrayList<SubcategoryModel> subCatModels, String error) {
                ProgressHud.dialog.dismiss();
                if (error == null) {
                    subcategoryModels.clear();
                    subcategoryModels.addAll(subCatModels);
                    if (subcategoryModels.size() > 0) {
                        no_subcategories_available.setVisibility(View.GONE);
                    }
                    else {
                        no_subcategories_available.setVisibility(View.VISIBLE);
                    }
                    subcategoryAdapter.notifyDataSetChanged();
                }
                else {
                    Services.showDialog(AllUserSubcategoriesActivity.this,"ERROR",error);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == 123 && resultCode == RESULT_OK) {
             Log.d("VIJAYRATHORE","lsjsdjsdjksjd");
            subcategoryAdapter.notifyDataSetChanged();
        }
    }
}
