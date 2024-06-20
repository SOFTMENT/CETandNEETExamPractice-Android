package in.softment.exampractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import in.softment.exampractice.Model.SolvedPaperModel;
import in.softment.exampractice.Model.UserModel;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_result);

        SolvedPaperModel solvedPaperModel = (SolvedPaperModel) getIntent().getSerializableExtra("solvedModel");

        RoundedImageView profileImage = findViewById(R.id.profileImage);
        TextView userName = findViewById(R.id.userName);

        TextView score = findViewById(R.id.score);
        TextView totalQuestions = findViewById(R.id.totalQuestions);
        TextView totalTrue = findViewById(R.id.totalTrue);
        TextView totalWrong = findViewById(R.id.totalWrong);

        if (!UserModel.data.getProfileImage().isEmpty()) {
            Glide.with(this).load(UserModel.data.getProfileImage()).placeholder(R.drawable.profile_placeholder).into(profileImage);
        }

        userName.setText(UserModel.data.fullName);

        score.setText(String.format("%.2f",solvedPaperModel.getPercentage()));
        totalQuestions.setText(String.valueOf(solvedPaperModel.getTotalQuestion()));
        totalTrue.setText(String.valueOf(solvedPaperModel.getTotalCorrect()));
        totalWrong.setText(String.valueOf(solvedPaperModel.getTotalWrong()));


        findViewById(R.id.checkAnswer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(ResultActivity.this, CheckAnswerActivity.class);
                    intent.putExtra("solvedPaperModel",solvedPaperModel);
                    startActivity(intent);
            }
        });

        findViewById(R.id.goHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}
