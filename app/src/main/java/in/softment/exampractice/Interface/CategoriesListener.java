package in.softment.exampractice.Interface;

import java.util.ArrayList;

import in.softment.exampractice.Model.CategoryModel;

public interface CategoriesListener {

    public void onCallback(ArrayList<CategoryModel> categoryModels, String error);
}
