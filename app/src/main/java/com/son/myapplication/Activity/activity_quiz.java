package com.son.myapplication.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.son.myapplication.Database.CDatabase;
import com.son.myapplication.Database.Question;
import com.son.myapplication.Database.QuizContract;
import com.son.myapplication.R;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class activity_quiz extends AppCompatActivity {
    private final static long INTERVAL = 1000;
    private final static long TIMEOUT = 15000;
    private MediaPlayer mediaPlayer;
    Animation uptodown,downtoup;
    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private ImageView Question;
    private TextView Score;
    private TextView help;
    private TextView CountDown;
    private RadioGroup rbGroup;
    private RadioButton a;
    private RadioButton b;
    private RadioButton c;
    private RadioButton d;
    private Button Next;
    private int highScore=0;
    private com.son.myapplication.Database.Question question;
    CountDownTimer mCountDown;
    private List<Question> questionList;
    SQLiteDatabase database;
    int pos = 0;
    int grade = 0;
    Random rd=new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Question = findViewById(R.id.txtQuestion);
        Score = findViewById(R.id.txtViewScore);
        help = findViewById(R.id.txtHelp);
        rbGroup = findViewById(R.id.RG);
        a = findViewById(R.id.rb1);
        b = findViewById(R.id.rb2);
        c = findViewById(R.id.rb3);
        d = findViewById(R.id.rb4);
        Next = findViewById(R.id.btnNext);
        uptodown= AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup=AnimationUtils.loadAnimation(this,R.anim.downtoup);
        Question.setAnimation(uptodown);
        rbGroup.setAnimation(uptodown);
        Next.setAnimation(downtoup);
        int songId = this.getRawResIdByName("myfile");
        this.mediaPlayer=   MediaPlayer.create(this, songId);
        this.mediaPlayer.start();
        //Truyen du lieu tu class Level sang de goi len che do Easy hoac Medium
        Intent callerintent = getIntent();
        Bundle packagefromcaller = callerintent.getBundleExtra("Level");
        final int mode = packagefromcaller.getInt("mode");
            questionList = getQuestions1(mode);
        Display();
        this.mediaPlayer.pause();
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Checkanswer();


                if (pos >= questionList.size()) {
                    Intent intent = new Intent(activity_quiz.this, activity_result.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("score", grade);
                    bundle.putInt("position", pos);
                    intent.putExtra("MyPackage", bundle);
                    startActivity(intent);
                    if(grade>highScore)
                    {
                        highScore=grade;
                        if(mode ==1){
                            saveHighScoreEasy(highScore);
                        }
                        if(mode ==2)
                        {
                            saveHighScoreMedium(highScore);
                        }
                        if(mode ==3)
                        {
                            saveHighScoreHard(highScore);
                        }

                    }
                    finish();
                }
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper();
            }
        });
    }

    private int getRawResIdByName(String myfile) {
        String pkgName = this.getPackageName();
        int resID = this.getResources().getIdentifier(myfile, "raw", pkgName);
        return resID;
    }
    public void Checkanswer()
    {
        a.setEnabled(true);
        b.setEnabled(true);
        c.setEnabled(true);
        d.setEnabled(true);
        //mCountDown.start();
        int idCheck = rbGroup.getCheckedRadioButtonId();
        switch (idCheck) {
            case R.id.rb1: {
                //Nếu đáp án là câu A thì cộng kq lên 1
                if (question.getA().compareTo(question.getAnswerNr()) == 0)
                    grade = grade + 1;
                break;
            }
            case R.id.rb2: {

                //Nếu đáp án là câu B thì cộng kq lên 1
                if (question.getB().compareTo(question.getAnswerNr()) == 0)
                    grade = grade + 1;
                break;
            }
            case R.id.rb3: {
                //Nếu đáp án là câu C thì cộng kq lên 1
                if (question.getC().compareTo(question.getAnswerNr()) == 0)
                    grade = grade + 1;
                break;
            }
            case R.id.rb4: {
                //Nếu đáp án là câu D thì cộng kq lên 1
                if (question.getD().compareTo(question.getAnswerNr()) == 0)
                    grade = grade + 1;
                break;
            }
            default: {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()) {
                            new AlertDialog.Builder(activity_quiz.this)
                                    .setTitle("Message")
                                    .setMessage("Please check your answer")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).show();
                        }
                    }
                });
                pos--;

            }

        }
        mCountDown.start();
        Display();
    }
    public void checkAnswerCountDown(){
            a.setEnabled(true);
            b.setEnabled(true);
            c.setEnabled(true);
            d.setEnabled(true);
            //mCountDown.start();
            int idCheck = rbGroup.getCheckedRadioButtonId();
            switch (idCheck) {
                case R.id.rb1: {
                    //Nếu đáp án là câu A thì cộng kq lên 1
                    if (question.getA().compareTo(question.getAnswerNr()) == 0)
                        grade = grade + 1;
                    break;
                }
                case R.id.rb2: {

                    //Nếu đáp án là câu B thì cộng kq lên 1
                    if (question.getB().compareTo(question.getAnswerNr()) == 0)
                        grade = grade + 1;
                    break;
                }
                case R.id.rb3: {
                    //Nếu đáp án là câu C thì cộng kq lên 1
                    if (question.getC().compareTo(question.getAnswerNr()) == 0)
                        grade = grade + 1;
                    break;
                }
                case R.id.rb4: {
                    //Nếu đáp án là câu D thì cộng kq lên 1
                    if (question.getD().compareTo(question.getAnswerNr()) == 0)
                        grade = grade + 1;
                    break;
                }
        }
    }
    public void Display() {

        question = questionList.get(pos);
        a.setText(question.getA());
        b.setText(question.getB());
        c.setText(question.getC());
        d.setText(question.getD());
        Bitmap bmHinhDaiDien = BitmapFactory.decodeByteArray(question.getImage(), 0, question.getImage().length);
        Question.setImageBitmap(bmHinhDaiDien);
        pos++;
        Score.setText("Score" + grade);
        //mCountDown.start();
        rbGroup.clearCheck();

    }
    void saveHighScoreEasy(int highScore)
    {
        SharedPreferences sharedPreferences=getSharedPreferences("MyData1",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("H",highScore);
        editor.apply();

    }
    void saveHighScoreMedium(int highScore)
    {
        SharedPreferences sharedPreferences=getSharedPreferences("MyData2",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("H",highScore);
        editor.apply();

    }
    void saveHighScoreHard(int highScore)
    {
        SharedPreferences sharedPreferences=getSharedPreferences("MyData3",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("H",highScore);
        editor.apply();

    }
    protected void onResume() {
        super.onResume();
        CountDown = findViewById(R.id.txtTime);
        mCountDown = new CountDownTimer(TIMEOUT, INTERVAL)
        {
            @Override
            public void onTick(long millisUntilFinished) {
                //Display();
                CountDown.setText("0:"+millisUntilFinished/INTERVAL);
                if(millisUntilFinished<10000)
                {
                    //CountDown.setTextColor(Color.RED);
                    CountDown.setText("0:0"+millisUntilFinished/INTERVAL);
                }
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                checkAnswerCountDown();
                CountDown.setText("Hết giờ");
                Display();
                mCountDown.start();
            }

        }
                .start();
    }


    public List<Question> getQuestions1(int mode) {
        List<Question> questionList = new ArrayList<>();;
        database = CDatabase.initDatabase(this, DATABASE_NAME);
        Cursor c = database.rawQuery("SELECT * FROM " + QuizContract.QuestionsTable.TABLE_NAME + " WHERE mode= " + mode + " ORDER BY Random() ", null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                //question.setQuestion(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_QUESTION)));
                question.setImage(c.getBlob(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_QUESTION)));
                question.setA(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_A)));
                question.setB(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_B)));
                question.setC(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_C)));
                question.setD(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_D)));
                question.setAnswerNr(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_ANSWER_NR)));
                question.setMode(c.getInt(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_MODE)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
    public void Helper()
    {
        if(grade<5) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (!isFinishing()) {
                        new AlertDialog.Builder(activity_quiz.this)
                                .setTitle("Message")
                                .setMessage("You need 5 score to help")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                    }
                }
            });
        }
        else {
            checkHelper();


            grade = grade - 5;
        }
        Score.setText("Score: " + grade);

        }
    public void checkHelper() {
        RadioButton quizAnswer[] = {a, b, c, d};
        if(question.getA().compareTo(question.getAnswerNr()) != 0)
            a.setEnabled(false);
        if(question.getB().compareTo(question.getAnswerNr()) != 0)
            b.setEnabled(false);
        if(question.getC().compareTo(question.getAnswerNr()) != 0)
            c.setEnabled(false);
        if(question.getD().compareTo(question.getAnswerNr()) != 0)
            d.setEnabled(false);

    }
    }
