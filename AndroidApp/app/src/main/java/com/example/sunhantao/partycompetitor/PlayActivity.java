package com.example.sunhantao.partycompetitor;

import android.animation.ValueAnimator;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public class PlayActivity extends AppCompatActivity {
    Typeface typeface;
    Typeface typeface2;
    Typeface typeface3;
    CardView cv1;
    CardView cv2;
    RelativeLayout ll;
    TextView score1;
    int sc1;
    TextView cot;
    TextView score2;
    int sc2;
    TextView nameView1;
    TextView nameView2;
    int phase;
    TextView questionIndex;
    ProgressBar progress1;
    ProgressBar progress2;
    ProgressBar progress3;
    ImageView phasePic2;
    ImageView phasePic3;
    ImageView phasePic4;
    TextView phase1Intro;
    TextView phase2Intro;
    TextView phase3Intro;
    TextView phase4Intro;
    CardView card1;
    CardView card2;
    CardView card3;
    CardView card4;
    TextView questionTime;
    TextView vieTime;
    TextView answerTime;
    TextView vieCount;
    TextView answerCount;
    TextView questionResult;
    TextView timer1;
    int vieTimer;
    int answerTimer;
    ValueAnimator progressAnimator;
    ValueAnimator picAnimator;
    ValueAnimator posAnimator;
    Button test;
    ImageButton correctButton;
    ImageButton wrongButton;
    Button nextQuestion;
    String name1;
    String name2;
    int numberOfQuestions;
    int amountOfQuestions;
    int answering;
    ValueAnimator vieCountAnimater;
    ValueAnimator answerCountAnimator;
    boolean answer;
    boolean correct;
    BluetoothSocket mBlueToothSocket;
    OutputStream outStream;
    InputStream inStream;
    AlertDialog.Builder vierDialog;
    private Handler handler;
    MediaPlayer countDownPlayer;
    MediaPlayer startPlayer;
    MediaPlayer timePlayer;
    MediaPlayer correctPlayer;
    MediaPlayer wrongPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        countDownPlayer = new MediaPlayer();
        timePlayer = new MediaPlayer();
        startPlayer = new MediaPlayer();
        correctPlayer = new MediaPlayer();
        wrongPlayer = new MediaPlayer();
        countDownPlayer = MediaPlayer.create(this,R.raw.countdown);
        countDownPlayer.setLooping(false);
        timePlayer = MediaPlayer.create(this, R.raw.time);
        timePlayer.setLooping(true);
        startPlayer = MediaPlayer.create(this,R.raw.start);
        startPlayer.setLooping(false);
        correctPlayer = MediaPlayer.create(this,R.raw.correct);
        correctPlayer.setLooping(false);
        wrongPlayer = MediaPlayer.create(this,R.raw.wrong);
        wrongPlayer.setLooping(false);
        mBlueToothSocket = ((GlobalBlueSocket) getApplication()).getGlobalBlueSocket();
        try {
            outStream = mBlueToothSocket.getOutputStream();
            inStream = mBlueToothSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler = new Handler();
        typeface = Typeface.createFromAsset(getAssets(), "UnidreamLED.ttf");
        typeface2 = Typeface.createFromAsset(getAssets(), "Omega.ttf");
        typeface3 = Typeface.createFromAsset(getAssets(), "Chalkduster.ttf");
        cv1 = findViewById(R.id.cardView);
        cv2 = findViewById(R.id.cardView2);
        ll = findViewById(R.id.linearLayout2);
        cv1.setAlpha(0);
        cv2.setAlpha(0);
        ll.setAlpha(0);
        Intent lastIntent = getIntent();
        name1 = lastIntent.getStringExtra("name1");
        name2 = lastIntent.getStringExtra("name2");
        amountOfQuestions = lastIntent.getIntExtra("number", 5);
        numberOfQuestions = amountOfQuestions - 1;
        answering = 1;
        sc1 = 0;
        sc2 = 0;
        score1 = findViewById(R.id.score1);
        cot = findViewById(R.id.cot);
        score2 = findViewById(R.id.score2);
        nameView1 = findViewById(R.id.name1);
        nameView1.setText(getString(R.string.player1, name1));
        nameView1.setTypeface(typeface);
        nameView2 = findViewById(R.id.name2);
        nameView2.setTypeface(typeface);
        nameView2.setText(getString(R.string.player2, name2));
        score1.setTypeface(typeface);
        cot.setTypeface(typeface);
        score2.setTypeface(typeface);
        questionIndex = findViewById(R.id.textView3);
        questionIndex.setTypeface(typeface3);
        progress1 = findViewById(R.id.progressBar);
        progress2 = findViewById(R.id.progressBar2);
        progress3 = findViewById(R.id.progressBar3);
        phasePic2 = findViewById(R.id.imageView2);
        phase1Intro = findViewById(R.id.phase1Intro);
        phase2Intro = findViewById(R.id.phase2Intro);
        phase3Intro = findViewById(R.id.phase3Intro);
        phase4Intro = findViewById(R.id.phase4Intro);
        phase1Intro.setTypeface(typeface3);
        phase2Intro.setTypeface(typeface3);
        phase3Intro.setTypeface(typeface3);
        phase4Intro.setTypeface(typeface3);
        phasePic3 = findViewById(R.id.imageView3);
        phasePic4 = findViewById(R.id.imageView4);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        questionTime = findViewById(R.id.textView4);
        questionTime.setTypeface(typeface2);
        vieTime = findViewById(R.id.textView5);
        vieTime.setTypeface(typeface2);
        vieCount = findViewById(R.id.vieCount);
        vieCount.setTypeface(typeface2);
        answerTime = findViewById(R.id.textView6);
        answerTime.setTypeface(typeface2);
        answerCount = findViewById(R.id.answerCount);
        answerCount.setTypeface(typeface2);
        questionResult = findViewById(R.id.question_result);
        questionResult.setTypeface(typeface2);
        phase = 0;
        vieTimer = 9;
        answerTimer = 29;
        timer1 = findViewById(R.id.timer1);
        timer1.bringToFront();
        timer1.setTypeface(typeface2);
        answer = false;
        correct = false;
        showCounter();
        progressAnimator = ValueAnimator.ofInt(0, 100);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (Integer) animation.getAnimatedValue();
                switch (phase) {
                    case 1:
                        progress1.setProgress(currentValue);
                        progress1.requestLayout();
                        break;
                    case 2:
                        progress2.setProgress(currentValue);
                        progress2.requestLayout();
                        break;
                    case 3:
                        progress3.setProgress(currentValue);
                        progress3.requestLayout();
                        break;
                    case 4:
                        progress1.setProgress(100 - currentValue);
                        progress1.requestLayout();
                        progress2.setProgress(100 - currentValue);
                        progress2.requestLayout();
                        if (progress3.getProgress() != 0) {
                            progress3.setProgress(100 - currentValue);
                            progress3.requestLayout();
                        }
                        break;
                }
                if (currentValue == 100 && phase != 4) {
                    picAnimator.start();
                }
            }
        });
        picAnimator = ValueAnimator.ofFloat(0, 1.0f);
        picAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                switch (phase) {
                    case 1:
                        phasePic2.setAlpha(currentValue);
                        phasePic2.requestLayout();
                        phase2Intro.setAlpha(currentValue);
                        phase2Intro.requestLayout();
                        break;
                    case 2:
                        phasePic3.setAlpha(currentValue);
                        phasePic3.requestLayout();
                        phase3Intro.setAlpha(currentValue);
                        phase3Intro.requestLayout();
                        break;
                    case 3:
                        phasePic4.setAlpha(currentValue);
                        phasePic4.requestLayout();
                        phase4Intro.setAlpha(currentValue);
                        phase4Intro.requestLayout();
                        break;
                    case 4:
                        phasePic2.setAlpha(1.0f - currentValue);
                        phasePic2.requestLayout();
                        phase2Intro.setAlpha(1.0f - currentValue);
                        phase2Intro.requestLayout();
                        if (phase3Intro.getAlpha() > 0) {
                            phasePic3.setAlpha(currentValue);
                            phasePic3.requestLayout();
                            phase3Intro.setAlpha(currentValue);
                            phase3Intro.requestLayout();
                        }
                        phasePic4.setAlpha(1.0f - currentValue);
                        phasePic4.requestLayout();
                        phase4Intro.setAlpha(1.0f - currentValue);
                        phase4Intro.requestLayout();
                        break;
                }
                if (phase == 4 && currentValue == 1.0f) {
                    progressAnimator.start();
                }
            }
        });
        posAnimator = ValueAnimator.ofFloat(1, 0);
        posAnimator.setDuration(1000);
        posAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                switch (phase) {
                    case 1:
                        card1.setAlpha(currentValue);
                        card1.requestLayout();
                        card2.setAlpha(1 - currentValue);
                        card2.requestLayout();
                        if (currentValue == 0) {
                            vieTimer = 9;
                            showVieCount();
                        }
                        break;
                    case 2:
                        card2.setAlpha(currentValue);
                        card2.requestLayout();
                        card3.setAlpha(1 - currentValue);
                        card3.requestLayout();
                        if (currentValue == 0) {
                            answerTimer = 29;
                            showAnswerCount();
                        }
                        break;
                    case 3:
                        if (!answer) {
                            card2.setAlpha(currentValue);
                            card2.requestLayout();
                        } else {
                            card3.setAlpha(currentValue);
                            card3.requestLayout();
                            if (currentValue == 0) {
                                refreshScore();
                            }
                        }
                        if (currentValue == 1 && numberOfQuestions > 0) {
                            nextQuestion.setVisibility(View.VISIBLE);
                        } else if (currentValue == 1) {
                            nextQuestion.setVisibility(View.GONE);
                        }
                        if (currentValue == 0) {
                            nextQuestion.setEnabled(true);
                        }
                        card4.setAlpha(1 - currentValue);
                        card4.requestLayout();
                        break;
                    case 4:
                        card4.setAlpha(currentValue);
                        card4.requestLayout();
                        card1.setAlpha(1 - currentValue);
                        card1.requestLayout();
                        if (currentValue == 0) {
                            test.setEnabled(true);
                        }
                }
            }
        });
        test = findViewById(R.id.button3);
        test.setEnabled(false);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phase = 1;
                nextPhase();
                test.setEnabled(false);
                answer = false;
            }
        });
        correctButton = findViewById(R.id.correctButton);
        correctButton.setEnabled(false);
        wrongButton = findViewById(R.id.wrongButton);
        wrongButton.setEnabled(false);
        correctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correctPlayer.seekTo(0);
                correctPlayer.start();
                answerCountAnimator.cancel();
                phase = 3;
                correct = true;
                nextPhase();
                correctButton.setEnabled(false);
                wrongButton.setEnabled(false);
                if(answering == 1){
                    questionResult.setText(getString(R.string.question_result_right,name1));
                }else{
                    questionResult.setText(getString(R.string.question_result_right,name2));
                }
                sendG("y");
            }
        });
        wrongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wrongPlayer.seekTo(0);
                wrongPlayer.start();
                answerCountAnimator.cancel();
                phase = 3;
                correct = false;
                nextPhase();
                correctButton.setEnabled(false);
                wrongButton.setEnabled(false);
                if(answering == 1){
                    questionResult.setText(getString(R.string.question_result_wrong,name1,name2));
                }else{
                    questionResult.setText(getString(R.string.question_result_wrong,name2,name1));
                }
                sendG("n");
            }
        });
        nextQuestion = findViewById(R.id.next);
        nextQuestion.setEnabled(false);
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phase = 4;
                nextPhase();
                answer = false;
                correct = false;
                vieCount.setText("3");
                vieCount.setTypeface(typeface2);
                answerCount.setText("3");
                answerCount.setTypeface(typeface2);
                numberOfQuestions--;
                questionIndex.setText("Question " + (amountOfQuestions - numberOfQuestions));
                sendG("1");
                receiveThread = new Thread() {
                    public void run() {
                        byte[] rsp;
                        while (true) {
                            try {
                                rsp = new byte[inStream.available()];

                                answering = inStream.read(rsp);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (answering > 0) {
                                break;
                            }
                        }
                        showVier();
                    }
                };
            }
        });
    }

    private String getNextTimer(String ct) {
        switch (ct) {
            case "3":
                return "2";
            case "2":
                return "1";
            case "1":
                return "GO";
            case "GO":
                return "DONE";
        }
        return null;
    }

    private void showCounter() {
        final float[] lastValue = {1.0f};
        final ValueAnimator scaleAnimater = ValueAnimator.ofFloat(1, 0.5f);
        scaleAnimater.setDuration(1000);
        scaleAnimater.setRepeatCount(3);
        scaleAnimater.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float currentValue = (float) animation.getAnimatedValue();
                if(currentValue == 1.0f){
                    countDownPlayer.seekTo(0);
                    countDownPlayer.start();
                }
                timer1.setAlpha(1.5f - currentValue);
                timer1.setScaleX(currentValue);
                timer1.setScaleY(currentValue);
                if (timer1.getText().toString().equals("GO")) {
                    cv1.setAlpha(1.5f - currentValue);
                    cv2.setAlpha(1.5f - currentValue);
                    ll.setAlpha(1.5f - currentValue);
                }
                if (currentValue > lastValue[0]) {
                    if (!timer1.getText().toString().equals("1")) {
                        countDownPlayer.seekTo(0);
                        countDownPlayer.start();
                    } else{
                        startPlayer.seekTo(0);
                        startPlayer.start();
                    }
                    timer1.setText(getNextTimer(timer1.getText().toString()));
                }
                if (currentValue == 0.5f) {
                    timer1.setVisibility(View.GONE);
                    test.setEnabled(true);
                }
                lastValue[0] = currentValue;
            }
        });
        sendG("g");
        scaleAnimater.start();
    }

    void showVieCount() {
        final float[] lastValue = {1.0f};
        vieCountAnimater = ValueAnimator.ofFloat(1, 0);
        vieCountAnimater.setDuration(1000);
        vieCountAnimater.setRepeatCount(13);
        vieCountAnimater.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                if(currentValue == 1.0f){
                    countDownPlayer.seekTo(0);
                    countDownPlayer.start();
                }
                int check = vieTimer + 1;
                if (!vieCount.getText().toString().equals("" + check)) {
                    vieCount.setScaleX(currentValue);
                    vieCount.setScaleY(currentValue);
                }
                if (currentValue > lastValue[0]) {
                    if (!vieCount.getText().toString().equals("GO") && vieTimer == 9) {
                        countDownPlayer.seekTo(0);
                        countDownPlayer.start();
                        vieCount.setText(getNextTimer(vieCount.getText().toString()));
                    } else if(vieCount.getText().toString().equals("1") && vieTimer == 9){
                        startPlayer.seekTo(0);
                        startPlayer.start();
                    }
                    else {
                        if (vieCount.getText().toString().equals("GO")) {
                            timePlayer.start();
                            vieCount.setTypeface(typeface);
                            sendG("g");
                            receiveThread.start();
                        }
                        vieCount.setText(vieTimer + "");
                        vieTimer--;
                    }
                }
                if (currentValue == 0 && !answer) {
                    timePlayer.pause();
                    phase = 3;
                    answer = false;
                    questionResult.setText(R.string.question_result_draw);
                    receiveThread.interrupt();
                    nextPhase();
                    sendG("g");

                }
                lastValue[0] = currentValue;
            }
        });
        vieCountAnimater.start();
    }

    void showAnswerCount() {
        final float[] lastValue = {1.0f};
        answerCountAnimator = ValueAnimator.ofFloat(1, 0);
        answerCountAnimator.setDuration(1000);
        answerCountAnimator.setRepeatCount(33);
        answerCountAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                if(currentValue == 1.0f){
                    countDownPlayer.seekTo(0);
                    countDownPlayer.start();
                }
                int check = answerTimer + 1;
                if (!answerCount.getText().toString().equals("" + check)) {
                    answerCount.setScaleX(currentValue);
                    answerCount.setScaleY(currentValue);
                }
                if (currentValue > lastValue[0]) {
                    if (!answerCount.getText().toString().equals("GO") && answerTimer == 29) {
                        countDownPlayer.seekTo(0);
                        countDownPlayer.start();
                        answerCount.setText(getNextTimer(answerCount.getText().toString()));
                    }else {
                        if (answerCount.getText().toString().equals("GO")) {
                            timePlayer.start();
                            answerCount.setTypeface(typeface);
                            correctButton.setEnabled(true);
                            wrongButton.setEnabled(true);
                        }
                        answerCount.setText(answerTimer + "");
                        answerTimer--;
                    }
                }
                if (currentValue == 0 ) {
                    timePlayer.pause();
                    phase = 3;
                    sendG("n");
                    if (answering == 1)
                        questionResult.setText(getString(R.string.question_result_no, name1, name2));
                    else
                        questionResult.setText(getString(R.string.question_result_no, name2, name1));
                    nextPhase();
                }
                lastValue[0] = currentValue;
            }
        });
        answerCountAnimator.start();
    }

    void nextPhase() {
        if(timePlayer.isPlaying())
            timePlayer.pause();
        if(phase == 3){
            if(answerCountAnimator != null)
                answerCountAnimator.cancel();
        }
        posAnimator.start();
        if(phase != 4) {
            progressAnimator.start();
        } else{
            picAnimator.start();
        }
    }

    void refreshScore() {
        final float[] lastValue = {1.0f};
        ValueAnimator scoreAnimator = ValueAnimator.ofFloat(1, 0, 1);
        scoreAnimator.setDuration(2000);
        scoreAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                if ((answering == 1 && correct) || (answering == 2 && !correct)) {
                    score1.setAlpha(currentValue);
                } else if ((answering == 2 && correct) || (answering == 1 && !correct)) {
                    score2.setAlpha(currentValue);
                }
                if (currentValue >= lastValue[0] && currentValue != 1) {
                    if ((answering == 1 && correct) || (answering == 2 && !correct)) {
                        sc1++;
                        if (sc1 < 10)
                            score1.setText("0" + sc1);
                        else
                            score1.setText("" + sc1);
                    } else if ((answering == 2 && correct) || (answering == 1 && !correct)) {
                        sc2++;
                        if (sc2 < 10)
                            score2.setText("0" + sc2);
                        else
                            score2.setText("" + sc2);
                    }
                    lastValue[0] = 2;
                } else if(lastValue[0] != 2){
                    lastValue[0] = currentValue;
                }
                if (currentValue == 1.0f && lastValue[0] != 1.0f && numberOfQuestions == 0) {
                    showWinner();
                }
            }
        });
        scoreAnimator.start();
    }

    void showWinner() {

        final AlertDialog.Builder winnerDialog =
                new AlertDialog.Builder(PlayActivity.this);
        winnerDialog.setTitle("Game Over");
        if (sc1 > sc2)
            winnerDialog.setMessage(name1 + " win!");
        else if (sc1 == sc2)
            winnerDialog.setMessage("Draw!");
        else
            winnerDialog.setMessage(name2 + " win!");
        winnerDialog.setPositiveButton("Play Again",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendG("0");
                        Intent intent = new Intent(PlayActivity.this, SignActivity.class);
                        intent.putExtra("name1", name1);
                        intent.putExtra("name2", name2);
                        intent.putExtra("number", amountOfQuestions);
                        startActivity(intent);
                        finish();
                    }
                });
        winnerDialog.setNeutralButton("Exit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        winnerDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        winnerDialog.show();
    }

    void sendG(final String inf) {
        new Thread() {
            public void run() {
                try {
                    outStream.write(inf.getBytes());
                    outStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    Thread receiveThread = new Thread() {
        public void run() {
            byte[] rsp;
            while (true) {
                try {
                    rsp = new byte[inStream.available()];

                    answering = inStream.read(rsp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (answering > 0) {
                    break;
                }
            }
            handler.post(animaterRun);
            showVier();
        }
    };

    void showVier(){
        vierDialog = new AlertDialog.Builder(PlayActivity.this);
        vierDialog.setTitle("Who is faster?");
        if (answering == 1)
            vierDialog.setMessage(name1 + " get the answer right!");
        else if (answering == 2)
            vierDialog.setMessage(name2 + " get the answer right!");
        vierDialog.setPositiveButton("Ask for Answer!",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vieCountAnimater.cancel();
                        sendG("v");
                        answer = true;
                        phase = 2;
                        handler.post(runnableUi);
                    }
                });
        new Thread("hah") {public void run() {Looper.prepare();vierDialog.show();Looper.loop();};}.start();
    }

    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {
            nextPhase();
        }

    };

    Runnable animaterRun = new Runnable() {
        @Override
        public void run() {
            vieCountAnimater.cancel();
            timePlayer.pause();
        }
    };
}
