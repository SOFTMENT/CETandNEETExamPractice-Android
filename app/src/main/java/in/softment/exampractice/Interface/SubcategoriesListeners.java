package in.softment.exampractice.Interface;

import java.util.ArrayList;

import in.softment.exampractice.Model.SubcategoryModel;


public interface SubcategoriesListeners {

    public void onCallback(ArrayList<SubcategoryModel> subcategoryModels, String error);
}
