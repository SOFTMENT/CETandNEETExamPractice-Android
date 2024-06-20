package in.softment.exampractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.auth.api.Auth;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.softment.exampractice.Interface.CategoriesListener;
import in.softment.exampractice.Interface.QuestionsListener;
import in.softment.exampractice.Model.CategoryModel;
import in.softment.exampractice.Model.QuestionModel;
import in.softment.exampractice.Model.SolvedPaperModel;
import in.softment.exampractice.Model.SubcategoryModel;
import in.softment.exampractice.Util.ProgressHud;
import in.softment.exampractice.Util.Services;

public class QuestionAnswerActivity extends AppCompatActivity {
    TextView questionCompletedTV,questionTV,choice1,choice2,choice3,choice4;
    LinearLayout choiceView1,choiceView2,choiceView3,choiceView4;
    private int count = 0;
    private int totalQuestion = 1;
    ArrayList<QuestionModel> questionModels = new ArrayList<>();
    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout mainLL;
    List<String> myArray = new ArrayList<>();
    String catName;
    SubcategoryModel subcategoryModel;
    private RoundedImageView queImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_question_answer);

        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();

         mainLL = findViewById(R.id.mainLL);

         subcategoryModel = (SubcategoryModel) getIntent().getSerializableExtra("subcategory");
         catName = getIntent().getStringExtra("catName");


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

         choiceView1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 clearSelection();

                 choiceView1.setBackgroundResource(R.drawable.selected_answer_back);
             }
         });
        choiceView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearSelection();

                choiceView2.setBackgroundResource(R.drawable.selected_answer_back);
            }
        });
        choiceView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            clearSelection();

                choiceView3.setBackgroundResource(R.drawable.selected_answer_back);
            }
        });
        choiceView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearSelection();

                choiceView4.setBackgroundResource(R.drawable.selected_answer_back);
            }
        });

         totalQuestion = subcategoryModel.getTotalQuestions();

         findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showCancelDialog();
            }
         });



        CollectionReference collection = FirebaseFirestore.getInstance().collection("Categories").document(subcategoryModel.getCatId()).collection("Questions");
        AggregateQuery countQuery = collection.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                if (snapshot.getCount() > 0) {
                    myArray.addAll(Services.getRandomArray((int) snapshot.getCount(),subcategoryModel.getTotalQuestions()));

                    int loop = (myArray.size() % 10 == 0) ? (myArray.size() / 10) : ((myArray.size() / 10) + 1);

                    for (int i = 0; i < loop; i++) {
                        if (i == loop - 1) {
                            getQuestions(subcategoryModel.getCatId(), myArray.subList((i * 10), myArray.size()));
                        }
                        else {
                            getQuestions(subcategoryModel.getCatId(), myArray.subList((i * 10), ((i+1) * 10)));
                        }
                    }
                }
                else {
                    finish();
                }


            } else {
                Services.showDialog(QuestionAnswerActivity.this,"ERROR",task.getException().getLocalizedMessage());
            }
        });



        AppCompatButton nextBtn = findViewById(R.id.nextBtn);
        AppCompatButton previousBtn  = findViewById(R.id.previousBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(choiceView1.getBackground().getConstantState().equals(
                        ResourcesCompat.getDrawable(getResources(),R.drawable.selected_answer_back,null).getConstantState())) {
                        questionModels.get(count).setUserAnswer("A");
                }
                else if(choiceView2.getBackground().getConstantState().equals(
                        ResourcesCompat.getDrawable(getResources(),R.drawable.selected_answer_back,null).getConstantState())) {

                    questionModels.get(count).setUserAnswer("B");
                }
                else if(choiceView3.getBackground().getConstantState().equals(
                        ResourcesCompat.getDrawable(getResources(),R.drawable.selected_answer_back,null).getConstantState())) {
                    questionModels.get(count).setUserAnswer("C");

                }
                else if(choiceView4.getBackground().getConstantState().equals(
                        ResourcesCompat.getDrawable(getResources(),R.drawable.selected_answer_back,null).getConstantState())) {

                    questionModels.get(count).setUserAnswer("D");
                }

                if (!questionModels.get(count).getUserAnswer().isEmpty()) {

                     if (count >= questionModels.size() - 1) {

                         AlertDialog.Builder builder = new AlertDialog.Builder(QuestionAnswerActivity.this);
                         View view = getLayoutInflater().inflate(R.layout.my_custom_dialog, null);
                         TextView title = view.findViewById(R.id.title);
                         TextView message = view.findViewById(R.id.message);

                         title.setText("Submit");
                         message.setText("Are you sure you want to submit this paper?");
                         CardView yes = view.findViewById(R.id.ok);
                         CardView cancel = view.findViewById(R.id.cancel);


                         builder.setView(view);


                         AlertDialog alertDialog = builder.create();

                         yes.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                alertDialog.dismiss();
                                 submitSolvedPaper(questionModels, subcategoryModel.getCatId(), subcategoryModel.getId(), catName, subcategoryModel.getName());
                             }
                         });
                         cancel.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 alertDialog.dismiss();
                             }
                         });


                         alertDialog.show();

                     }
                     else {
                         count++;
                         previousBtn.setVisibility(View.VISIBLE);

                         if (count == questionModels.size() - 1) {
                             nextBtn.setText("Submit");

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

    }

    public void getQuestions(String catId, List<String> myList){
        Services.getAllQuestion(catId, myList, new QuestionsListener() {
            @Override
            public void onCallback(ArrayList<QuestionModel> qusModels, String error) {

                if (error == null) {

                    try {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        mainLL.setVisibility(View.VISIBLE);
                    }
                    catch (Error ignored) {

                    }

                    questionModels.addAll(qusModels);

                    if (count == 0) {
                        if (questionModels.size() > 0) {

                            submitUnSolvedPaper(questionModels, subcategoryModel.getCatId(),subcategoryModel.getId(),catName, subcategoryModel.getName(), subcategoryModel.getTotalQuestions());
                            nextQuestionAndAnswers();

                        }

                    }


                }
                else {

                    Services.showDialog(QuestionAnswerActivity.this,"ERROR",error);
                }
            }
        });
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
            choiceView1.setBackgroundResource(R.drawable.selected_answer_back);
        }
        else if (questionModel.getUserAnswer().equalsIgnoreCase("B")) {
            choiceView2.setBackgroundResource(R.drawable.selected_answer_back);
        }
        else if (questionModel.getUserAnswer().equalsIgnoreCase("C")) {
            choiceView3.setBackgroundResource(R.drawable.selected_answer_back);
        }
        else if (questionModel.getUserAnswer().equalsIgnoreCase("D")) {
            choiceView4.setBackgroundResource(R.drawable.selected_answer_back);
        }
    }

    public void previousQuestionAndAnswers(){
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

    public void showCancelDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionAnswerActivity.this);
        View view = getLayoutInflater().inflate(R.layout.my_custom_dialog, null);
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);

        title.setText("Exit");
        message.setText("Are you sure you want to exit?");
        CardView yes = view.findViewById(R.id.ok);
        CardView cancel = view.findViewById(R.id.cancel);


        builder.setView(view);


        AlertDialog alertDialog = builder.create();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();


                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        showCancelDialog();
    }


    public void submitUnSolvedPaper(ArrayList<QuestionModel> questionModels, String catId, String subCatId, String catName, String subCatName, int totalQuestion){
        SolvedPaperModel solvedPaperModel = new SolvedPaperModel();
        solvedPaperModel.catId = catId;
        solvedPaperModel.subCatId = subCatId;
        solvedPaperModel.questionModels = questionModels;
        solvedPaperModel.catName = catName;
        solvedPaperModel.totalQuestion = totalQuestion;
        solvedPaperModel.subCatName = subCatName;

        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("SolvedPapers")
                .document(subCatId).set(solvedPaperModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent output = new Intent();
                        output.putExtra("SUCCESS","true");
                        setResult(RESULT_OK, output);

                    }
                });
    }

    public void submitSolvedPaper(ArrayList<QuestionModel> questionModels, String catId, String subCatId, String catName, String subCatName){
        ProgressHud.show(this,"");
        SolvedPaperModel solvedPaperModel = new SolvedPaperModel();
        solvedPaperModel.catId = catId;
        solvedPaperModel.subCatId = subCatId;
        solvedPaperModel.questionModels = questionModels;
        solvedPaperModel.quizCompletionDate = new Date();
        solvedPaperModel.catName = catName;
        solvedPaperModel.subCatName = subCatName;
        solvedPaperModel.completed = true;
        int totalCorrectAnswer = 0;
        for (QuestionModel questionModel : questionModels) {
            if (questionModel.getAnswer().equalsIgnoreCase(questionModel.getUserAnswer())) {
                totalCorrectAnswer++;
            }
        }
        solvedPaperModel.totalQuestion = questionModels.size();
        solvedPaperModel.totalCorrect = totalCorrectAnswer;
        solvedPaperModel.totalWrong = questionModels.size() - totalCorrectAnswer;
        solvedPaperModel.percentage = ((totalCorrectAnswer * 1.0) * 100.0) / questionModels.size();

        FirebaseFirestore.getInstance().collection("Users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("SolvedPapers")
                .document(subCatId).set(solvedPaperModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                     if (task.isSuccessful()) {
                            Intent intent = new Intent(QuestionAnswerActivity.this, ResultActivity.class);
                            intent.putExtra("solvedModel",solvedPaperModel);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                     }
                     else {
                         Services.showDialog(QuestionAnswerActivity.this,"ERROR",task.getException().toString());
                     }
            }
        });
    }
}
