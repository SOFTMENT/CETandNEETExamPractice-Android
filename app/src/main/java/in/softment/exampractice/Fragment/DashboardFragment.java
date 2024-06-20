package in.softment.exampractice.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import in.softment.exampractice.Adapter.CategoryAdapter;
import in.softment.exampractice.Interface.CategoriesListener;
import in.softment.exampractice.Model.CategoryModel;
import in.softment.exampractice.Model.UserModel;
import in.softment.exampractice.NotificationActivity;
import in.softment.exampractice.R;
import in.softment.exampractice.Util.ProgressHud;
import in.softment.exampractice.Util.Services;


public class DashboardFragment extends Fragment {
    private CategoryAdapter categoryAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        RoundedImageView profileImage = view.findViewById(R.id.profileImage);
        if (!UserModel.data.getProfileImage().isEmpty()) {
            Glide.with(this).load(UserModel.data.getProfileImage()).placeholder(R.drawable.profile_placeholder).into(profileImage);
        }

        TextView heading = view.findViewById(R.id.heading);
        heading.setText("Hey "+ UserModel.data.getFullName().split(" ")[0]+", what subject\ndo you want to improve today?");

        ArrayList<CategoryModel> categoryModels = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryModels);
        recyclerView.setAdapter(categoryAdapter);

        //NOTIFICATION
        view.findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NotificationActivity.class));
            }
        });

        ProgressHud.show(getContext(),"");
        Services.getAllCategory(new CategoriesListener() {
            @Override
            public void onCallback(ArrayList<CategoryModel> catModels, String error) {
                ProgressHud.dialog.dismiss();
                if (error == null) {
                    categoryModels.clear();
                    categoryModels.addAll(catModels);
                    categoryAdapter.notifyDataSetChanged();
                }
                else {
                    //Services.showDialog(getContext(),"ERROR",error);
                }
            }
        });



        return view;
    }
}
