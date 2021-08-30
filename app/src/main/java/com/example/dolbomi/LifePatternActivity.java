package com.example.dolbomi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class LifePatternActivity extends AppCompatActivity {

    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    private DatabaseReference mDatabase;
    Calendar date = Calendar.getInstance();
    Calendar date1 = Calendar.getInstance();
    Calendar date2 = Calendar.getInstance();
    Calendar date3 = Calendar.getInstance();
    Calendar date4 = Calendar.getInstance();
    Calendar date5 = Calendar.getInstance();
    Calendar date6 = Calendar.getInstance();
    Calendar date7 = Calendar.getInstance();
    int year_ = date.get(Calendar.YEAR);
    int month_ = date.get(Calendar.MONTH) + 1;
    int day_ = date.get(Calendar.DAY_OF_MONTH);
    int hour_ = date.get(Calendar.HOUR_OF_DAY) + 9;
    LocalDate today;
    DayOfWeek dayOfWeek;
    int dayOfWeekNumber;
    String month_s;
    String month_s_1, month_s_2, month_s_3, month_s_4, month_s_5, month_s_6, month_s_7;
    String day_s;
    String day_s_1, day_s_2, day_s_3, day_s_4, day_s_5, day_s_6, day_s_7;

    String todayDate;
    String todayDate_1; // 오늘 -1
    String todayDate_2; // 오늘 -2
    String todayDate_3; // 오늘 -3
    String todayDate_4; // 오늘 -4
    String todayDate_5; // 오늘 -5
    String todayDate_6; // 오늘 -6
    String todayDate_7; // 오늘 -7

    int totalFrontDoorCount = 0;
    int totalBathRoomCount = 0;
    int totalBedRoomCount = 0;
    int totalRefrigeratorCount = 0;

    int count = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int totalFrontDoorCount = 0;
        int totalBathRoomCount = 0;
        int totalBedRoomCount = 0;
        int totalRefrigeratorCount = 0;

        int count = 0;

        processDatePickerResult(year_, month_, day_);

    }

    public void showDatePicker2(View view) {
        DatePickerFragment2 newFragment = new DatePickerFragment2();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void processDatePickerResult(int year, int month, int day){
        setContentView(R.layout.activity_life_pattern);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        totalFrontDoorCount = 0;
        totalBathRoomCount = 0;
        totalBedRoomCount = 0;
        totalRefrigeratorCount = 0;

        count = 0;

        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month-1);
        date.set(Calendar.DATE, day);
        date1.set(Calendar.YEAR, year);
        date1.set(Calendar.MONTH, month-1);
        date1.set(Calendar.DATE, day);
        date2.set(Calendar.YEAR, year);
        date2.set(Calendar.MONTH, month-1);
        date2.set(Calendar.DATE, day);
        date3.set(Calendar.YEAR, year);
        date3.set(Calendar.MONTH, month-1);
        date3.set(Calendar.DATE, day);
        date4.set(Calendar.YEAR, year);
        date4.set(Calendar.MONTH, month-1);
        date4.set(Calendar.DATE, day);
        date5.set(Calendar.YEAR, year);
        date5.set(Calendar.MONTH, month-1);
        date5.set(Calendar.DATE, day);
        date6.set(Calendar.YEAR, year);
        date6.set(Calendar.MONTH, month-1);
        date6.set(Calendar.DATE, day);
        date7.set(Calendar.YEAR, year);
        date7.set(Calendar.MONTH, month-1);
        date7.set(Calendar.DATE, day);

        today = LocalDate.of(year, month, day);

        dayOfWeek  = today.getDayOfWeek();
        dayOfWeekNumber = dayOfWeek.getValue();

//        if (hour_ >= 24) {
//            hour_ -= 24;
//            //day += 1;
//            dayOfWeekNumber += 1;
//            if (dayOfWeekNumber == 8) {
//                dayOfWeekNumber = 1;
//            }
//        }

        if(month < 10) {
            month_s = "0" + month;
        } else {
            month_s = String.valueOf(month);
        }
        if(day < 10) {
            day_s = "0" + day;
        } else {
            day_s = String.valueOf(day);
        }

        int flag = 0; // 전 달로 넘어갔는지 확인 플래그
        date1.add(date1.DATE, -1);
        int day_1 = date1.get(Calendar.DAY_OF_MONTH);
        int hour_1 = date1.get(Calendar.HOUR_OF_DAY) + 9;
        // 전 달로 넘어갈 경우
        int month_1;
        if (day_1 > day && flag == 0) {
            month_1 = month - 1;
            month = month_1;
            if(month_1 < 10) {
                month_s_1 = "0" + month_1;
            } else {
                month_s_1 = String.valueOf(month_1);
            }
            flag = 1;
        } else {
            month_1 = month_;
            if(month_1 < 10) {
                month_s_1 = "0" + month_1;
            } else {
                month_s_1 = String.valueOf(month_1);
            }
        }
        if (hour_1 >= 24) {
            hour_1 -= 24;
            day_1 += 1;
        }
        if(day_1 < 10) {
            day_s_1 = "0" + day_1;
        } else {
            day_s_1 = String.valueOf(day_1);
        }

        date2.add(date2.DATE, -2);
        int day_2 = date2.get(Calendar.DAY_OF_MONTH);
        int hour_2 = date1.get(Calendar.HOUR_OF_DAY) + 9;
        // 전 달로 넘어갈 경우
        int month_2;
        if (day_2 > day_ && flag == 0) {
            month_2 = month_ - 1;
            month = month_2;
            if(month_2 < 10) {
                month_s_2 = "0" + month_2;
            } else {
                month_s_2 = String.valueOf(month_2);
            }
            flag = 1;
        } else {
            month_2 = month_;
            if(month_2 < 10) {
                month_s_2 = "0" + month_2;
            } else {
                month_s_2 = String.valueOf(month_2);
            }
        }
        if (hour_2 >= 24) {
            hour_2 -= 24;
            day_2 += 1;
        }
        if(day_2 < 10) {
            day_s_2 = "0" + day_2;
        } else {
            day_s_2 = String.valueOf(day_2);
        }

        date3.add(date3.DATE, -3);
        int day_3 = date3.get(Calendar.DAY_OF_MONTH);
        int hour_3 = date1.get(Calendar.HOUR_OF_DAY) + 9;
        // 전 달로 넘어갈 경우
        int month_3;
        if (day_3 > day_ && flag == 0) {
            month_3 = month_ - 1;
            month = month_3;
            if(month_3 < 10) {
                month_s_3 = "0" + month_3;
            } else {
                month_s_3 = String.valueOf(month_3);
            }
            flag = 1;
        } else {
            month_3 = month_;
            if(month_3 < 10) {
                month_s_3 = "0" + month_3;
            } else {
                month_s_3 = String.valueOf(month_3);
            }
        }
        if (hour_3 >= 24) {
            hour_3 -= 24;
            day_3 += 1;
        }
        if(day_3 < 10) {
            day_s_3 = "0" + day_3;
        } else {
            day_s_3 = String.valueOf(day_3);
        }

        date4.add(date4.DATE, -4);
        int day_4 = date4.get(Calendar.DAY_OF_MONTH);
        int hour_4 = date1.get(Calendar.HOUR_OF_DAY) + 9;
        // 전 달로 넘어갈 경우
        int month_4;
        if (day_4 > day_ && flag == 0) {
            month_4 = month_ - 1;
            month = month_4;
            if(month_4 < 10) {
                month_s_4 = "0" + month_4;
            } else {
                month_s_4 = String.valueOf(month_4);
            }
            flag = 1;
        } else {
            month_4 = month_;
            if(month_4 < 10) {
                month_s_4 = "0" + month_4;
            } else {
                month_s_4 = String.valueOf(month_4);
            }
        }
        if (hour_4 >= 24) {
            hour_4 -= 24;
            day_4 += 1;
        }
        if(day_4 < 10) {
            day_s_4 = "0" + day_4;
        } else {
            day_s_4 = String.valueOf(day_4);
        }

        date5.add(date5.DATE, -5);
        int day_5 = date5.get(Calendar.DAY_OF_MONTH);
        int hour_5 = date1.get(Calendar.HOUR_OF_DAY) + 9;
        // 전 달로 넘어갈 경우
        int month_5;
        if (day_5 > day_ && flag == 0) {
            month_5 = month_ - 1;
            month = month_5;
            if(month_5 < 10) {
                month_s_5 = "0" + month_5;
            } else {
                month_s_5 = String.valueOf(month_5);
            }
            flag = 1;
        } else {
            month_5 = month_;
            if(month_5 < 10) {
                month_s_5 = "0" + month_5;
            } else {
                month_s_5 = String.valueOf(month_5);
            }
        }
        if (hour_5 >= 24) {
            hour_5 -= 24;
            day_5 += 1;
        }
        if(day_5 < 10) {
            day_s_5 = "0" + day_5;
        } else {
            day_s_5 = String.valueOf(day_5);
        }

        date6.add(date6.DATE, -6);
        int day_6 = date6.get(Calendar.DAY_OF_MONTH);
        int hour_6 = date1.get(Calendar.HOUR_OF_DAY) + 9;
        // 전 달로 넘어갈 경우
        int month_6;
        if (day_6 > day_ && flag == 0) {
            month_6 = month_ - 1;
            month = month_6;
            if(month_6 < 10) {
                month_s_6 = "0" + month_6;
            } else {
                month_s_6 = String.valueOf(month_6);
            }
            flag = 1;
        } else {
            month_6 = month_;
            if(month_6 < 10) {
                month_s_6 = "0" + month_6;
            } else {
                month_s_6 = String.valueOf(month_6);
            }
        }
        if (hour_6 >= 24) {
            hour_6 -= 24;
            day_6 += 1;
        }
        if(day_6 < 10) {
            day_s_6 = "0" + day_6;
        } else {
            day_s_6 = String.valueOf(day_6);
        }

        date7.add(date7.DATE, -7);
        int day_7 = date7.get(Calendar.DAY_OF_MONTH);
        int hour_7 = date1.get(Calendar.HOUR_OF_DAY) + 9;
        // 전 달로 넘어갈 경우
        int month_7;
        if (day_7 > day_ && flag == 0) {
            month_7 = month_ - 1;
            month = month_7;
            if(month_7 < 10) {
                month_s_7 = "0" + month_7;
            } else {
                month_s_7 = String.valueOf(month_7);
            }
            flag = 1;
        } else {
            month_7 = month_;
            if(month_7 < 10) {
                month_s_7 = "0" + month_7;
            } else {
                month_s_7 = String.valueOf(month_7);
            }
        }
        if (hour_7 >= 24) {
            hour_7 -= 24;
            day_7 += 1;
        }
        if(day_7 < 10) {
            day_s_7 = "0" + day_7;
        } else {
            day_s_7 = String.valueOf(day_7);
        }


        todayDate = year_ + "-" + month_s + "-" + day_s;
        todayDate_1 = year_ + "-" + month_s_1 + "-" + day_s_1;
        todayDate_2 = year_ + "-" + month_s_2 + "-" + day_s_2;
        todayDate_3 = year_ + "-" + month_s_3 + "-" + day_s_3;
        todayDate_4 = year_ + "-" + month_s_4 + "-" + day_s_4;
        todayDate_5 = year_ + "-" + month_s_5 + "-" + day_s_5;
        todayDate_6 = year_ + "-" + month_s_6 + "-" + day_s_6;
        todayDate_7 = year_ + "-" + month_s_7 + "-" + day_s_7;

        getLifePatternValue(dayOfWeekNumber);

        count = 0;
        getLifePatternValue1to7(count);

        TextView dataTextView = (TextView) findViewById(R.id.dataTextView);
        dataTextView.setText(todayDate);
    }

    synchronized private void getLifePatternValue1to7(int count){
        for(int i=0; i<8; i++){
            switch(this.count){
                case 0: getLifePatternValue_1();
                    this.count++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1: getLifePatternValue_2();
                    this.count++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2: getLifePatternValue_3();
                    this.count++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3: getLifePatternValue_4();
                    this.count++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4: getLifePatternValue_5();
                    this.count++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5: getLifePatternValue_6();
                    this.count++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6: getLifePatternValue_7();
                    this.count++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7: getTodayLifePatternValue();
                    this.count++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }


    }

    private void getLifePatternValue_1() {

        final DatabaseReference frontDoorValue1 = mDatabase.child("Home1").child(todayDate_1).child("Frontdoor"); // 현관문 센서 값
        final DatabaseReference bathRoomValue1 = mDatabase.child("Home1").child(todayDate_1).child("Bathroom"); // 욕실 문 센서 값
        final DatabaseReference bedRoomValue1 = mDatabase.child("Home1").child(todayDate_1).child("Bedroom"); // 침실 문 센서 값
        final DatabaseReference refrigeratorValue1 = mDatabase.child("Home1").child(todayDate_1).child("Refrigerator"); // 냉장고 센서 값

        bedRoomValue1.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long bedRoomCount = snapshot.getChildrenCount();

                    totalBedRoomCount += bedRoomCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        bathRoomValue1.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long bathroomCount = snapshot.getChildrenCount();

                    totalBathRoomCount += bathroomCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        frontDoorValue1.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long frontDoorCount = snapshot.getChildrenCount();

                    totalFrontDoorCount += frontDoorCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        refrigeratorValue1.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long refrigeratorCount = snapshot.getChildrenCount();

                    totalRefrigeratorCount += refrigeratorCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    private void getLifePatternValue_2() {

        final DatabaseReference frontDoorValue2 = mDatabase.child("Home1").child(todayDate_2).child("Frontdoor"); // 현관문 센서 값
        final DatabaseReference bathRoomValue2 = mDatabase.child("Home1").child(todayDate_2).child("Bathroom"); // 욕실 문 센서 값
        final DatabaseReference bedRoomValue2 = mDatabase.child("Home1").child(todayDate_2).child("Bedroom"); // 침실 문 센서 값
        final DatabaseReference refrigeratorValue2 = mDatabase.child("Home1").child(todayDate_2).child("Refrigerator"); // 냉장고 센서 값

        bedRoomValue2.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long bedRoomCount = snapshot.getChildrenCount();

                    totalBedRoomCount += bedRoomCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        bathRoomValue2.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long bathroomCount = snapshot.getChildrenCount();

                    totalBathRoomCount += bathroomCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        frontDoorValue2.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long frontDoorCount = snapshot.getChildrenCount();

                    totalFrontDoorCount += frontDoorCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        refrigeratorValue2.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long refrigeratorCount = snapshot.getChildrenCount();

                    totalRefrigeratorCount += refrigeratorCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void getLifePatternValue_3() {

        final DatabaseReference frontDoorValue3 = mDatabase.child("Home1").child(todayDate_3).child("Frontdoor"); // 현관문 센서 값
        final DatabaseReference bathRoomValue3 = mDatabase.child("Home1").child(todayDate_3).child("Bathroom"); // 욕실 문 센서 값
        final DatabaseReference bedRoomValue3 = mDatabase.child("Home1").child(todayDate_3).child("Bedroom"); // 침실 문 센서 값
        final DatabaseReference refrigeratorValue3 = mDatabase.child("Home1").child(todayDate_3).child("Refrigerator"); // 냉장고 센서 값

        bedRoomValue3.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long bedRoomCount = snapshot.getChildrenCount();

                    totalBedRoomCount = bedRoomCount.intValue() + totalBedRoomCount;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        bathRoomValue3.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long bathroomCount = snapshot.getChildrenCount();

                    totalBathRoomCount += bathroomCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        frontDoorValue3.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long frontDoorCount = snapshot.getChildrenCount();

                    totalFrontDoorCount += frontDoorCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        refrigeratorValue3.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long refrigeratorCount = snapshot.getChildrenCount();

                    totalRefrigeratorCount += refrigeratorCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void getLifePatternValue_4() {

        final DatabaseReference frontDoorValue4 = mDatabase.child("Home1").child(todayDate_4).child("Frontdoor"); // 현관문 센서 값
        final DatabaseReference bathRoomValue4 = mDatabase.child("Home1").child(todayDate_4).child("Bathroom"); // 욕실 문 센서 값
        final DatabaseReference bedRoomValue4 = mDatabase.child("Home1").child(todayDate_4).child("Bedroom"); // 침실 문 센서 값
        final DatabaseReference refrigeratorValue4 = mDatabase.child("Home1").child(todayDate_4).child("Refrigerator"); // 냉장고 센서 값

        bedRoomValue4.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long bedRoomCount = snapshot.getChildrenCount();

                    totalBedRoomCount += bedRoomCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        bathRoomValue4.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long bathroomCount = snapshot.getChildrenCount();

                    totalBathRoomCount += bathroomCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        frontDoorValue4.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long frontDoorCount = snapshot.getChildrenCount();

                    totalFrontDoorCount += frontDoorCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        refrigeratorValue4.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long refrigeratorCount = snapshot.getChildrenCount();

                    totalRefrigeratorCount += refrigeratorCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void getLifePatternValue_5() {

        final DatabaseReference frontDoorValue5 = mDatabase.child("Home1").child(todayDate_5).child("Frontdoor"); // 현관문 센서 값
        final DatabaseReference bathRoomValue5 = mDatabase.child("Home1").child(todayDate_5).child("Bathroom"); // 욕실 문 센서 값
        final DatabaseReference bedRoomValue5 = mDatabase.child("Home1").child(todayDate_5).child("Bedroom"); // 침실 문 센서 값
        final DatabaseReference refrigeratorValue5 = mDatabase.child("Home1").child(todayDate_5).child("Refrigerator"); // 냉장고 센서 값

        bedRoomValue5.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long bedRoomCount = snapshot.getChildrenCount();

                    totalBedRoomCount += bedRoomCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        bathRoomValue5.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long bathroomCount = snapshot.getChildrenCount();

                    totalBathRoomCount += bathroomCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        frontDoorValue5.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long frontDoorCount = snapshot.getChildrenCount();

                    totalFrontDoorCount += frontDoorCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        refrigeratorValue5.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long refrigeratorCount = snapshot.getChildrenCount();

                    totalRefrigeratorCount += refrigeratorCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void getLifePatternValue_6() {

        final DatabaseReference frontDoorValue6 = mDatabase.child("Home1").child(todayDate_6).child("Frontdoor"); // 현관문 센서 값
        final DatabaseReference bathRoomValue6 = mDatabase.child("Home1").child(todayDate_6).child("Bathroom"); // 욕실 문 센서 값
        final DatabaseReference bedRoomValue6 = mDatabase.child("Home1").child(todayDate_6).child("Bedroom"); // 침실 문 센서 값
        final DatabaseReference refrigeratorValue6 = mDatabase.child("Home1").child(todayDate_6).child("Refrigerator"); // 냉장고 센서 값

        bedRoomValue6.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long bedRoomCount = snapshot.getChildrenCount();

                    totalBedRoomCount += bedRoomCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        bathRoomValue6.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long bathroomCount = snapshot.getChildrenCount();

                    totalBathRoomCount += bathroomCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        frontDoorValue6.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long frontDoorCount = snapshot.getChildrenCount();

                    totalFrontDoorCount += frontDoorCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        refrigeratorValue6.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long refrigeratorCount = snapshot.getChildrenCount();

                    totalRefrigeratorCount += refrigeratorCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void getLifePatternValue_7() {

        final DatabaseReference frontDoorValue7 = mDatabase.child("Home1").child(todayDate_7).child("Frontdoor"); // 현관문 센서 값
        final DatabaseReference bathRoomValue7 = mDatabase.child("Home1").child(todayDate_7).child("Bathroom"); // 욕실 문 센서 값
        final DatabaseReference bedRoomValue7 = mDatabase.child("Home1").child(todayDate_7).child("Bedroom"); // 침실 문 센서 값
        final DatabaseReference refrigeratorValue7 = mDatabase.child("Home1").child(todayDate_7).child("Refrigerator"); // 냉장고 센서 값

        bedRoomValue7.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long bedRoomCount = snapshot.getChildrenCount();

                    totalBedRoomCount += bedRoomCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        bathRoomValue7.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long bathroomCount = snapshot.getChildrenCount();

                    totalBathRoomCount += bathroomCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        frontDoorValue7.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long frontDoorCount = snapshot.getChildrenCount();

                    totalFrontDoorCount += frontDoorCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        refrigeratorValue7.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long refrigeratorCount = snapshot.getChildrenCount();

                    totalRefrigeratorCount += refrigeratorCount.intValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void getLifePatternValue(int dayOfWeekNumber) {
        setContentView(R.layout.activity_life_pattern);

        Button monButton = (Button) findViewById(R.id.monButton);
        Button tueButton = (Button) findViewById(R.id.tueButton);
        Button wedButton = (Button) findViewById(R.id.wedButton);
        Button thuButton = (Button) findViewById(R.id.thuButton);
        Button friButton = (Button) findViewById(R.id.friButton);
        Button satButton = (Button) findViewById(R.id.satButton);
        Button sunButton = (Button) findViewById(R.id.sunButton);

        if (dayOfWeekNumber == 1) {
            // 월요일
            monButton.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.today_button));
            monButton.setTextColor(Color.WHITE);
        } else if (dayOfWeekNumber == 2) {
            // 화요일
            tueButton.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.today_button));
            tueButton.setTextColor(Color.WHITE);
        } else if (dayOfWeekNumber == 3) {
            // 수요일
            wedButton.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.today_button));
            wedButton.setTextColor(Color.WHITE);
        } else if (dayOfWeekNumber == 4) {
            // 목요일
            thuButton.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.today_button));
            thuButton.setTextColor(Color.WHITE);
        } else if (dayOfWeekNumber == 5) {
            // 금요일
            friButton.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.today_button));
            friButton.setTextColor(Color.WHITE);
        } else if (dayOfWeekNumber == 6) {
            // 토요일
            satButton.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.today_button));
            satButton.setTextColor(Color.WHITE);
        } else if (dayOfWeekNumber == 7) {
            // 일요일
            sunButton.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.today_button));
            sunButton.setTextColor(Color.WHITE);
        }

    }

    private void getTodayLifePatternValue() {

        final DatabaseReference frontDoorValue = mDatabase.child("Home1").child(todayDate).child("Frontdoor"); // 현관문 센서 값
        final DatabaseReference bathRoomValue = mDatabase.child("Home1").child(todayDate).child("Bathroom"); // 욕실 문 센서 값
        final DatabaseReference bedRoomValue = mDatabase.child("Home1").child(todayDate).child("Bedroom"); // 침실 문 센서 값
        final DatabaseReference refrigeratorValue = mDatabase.child("Home1").child(todayDate).child("Refrigerator"); // 냉장고 센서 값


        ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.progressBar1_bedroom);
        ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.progressBar2_bathroom);
        ProgressBar progressBar3 = (ProgressBar) findViewById(R.id.progressBar3_drug);
        ProgressBar progressBar4 = (ProgressBar) findViewById(R.id.progressBar4_frontdoor);
        ProgressBar progressBar5 = (ProgressBar) findViewById(R.id.progressBar5_refrigerator);

        TextView textView1 = (TextView) findViewById(R.id.textView1);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        TextView textView5 = (TextView) findViewById(R.id.textView5);

        bedRoomValue.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long bedRoomCount = snapshot.getChildrenCount();

                    if(totalBedRoomCount < 7) {
                        progressBar1.setMax(1);
                    } else {
                        progressBar1.setMax(totalBedRoomCount / 7);
                    }

                    String bedText = bedRoomCount.toString() + " / " + (totalBedRoomCount / 7);
                    textView1.setText(bedText);
                    progressBar1.setProgress(bedRoomCount.intValue());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        bathRoomValue.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long bathroomCount = snapshot.getChildrenCount();

                    if(totalBathRoomCount < 7) {
                        progressBar2.setMax(1);
                    } else {
                        progressBar2.setMax(totalBathRoomCount / 7);
                    }

                    String bathText = bathroomCount + " / " +  (totalBathRoomCount / 7);
                    textView2.setText(bathText);
                    progressBar2.setProgress(bathroomCount.intValue());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        frontDoorValue.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long frontDoorCount = snapshot.getChildrenCount();

                    if(totalFrontDoorCount < 7) {
                        progressBar4.setMax(1);
                    } else {
                        progressBar4.setMax(totalFrontDoorCount / 7);
                    }

                    String frontText = frontDoorCount + " / " + (totalFrontDoorCount / 7);
                    textView4.setText(frontText);
                    progressBar4.setProgress(frontDoorCount.intValue());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        refrigeratorValue.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    Long refrigeratorCount = snapshot.getChildrenCount();

                    if(totalRefrigeratorCount < 7) {
                        progressBar5.setMax(1);
                    } else {
                        progressBar5.setMax(totalRefrigeratorCount / 7);
                    }

                    String refriText = refrigeratorCount + " / " + (totalRefrigeratorCount / 7);
                    textView5.setText(refriText);
                    progressBar5.setProgress(refrigeratorCount.intValue());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}