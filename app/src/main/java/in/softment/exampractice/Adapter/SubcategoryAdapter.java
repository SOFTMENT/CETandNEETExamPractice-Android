package in.softment.exampractice.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


import in.softment.exampractice.AllUserSubcategoriesActivity;
import in.softment.exampractice.Interface.SolvedPaperListener;
import in.softment.exampractice.Model.SolvedPaperModel;
import in.softment.exampractice.Model.SubcategoryModel;
import in.softment.exampractice.Model.UserModel;
import in.softment.exampractice.QuestionAnswerActivity;
import in.softment.exampractice.R;
import in.softment.exampractice.ResultActivity;
import in.softment.exampractice.ResumeQuestionActivity;
import in.softment.exampractice.Util.Services;

public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.ViewHolder> {

    private String catName;
    private ArrayList<SubcategoryModel> subcategoryModels;
    private AllUserSubcategoriesActivity subcategoriesActivity;
    public SubcategoryAdapter(AllUserSubcategoriesActivity subcategoriesActivity, String catName,ArrayList<SubcategoryModel> subcategoryModels ) {
        this.subcategoriesActivity = subcategoriesActivity;
        this.catName = catName;
        this.subcategoryModels = subcategoryModels;
    }
    @NonNull
    @Override
    public SubcategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(subcategoriesActivity).inflate(R.layout.user_subcategory_view_cell,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubcategoryAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        SubcategoryModel subcategoryModel = subcategoryModels.get(position);
        holder.name.setText(subcategoryModel.getName());
        holder.questionCount.setText(subcategoryModel.getTotalQuestions()+" Questions");

        Services.getSolvedPaper(UserModel.data.getUid(), subcategoryModel.getId(), new SolvedPaperListener() {
            @Override
            public void onCallBack(SolvedPaperModel solvedPaperModel, String error) {
                if (error == null) {
                    holder.start.setVisibility(View.GONE);
                    subcategoryModel.solvedPaperModel  = solvedPaperModel;
                    if (subcategoryModel.solvedPaperModel.completed) {
                        holder.checkResults.setVisibility(View.VISIBLE);
                        holder.resume.setVisibility(View.GONE);
                    }
                    else {
                        holder.checkResults.setVisibility(View.GONE);
                        holder.resume.setVisibility(View.VISIBLE);
                    }


                }
                else {
                    holder.start.setVisibility(View.VISIBLE);
                    holder.checkResults.setVisibility(View.GONE);
                    subcategoryModel.solvedPaperModel  = null;
                }

            }
        });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (subcategoryModel.solvedPaperModel == null) {

                        Intent intent = new Intent(subcategoriesActivity, QuestionAnswerActivity.class);
                        intent.putExtra("subcategory",subcategoryModel);
                        intent.putExtra("catName",catName);

                        subcategoriesActivity.startActivityForResult(intent,123);
                    }
                    else  {
                        Intent intent;
                        if (subcategoryModel.solvedPaperModel.completed) {

                            intent = new Intent(subcategoriesActivity, ResultActivity.class);
                        }
                        else {

                            intent = new Intent(subcategoriesActivity, ResumeQuestionActivity.class);
                        }
                        intent.putExtra("solvedModel",subcategoryModel.solvedPaperModel);
                        subcategoriesActivity.startActivity(intent);



                    }
                }
            });


    }

    @Override
    public int getItemCount() {
        return subcategoryModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, questionCount;
        TextView checkResults;
        TextView resume;
        TextView start;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            questionCount = itemView.findViewById(R.id.questionsCount);
            checkResults = itemView.findViewById(R.id.result);
            resume = itemView.findViewById(R.id.resume);
            start = itemView.findViewById(R.id.start);

        }
    }
}
