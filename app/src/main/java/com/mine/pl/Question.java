package com.mine.pl;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Question implements Parcelable {
    private String question;
    private List<String> options;

    @SerializedName("correct_answer")
    private String correctAnswer;

    private String userAnswer;

    public Question() {}

    public String getQuestion()     { return question; }
    public List<String> getOptions(){ return options;  }
    public String getCorrectAnswer(){ return correctAnswer; }
    public String getUserAnswer()   { return userAnswer;   }

    public void setUserAnswer(String userAnswer)       { this.userAnswer = userAnswer;   }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    protected Question(Parcel in) {
        question      = in.readString();
        options       = in.createStringArrayList();
        correctAnswer = in.readString();
        userAnswer    = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeStringList(options);
        dest.writeString(correctAnswer);
        dest.writeString(userAnswer);
    }

    @Override
    public int describeContents() { return 0; }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override public Question createFromParcel(Parcel in) { return new Question(in); }
        @Override public Question[] newArray(int size)        { return new Question[size]; }
    };
}
