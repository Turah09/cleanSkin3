package com.keturahedwards.cleanskin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.Calendar;

public class BrowPencil extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    Animation zoom_in;
    ImageView background_image;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText newBrandName, newpurchaseDate, newexpiration;
    private Spinner type;
    private Button save, cancel;
    private ImageView datePicker;
    private int oldMonth;
    private int oldYear;
    private int oldDay;
    private int newYear;
    private ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_brow_pencil);

        zoom_in = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        background_image = findViewById(R.id.background);
        background_image.setAnimation(zoom_in);

        Button add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCalenderPopIp();
            }
        });

        ImageView home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BrowPencil.this, DashBoard.class));
            }
        });
    }
    public void createCalenderPopIp() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View createPopUpWindow = getLayoutInflater().inflate(R.layout.popup, null);
        newBrandName = (EditText) createPopUpWindow.findViewById(R.id.newBrandName);
        type = (Spinner) createPopUpWindow.findViewById(R.id.type);
        icon = (ImageView) createPopUpWindow.findViewById(R.id.blank);
        icon.setImageResource((R.drawable.eyebrow_pencil));
        newpurchaseDate = (EditText) createPopUpWindow.findViewById(R.id.NewPuchaseDate);
        newexpiration = (EditText) createPopUpWindow.findViewById(R.id.NewExpirationDate);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.eyebrow, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        save = (Button) createPopUpWindow.findViewById(R.id.save);
        cancel = (Button) createPopUpWindow.findViewById(R.id.cancel);

        datePicker = (ImageView) createPopUpWindow.findViewById(R.id.datePicker);
        dialogBuilder.setView(createPopUpWindow);
        dialog = dialogBuilder.create();
        dialog.show();

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        cancel.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        }));
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(newYear, oldMonth, oldDay, 9, 00);
                Calendar endTime = Calendar.getInstance();
                endTime.set(newYear, oldMonth, oldDay, 9, 30);
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
                intent.putExtra(CalendarContract.Events.ALL_DAY, false);
                intent.putExtra(CalendarContract.Events.TITLE, "Brow Makeup update for " + newBrandName.getText().toString());
                intent.putExtra(CalendarContract.Events.DESCRIPTION, "Expiration date for" + newBrandName.getText().toString() + " "+ type.getSelectedItem().toString());
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = month + "/ " + dayOfMonth + " / " + year;
        newpurchaseDate.setText(date);
        oldDay = dayOfMonth;
        String oppick = type.getSelectedItem().toString();
        if (oppick.equals("Pencil")|| oppick.equals("Pomade")) {
            String newdate = month + "/ " + dayOfMonth + " / " + (year + 2);
            newexpiration.setText(newdate);
            oldMonth = month;
            newYear = (year + 2);
        }else if(oppick.equals("Gel")){
            int newMonth = month + 6;
            if(newMonth > 12){
                String newDate = (newMonth - 12) + " / " + dayOfMonth + " / " + (year + 1);
                newexpiration.setText(newDate);
                oldMonth = (newMonth - 12);
                newYear = (year + 1);
            }else{
                String newDate = newMonth + " / " + dayOfMonth + " / " + year;
                newexpiration.setText(newDate);
                oldMonth = newMonth;
            }
        }else if(oppick.equals("Powder")){
            String newdate = month + "/ " + dayOfMonth + " / " + (year + 2);
            newexpiration.setText(newdate);
            oldMonth = month;
            newYear = (year +2);
        }
    }
}
