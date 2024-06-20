package in.softment.exampractice.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class SolvedPaperModel implements Serializable {
    public String catId = "";
    public String subCatId = "";
    public ArrayList<QuestionModel> questionModels = new ArrayList<>();
    public Date quizCompletionDate = new Date();
    public String catName = "";
    public String subCatName = "";
    public int totalQuestion = 0;
    public int totalCorrect = 0;
    public int totalWrong =  0;
    public double percentage = 0;
    public boolean completed = false;


    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public ArrayList<QuestionModel> getQuestionModels() {
        return questionModels;
    }

    public void setQuestionModels(ArrayList<QuestionModel> questionModels) {
        this.questionModels = questionModels;
    }

    public Date getQuizCompletionDate() {
        return quizCompletionDate;
    }

    public void setQuizCompletionDate(Date quizCompletionDate) {
        this.quizCompletionDate = quizCompletionDate;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(int totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public int getTotalCorrect() {
        return totalCorrect;
    }

    public void setTotalCorrect(int totalCorrect) {
        this.totalCorrect = totalCorrect;
    }

    public int getTotalWrong() {
        return totalWrong;
    }

    public void setTotalWrong(int totalWrong) {
        this.totalWrong = totalWrong;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
