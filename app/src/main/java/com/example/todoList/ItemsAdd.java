package com.example.todoList;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ItemsAdd extends AppCompatActivity {

    private ItemDao itemDao;
    private EditText addItem;
    private EditText addDescription;
    private TextView displayDate;
    private Date date;
    private Item item;
    private ImageView imageview;
    private Bitmap bmpImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_items);
        addItem = findViewById(R.id.addItems);
        addDescription = findViewById(R.id.addDescription);
        TextView addDate = findViewById(R.id.addDate);
        displayDate = findViewById(R.id.displayDate);
        Button button2 = findViewById(R.id.takeImage);
        imageview = findViewById(R.id.imageView);


        addDate.setOnClickListener(new View.OnClickListener() {

            final Calendar calendar = Calendar.getInstance();
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.YEAR);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);


            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ItemsAdd.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String s = year +"-"+(month+1)+"-"+day;
                        displayDate.setText(s);
                    }
                },year,month,day);

                Calendar c = Calendar.getInstance();
                Calendar c1 = Calendar.getInstance();
                c1.set(Calendar.YEAR,2050);
                c1.set(Calendar.MONTH,0);
                c1.set(Calendar.DAY_OF_MONTH,1);

                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(c1.getTimeInMillis());
                int year = c.get(Calendar.YEAR);
                int month= c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog.getDatePicker().init(year,month,day,null);
                datePickerDialog.show();
            }
        });


        Button button1 = findViewById(R.id.saveButton);

        button1.setOnClickListener(new View.OnClickListener() {

                                      @Override
                                      public void onClick(View v) {
                                          insert();
                                      }
                                  }
        );

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }


    private final int CAMERA_INTENT = 51;

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) !=null){
            startActivityForResult(intent,CAMERA_INTENT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_INTENT){

                bmpImage = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).get("data");
                if(bmpImage !=null){
                    imageview.setImageBitmap(bmpImage);
                }
        }
    }

    private void insert() {

        final String addItem1 = addItem.getText().toString().trim();
        final String addDescription1 = addDescription.getText().toString().trim();
        final String addDate1 = displayDate.getText().toString().trim();


        if (addItem1.isEmpty()) {
            addItem.setError("Item required");
            addItem.requestFocus();
            return;
        }

        if (addDescription1.isEmpty()) {
            addDescription.setError("Description required");
            addDescription.requestFocus();
            return;
        }

        if (addDate1.isEmpty()) {
            displayDate.setError("Date required");
            displayDate.requestFocus();
            return;
        }



        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",java.util.Locale.getDefault());
        try {
            date = format.parse(addDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }




        class InsertItemAsyncTask extends AsyncTask<Item,Void ,Void> {

            InsertItemAsyncTask(ItemDao itemDao){
                    new ItemsAdd().itemDao = itemDao;
            }


            @Override
            protected Void doInBackground(Item... items) {

                if(bmpImage!=null) {

                    item = new Item(addItem1, addDescription1, date, DataConverter.convertImage2ByteArray(bmpImage));
                }else {
                    item = new Item(addItem1, addDescription1, date, null);
                }



                ItemDatabase.getInstance(ItemsAdd.this).itemDao().insert(item);
                return null;


            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(ItemsAdd.this, MainActivity.class));
            }



        }

        new InsertItemAsyncTask(itemDao).execute();







    }




}
