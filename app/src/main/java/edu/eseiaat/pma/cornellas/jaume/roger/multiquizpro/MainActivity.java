package edu.eseiaat.pma.cornellas.jaume.roger.multiquizpro;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int ids_answers[] = {
            R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4
    };
    private String[] all_questions;
    private TextView text_question;
    private Button btn_next,btn_prev;

    private int correct_answer;
    private int current_question;
    private boolean[] answer_is_correct;
    private int[] answer;



    private RadioGroup group;
    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("correct_answer", correct_answer);
        outState.putInt("current_question", current_question);
        outState.putBooleanArray("answer_is_correct", answer_is_correct);
        outState.putIntArray("answer",answer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_question = (TextView) findViewById(R.id.text_question);
        group = (RadioGroup) findViewById(R.id.answer_group);
        all_questions = getResources().getStringArray(R.array.all_questions);
        btn_next = (Button) findViewById(R.id.btn_check);
        btn_prev= (Button) findViewById(R.id.btn_prev);

        if (savedInstanceState==null){
            startOver();
        }else {
            Bundle state=savedInstanceState;
            correct_answer = state.getInt("correct_answer");
            current_question = state.getInt("current_question");
            answer_is_correct= state.getBooleanArray("answer_is_correct");
            answer= state.getIntArray("answer");
            showQuestion();
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();

                if (current_question<all_questions.length-1) {
                    current_question++;
                    showQuestion();
                }else{
                    checkResults();
                }
            }

        });
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                if (current_question>0){
                    current_question--;
                    showQuestion();
                }
            }
        });
    }

    private void startOver() {
        answer_is_correct= new boolean[all_questions.length];
        answer= new int[all_questions.length];
        for (int i=0; i<answer.length; i++){
            answer[i]=-1;
        }
        current_question = 0;
        showQuestion();
    }

    private void checkResults() {
        int correctas=0,incorrectas=0, nocontestadas=0;
        for(int i=0; i<all_questions.length; i++){
            if(answer_is_correct[i]) correctas++;
            else if(answer[i]==-1) nocontestadas++;
            else incorrectas++;
        }

        //TODO permitir traduccion de este texto:

        String message =
                String.format("Correctas: %d\nIncorrectas: %d\nNo contestadas: %d\n ",
                        correctas, incorrectas, nocontestadas);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.results);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.start_over, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //borrar resuestas y volver.
                startOver();
            }
        });
        builder.create().show();
    }

    private void checkAnswer() {
        int id = group.getCheckedRadioButtonId();
        int ans = -1;
        for (int i = 0; i < ids_answers.length; i++) {
            if (ids_answers[i] == id) {
                ans = i;
            }
        }
        answer_is_correct[current_question]= (ans==correct_answer);
        answer[current_question]= ans;
    }

    private void showQuestion() {
        String q= all_questions[current_question];
        String[] parts= q.split(";");

        group.clearCheck();

        text_question.setText(parts[0]);

        for (int i = 0; i < ids_answers.length; i++) {
            RadioButton rb = (RadioButton) findViewById(ids_answers[i]);
            String ans = parts[i+1];
            if ( ans.charAt(0) == '*'){
                correct_answer=i;
                ans = ans.substring(1);
            }
            rb.setText(ans);
            if (answer[current_question]==i){
                rb.setChecked(true);
            }
        }
        if (current_question==0){
            btn_prev.setVisibility(View.GONE);
        }else{
            btn_prev.setVisibility(View.VISIBLE);
        }
        if (current_question==all_questions.length-1){
            btn_next.setText(R.string.finish);
        }else{
            btn_next.setText(R.string.next);
        }
    }
}
