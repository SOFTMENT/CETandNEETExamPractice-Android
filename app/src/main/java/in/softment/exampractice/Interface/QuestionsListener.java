package in.softment.exampractice.Interface;

import java.util.ArrayList;

import in.softment.exampractice.Model.QuestionModel;

public interface QuestionsListener {
  void onCallback(ArrayList<QuestionModel>  qusModels, String error);
}
