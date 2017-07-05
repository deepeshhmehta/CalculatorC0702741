package com.example.deepesh.calculatorc0702741;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//make the MainActivity class implement onClickListener
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    boolean isSolved = false; //eqlauto pressed flag
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign onclick listener for all buttons
        Button button;
        for (int i=1; i<=19; i++) {
            //get the int assoociated with the id
            int resId = getResources().getIdentifier("button" + i, "id", getPackageName());
            button = (Button) findViewById(resId);
            //assign this as a listener
            button.setOnClickListener(this);
        }

        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setMovementMethod(new ScrollingMovementMethod());
    }



    @Override
    //the on click subroutine
    public void onClick(View v) {
        //basic variables which we will need for all operations
        TextView tv = (TextView)findViewById(R.id.textView);
        String str = tv.getText().toString();
        //check what button is pressed, solve if =, reset if ac, clear if del and handel special cases like 00 and .
        switch(((Button)v).getText().toString()){
            // Res is "equal to" without setting the "equal to" pressed flag
            case "Res":{
                tv.setText(solveEquation(tv.getText().toString()));
                break;
            }
            case "=":{
                isSolved = true; // set the "equal to" pressed flag
                tv.setText(solveEquation(tv.getText().toString()));
                break;
            }
            case "AC":{
                tv.setText("0");
                break;
            }
            case "Del":{

                if(str.length()>1 && !str.equals("Infinity")&&!isSolved)
                    tv.setText(str.substring(0,str.length()-1));
                else
                    tv.setText("0");

                break;
            }

            case ".":{
                //get last index of any operator used
                int index = 1;
                index = index>str.lastIndexOf("+")?index:str.lastIndexOf("+");
                index = index>str.lastIndexOf("-")?index:str.lastIndexOf("-");
                index = index>str.lastIndexOf("/")?index:str.lastIndexOf("/");
                index = index>str.lastIndexOf("X")?index:str.lastIndexOf("X");

                //do not allow for second insertion of decimal for one number
                if(str.lastIndexOf(".") < index){
                    appendText(tv,v);
                }
                break;
            }
            case "00":{
                try {
                    if (Double.parseDouble(str) == 0) {
                        break;
                    }
                    appendText(tv, v);
                }catch(NumberFormatException e){
                    appendText(tv,v);
                }finally {
                    break;
                }
            }
            case "+":
            case "-":
            case "X":
            case "/":{
                switch(str.charAt(str.length()-1)){
                    case '+':
                    case '-':
                    case 'X':
                    case '/':{
                        break;
                    }
                    default:{
                        appendText(tv,v);
                    }
                }
                break;
            }
            default:{
                appendText(tv,v);

            }

        }



    }

    private void appendText(TextView tv, View v) {
        //find if the button pressed is an operation
        boolean isOperation = false;
        switch(((Button)v).getText().toString()){
            case "+":
            case "-":
            case "X":
            case "/":{
                isOperation = true;
            }

        }
        //append text to the display panel only of the string is not 0 or if the last button pressed was not "equal to" and the current button is not an operation
        if(tv.getText().toString().equals("0") || (isSolved && !isOperation))
            tv.setText(((Button) v).getText().toString());
        else
            tv.setText(tv.getText()+ ((Button)v).getText().toString());

        isSolved = false; // set the equal to pressed flag to false again
    }

    public String solveEquation(String equation){
        String left = "0", operand="NS", right="0"; //initialise the solving variables
        //feed data to the left until you get an operand, then feed data to the right until next operand or end of string, the calculate store in left and continue traversing the string
        for(int i=0; i<equation.length(); i ++){
            switch(equation.charAt(i)){
                case '+':
                case '-':
                case 'X':
                case '/':{
                    if(!operand.equals("NS")){
                        left = calculate(left,operand,right);
                        right = "0";

                    }
                    operand = "" + equation.charAt(i);
                    break;
                }
                default:{
                    if(operand.equals("NS"))
                        left +=equation.charAt(i);
                    else
                        right +=equation.charAt(i);
                }
            }

        }
        left = calculate(left,operand,right);

        return left;
    }

    private String calculate(String left, String operand, String right) {
        //no need for comment here, still... carry out respective operations
        double leftDigits = Double.parseDouble(left);
        double rightDigits = Double.parseDouble(right);

        switch(operand){
            case "+":{leftDigits += rightDigits; break;}
            case "-":{leftDigits -= rightDigits; break;}
            case "X":{leftDigits *= rightDigits; break;}
            case "/":{leftDigits /= rightDigits; break;}
        }


        leftDigits *= 10000;
        leftDigits = Math.round(leftDigits);
        leftDigits /=10000;
        return ((leftDigits == (int)leftDigits)?Integer.toString((int)leftDigits):Double.toString(leftDigits));
    }
}
