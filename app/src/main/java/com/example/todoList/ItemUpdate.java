package com.example.todoList;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ItemUpdate extends AppCompatActivity {

    private EditText name;
    private EditText description;
    private TextView date1;
    private ItemDao itemDao;

    private Item item;
    private String name1;
    private String description1;
    private String strDate;
    private Date date;
    private int i;
    private ImageView imageView1;
    private Bitmap bmpImage1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_update);

        name = findViewById(R.id.updateItem);
        description = findViewById(R.id.updateDescription);
        date1 = findViewById(R.id.updateDate);
        Button button1 = findViewById(R.id.reminderButton);
        Button button2 = findViewById(R.id.takeImage1);
        imageView1 = findViewById(R.id.imageView1);

        item = (Item) getIntent().getSerializableExtra("item");
        loadItem(item);
        i = item.getId();
        String date0 = date1.getText().toString().trim();

        DateFormat df = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        try{

            Log.d("Date",date0);
            Date date111 = df.parse(date0);
            Log.d("Date",date0);
            strDate= outputFormat.format(date111);
        }catch(ParseException pe){
            pe.printStackTrace();
        }

        date1.setText(strDate);

        date1.setOnClickListener(new View.OnClickListener() {

            final Calendar calendar = Calendar.getInstance();
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.YEAR);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ItemUpdate.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String s1 = year +"-"+(month+1)+"-"+day;
                        date1.setText(s1);
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


        Log.d("ABC","RRR");

        findViewById(R.id.updateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(item);
                Log.d("ABC","BBB");

            }
        });

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(ItemUpdate.this,Reminder.class);
               intent.putExtra("id", i);
                intent.putExtra("name", name.getText().toString().trim());
                intent.putExtra("description", description.getText().toString().trim());
                intent.putExtra("date", strDate);

                startActivity(intent);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture1();
            }
        });
    }

    private final int CAMERA_INTENT = 51;

    private void takePicture1(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) !=null){
            startActivityForResult(intent,CAMERA_INTENT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_INTENT){

            Bitmap bmpImage = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).get("data");
                if(bmpImage !=null){
                    imageView1.setImageBitmap(bmpImage);
                }
        }
    }


    private void update(Item item){

        name1= name.getText().toString().trim();
        description1 = description.getText().toString().trim();
        String date11 = date1.getText().toString().trim();
        if (imageView1.getDrawable()!=null) {
            bmpImage1 = ((BitmapDrawable) imageView1.getDrawable()).getBitmap();
        }else{
            bmpImage1 = null;
        }
        Log.d("ABC","A");

        if (name1.isEmpty()) {
            name.setError("Item required");
            name.requestFocus();
            return;
        }

        if (description1.isEmpty()) {
            description.setError("Description required");
            description.requestFocus();
            return;
        }


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        try {
            date = format.parse(date11);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        class UpdateItemAsyncTask extends AsyncTask<Item,Void ,Void> {


            UpdateItemAsyncTask(ItemDao itemDao) {
                new ItemUpdate().itemDao = itemDao;
            }

            @Override
            protected Void doInBackground(Item... items) {

                Log.d("ABC","B");
                if(bmpImage1!=null) {
                    ItemDatabase.getInstance(ItemUpdate.this).itemDao().update(i, name1, description1, date,DataConverter.convertImage2ByteArray(bmpImage1));
                } else {
                    ItemDatabase.getInstance(ItemUpdate.this).itemDao().update(i, name1, description1, date,null);
                }
                Log.d("ABC","updated");
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                finish();
                //adapter.notifyDataSetChanged();
                startActivity(new Intent(ItemUpdate.this, MainActivity.class));
            }
        }

        new UpdateItemAsyncTask(itemDao).execute(item);
        Log.d("ABC","executed");

    }

    private void loadItem(Item item) {

        name.setText(item.getName());
        description.setText(item.getDescription());
        date1.setText(String.valueOf(item.getDate()));
        if (item.getImage()!=null) {
            imageView1.setImageBitmap(DataConverter.convertByteArray2Image(item.getImage()));
        }
        Log.d("ABC","finally");

    }
}
