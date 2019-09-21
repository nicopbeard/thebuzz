package edu.lehigh.cse216.phase0;

import android.widget.Button;

public class ItemListAdapterHelper {
    public static void incrementButtonCount(Button button){
        int currCnt = Integer.parseInt(button.getText().toString());
        button.setText(Integer.toString(currCnt + 1));
    }
    public static void decrementButtonCount(Button button){
        int currCnt = Integer.parseInt(button.getText().toString());
        button.setText(Integer.toString(currCnt + 1));
    }
}
