package com.netcosports.rectangleview;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by trung on 30/01/15.
 */
public class Program {

    public int positionOrderedByStartTime, positionOrderedByEndTime;
    public int channel;
    public int start_time, end_time;
    public int duration;
    public String name;

    public int color;

    public Program(int channel, int start_time, int duration, String name) {
        this.channel = channel;
        this.start_time = start_time * 10;
        this.duration = duration * 10;
        this.end_time = this.start_time + this.duration;
        this.name = name;

        this.color = generateDummyColor();
    }

    public void setPositionOrderedByStartTime(int position) {
        this.positionOrderedByStartTime = position;
    }

    public void setPositionOrderedByEndTime(int position) {
        this.positionOrderedByEndTime = position;
    }

    public static ArrayList<Program> generateDummyPrograms() {
        ArrayList<Program> x = new ArrayList<>();

        x.add(new Program(1, 0, 80, "A1"));//Should be painted in the first step
        x.add(new Program(1, 80, 70, "A2"));
        x.add(new Program(1, 150, 40, "A3"));
        x.add(new Program(1, 190, 10, "A4"));
        x.add(new Program(1, 200, 70, "A5"));
        x.add(new Program(1, 270, 35, "A6"));
        x.add(new Program(1, 305, 35, "A7"));
        x.add(new Program(1, 340, 70, "A8"));
        x.add(new Program(1, 410, 35, "A9"));
        x.add(new Program(1, 450, 40, "A10"));
        x.add(new Program(1, 500, 60, "A11"));


        x.add(new Program(2, 40, 40, "B1"));//Should be painted in the first step
        x.add(new Program(2, 80, 60, "B2"));
        x.add(new Program(2, 140, 60, "B3"));
        x.add(new Program(2, 200, 55, "B4"));
        x.add(new Program(2, 255, 70, "B5"));
        x.add(new Program(2, 325, 60, "B6"));
        x.add(new Program(2, 385, 70, "B7"));
        x.add(new Program(2, 500, 60, "B8"));
        x.add(new Program(2, 600, 60, "B9"));

        x.add(new Program(3, 25, 200, "C1"));//Should be painted in the first step
        x.add(new Program(3, 225, 40, "C2"));
        x.add(new Program(3, 265, 55, "C3"));
        x.add(new Program(3, 320, 20, "C4"));
        x.add(new Program(3, 340, 10, "C5"));
        x.add(new Program(3, 360, 40, "C6"));
        x.add(new Program(3, 400, 15, "C7"));
        x.add(new Program(3, 415, 20, "C8"));
        x.add(new Program(3, 500, 15, "C9"));
        x.add(new Program(3, 515, 100, "C10"));

        x.add(new Program(4, 10, 40, "D1"));//Should be painted in the first step
        x.add(new Program(4, 50, 80, "D2"));//Should be painted in the first step
        x.add(new Program(4, 130, 60, "D3"));
        x.add(new Program(4, 210, 40, "D4"));
        x.add(new Program(4, 250, 80, "D5"));
        x.add(new Program(4, 330, 20, "D6"));

//        x.add(new Program(5, 10, 40, "E1"));//Should be painted in the first step
//        x.add(new Program(5, 50, 80, "E2"));//Should be painted in the first step
//        x.add(new Program(5, 130, 60, "E3"));
//        x.add(new Program(5, 210, 40, "E4"));
//        x.add(new Program(5, 250, 80, "E5"));
//        x.add(new Program(5, 330, 20, "E6"));


        Collections.sort(x, new Comparator<Program>() {
            @Override
            public int compare(Program lhs, Program rhs) {
                if (rhs.end_time - lhs.end_time != 0) {
                    return (rhs.end_time - lhs.end_time);
                }

                return (rhs.start_time - lhs.start_time);
            }
        });
        for (int i = 0; i < x.size(); i++) {
            x.get(i).setPositionOrderedByEndTime(i);
        }


        Collections.sort(x, new Comparator<Program>() {
            @Override
            public int compare(Program lhs, Program rhs) {
                if (lhs.start_time - rhs.start_time != 0) {
                    return (lhs.start_time - rhs.start_time);
                }

                return (lhs.end_time - rhs.end_time);
            }
        });

        for (int i = 0; i < x.size(); i++) {
            x.get(i).setPositionOrderedByStartTime(i);
        }

        return x;
    }

    public int generateDummyColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
