package in.softment.exampractice.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class SubcategoryModel implements Serializable {

    public String name = "";
    public String id = "";
    public String catId = "";
    public int totalQuestions = 0;

    public SolvedPaperModel solvedPaperModel;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }


}
