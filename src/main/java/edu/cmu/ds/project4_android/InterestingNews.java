package edu.cmu.ds.project4_android;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

public class InterestingNews extends AppCompatActivity {

    /**
     * @author Sheryl Hsiung.
     * The click listener will need a reference to this object,
     * so that upon successfully finding news from the server.
     * The "this" of the OnClick will be the OnClickListener, not
     * this InterestingNews.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final InterestingNews ma = this;
        /**
         * Find the "submit" button, and add a listener to it
         */
        final Button submitButton = findViewById(R.id.submit);
        final Switch switcher = findViewById(R.id.switch1);

        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                RadioButton other = findViewById(R.id.radioButton);
                RadioButton cmu = findViewById(R.id.radioButton2);
                submitButton.setBackgroundColor(Color.CYAN);

                String inputAmount = ((EditText) findViewById(R.id.searchTerm)).getText().toString();
                try {
                    int amt = Integer.parseInt(inputAmount);
                    GetNews gp = new GetNews();
                    if (cmu.isChecked()) {
                        gp.search("", ma, amt, cmu.getText().toString());
                        // Done asynchronously in another thread
                    } else {
                        gp.search("", ma, amt, other.getText().toString());
                    }
                    // It calls ip.newsReady() in this thread when complete.
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        });
/**
 * this is the switch for night view.
 * It changes color of the views.
 */
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //ImageView pictureView = findViewById(R.id.InterestingNews);
                LinearLayout layout = findViewById(R.id.motherLayout);
                TextView hello = findViewById(R.id.hello);
                TextView feedback = findViewById(R.id.textView);
                TextView schoolAsk = findViewById(R.id.textView2);
                RadioButton techButton = findViewById(R.id.radioButton);
                RadioButton bitCnButton = findViewById(R.id.radioButton2);
                if (isChecked) {
                    feedback.setVisibility(View.VISIBLE);
                    feedback.setTextColor(Color.WHITE);
                    layout.setBackgroundColor(Color.BLACK);
                    switcher.setTextColor(Color.WHITE);
                    hello.setTextColor(Color.CYAN);
                    techButton.setTextColor(Color.WHITE);
                    bitCnButton.setTextColor(Color.WHITE);
                    schoolAsk.setTextColor(Color.WHITE);
                } else {
                    feedback.setVisibility(View.VISIBLE);
                    feedback.setTextColor(Color.GRAY);
                    layout.setBackgroundColor(Color.WHITE);
                    switcher.setTextColor(Color.BLACK);
                    hello.setTextColor(Color.BLUE);
                    techButton.setTextColor(Color.BLACK);
                    bitCnButton.setTextColor(Color.BLACK);
                    schoolAsk.setTextColor(Color.BLACK);
                }
            }
        });
    }


    /**
     * This is called by the GetNews's onPostExecute method when the news is ready.
     * This allows for passing back the StringBuilder string for updating the news on textview.
     */
    public void newsReady(StringBuilder string) {
        //ImageView pictureView = (ImageView) findViewById(R.id.InterestingNews);
        TextView searchView = (EditText) findViewById(R.id.searchTerm);
        Button submitButton = findViewById(R.id.submit);
        submitButton.setBackgroundColor(Color.GRAY);
        TextView feedback = findViewById(R.id.textView);
        if (string != null) {
            feedback.setText("");
            feedback.setVisibility(View.VISIBLE);
            feedback.setText(string);
        } else {
            feedback.setVisibility(View.VISIBLE);
            feedback.setText("Oh no!");
        }
        searchView.setText("");
    }


}
