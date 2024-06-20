package in.softment.exampractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.softment.exampractice.Interface.QuestionsListener;
import in.softment.exampractice.Model.QuestionModel;
import in.softment.exampractice.Model.SolvedPaperModel;
import in.softment.exampractice.Model.SubcategoryModel;
import in.softment.exampractice.Util.ProgressHud;
import in.softment.exampractice.Util.Services;

public class CheckAnswerActivity extends AppCompatActivity {
    TextView questionCompletedTV,questionTV,choice1,choice2,choice3,choice4;
    LinearLayout choiceView1,choiceView2,choiceView3,choiceView4;
    private int count = 0;
    private int totalQuestion = 1;
    private ArrayList<QuestionModel> questionModels = new ArrayList<>();
    private RoundedImageView queImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_check_answer);


        SolvedPaperModel solvedPaperModel = (SolvedPaperModel) getIntent().getSerializableExtra("solvedPaperModel");

        if (solvedPaperModel == null || solvedPaperModel.getQuestionModels().size() == 0){
            finish();
        }



        questionModels.addAll(solvedPaperModel.getQuestionModels());



        questionCompletedTV = findViewById(R.id.questionCompletedTV);
        questionTV = findViewById(R.id.questionTV);
        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);
        choice3 = findViewById(R.id.choice3);
        choice4 = findViewById(R.id.choice4);

        choiceView1 = findViewById(R.id.choiceView1);
        choiceView2 = findViewById(R.id.choiceView2);
        choiceView3 = findViewById(R.id.choiceView3);
        choiceView4 = findViewById(R.id.choiceView4);

        queImage = findViewById(R.id.queImage);

        totalQuestion = solvedPaperModel.getTotalQuestion();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        AppCompatButton nextBtn = findViewById(R.id.nextBtn);
        AppCompatButton previousBtn  = findViewById(R.id.previousBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!questionModels.get(count).getUserAnswer().isEmpty()) {

                    if (count >= questionModels.size() - 1) {

                        finish();

                    }
                    else {
                        count++;
                        previousBtn.setVisibility(View.VISIBLE);

                        if (count == questionModels.size() - 1) {
                            nextBtn.setText("Finish");

                        }

                        nextQuestionAndAnswers();
                    }

                }

            }
        });


        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                count--;
                nextBtn.setText("NEXT");
                if (count == 0) {
                    previousBtn.setVisibility(View.GONE);
                }
                previousQuestionAndAnswers();


            }
        });


        nextQuestionAndAnswers();
    }




    public void nextQuestionAndAnswers(){

        questionCompletedTV.setText("Question "+(count + 1)+ " of "+ totalQuestion);
        QuestionModel questionModel = questionModels.get(count);
        questionTV.setText(questionModel.getQuestion());
        choice1.setText(questionModel.getOptionA());
        choice2.setText(questionModel.getOptionB());
        choice3.setText(questionModel.getOptionC());
        choice4.setText(questionModel.getOptionD());

        if (questionModel.getQuestionImage()) {
            queImage.setVisibility(View.VISIBLE);
        }
        else {
            queImage.setVisibility(View.GONE);
        }

        setSelectedAnswer(questionModel);

    }
    public void setSelectedAnswer(QuestionModel questionModel) {
        clearSelection();

        if (questionModel.getUserAnswer().equalsIgnoreCase("A")) {
            choiceView1.setBackgroundResource(R.drawable.wrong_answer_back);
        }
        else if (questionModel.getUserAnswer().equalsIgnoreCase("B")) {
            choiceView2.setBackgroundResource(R.drawable.wrong_answer_back);
        }
        else if (questionModel.getUserAnswer().equalsIgnoreCase("C")) {
            choiceView3.setBackgroundResource(R.drawable.wrong_answer_back);
        }
        else if (questionModel.getUserAnswer().equalsIgnoreCase("D")) {
            choiceView4.setBackgroundResource(R.drawable.wrong_answer_back);
        }

        if (questionModel.getAnswer().equalsIgnoreCase("A")) {
            choiceView1.setBackgroundResource(R.drawable.selected_answer_back);
        }
        else if (questionModel.getAnswer().equalsIgnoreCase("B")) {
            choiceView2.setBackgroundResource(R.drawable.selected_answer_back);
        }
        else if (questionModel.getAnswer().equalsIgnoreCase("C")) {
            choiceView3.setBackgroundResource(R.drawable.selected_answer_back);
        }
        else if (questionModel.getAnswer().equalsIgnoreCase("D")) {
            choiceView4.setBackgroundResource(R.drawable.selected_answer_back);
        }
    }

    public void previousQuestionAndAnswers() {

        questionCompletedTV.setText((count+1)+ " of "+ totalQuestion);
        QuestionModel questionModel = questionModels.get(count);

        questionTV.setText(questionModel.getQuestion());
        choice1.setText(questionModel.getOptionA());
        choice2.setText(questionModel.getOptionB());
        choice3.setText(questionModel.getOptionC());
        choice4.setText(questionModel.getOptionD());

        if (questionModel.getQuestionImage()) {
            queImage.setVisibility(View.VISIBLE);
        }
        else {
            queImage.setVisibility(View.GONE);
        }
        setSelectedAnswer(questionModel);
    }

    public void clearSelection(){
        choiceView1.setBackgroundResource(R.drawable.unselected_answer_back);
        choiceView2.setBackgroundResource(R.drawable.unselected_answer_back);
        choiceView3.setBackgroundResource(R.drawable.unselected_answer_back);
        choiceView4.setBackgroundResource(R.drawable.unselected_answer_back);
    }


}
