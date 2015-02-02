package com.netcosports.rectangleview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by trung on 30/01/15.
 */
public class Program implements Comparable<Program>, Comparator<Program> {

    public int position;
    public int channel;
    public int start_time, end_time;
    public int duration;
    public String name;

    public Program(int channel, int start_time, int duration, String name) {
        this.channel = channel;
        this.start_time = start_time * 10;
        this.duration = duration * 10;
        this.end_time = this.start_time + this.duration;
        this.name = name;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    @Override
    public int compareTo(Program another) {
        if (start_time > another.start_time) {
            return 1;
        }
        else {
            if (start_time < another.start_time) {
                return -1;
            }
            else {
                if (end_time > another.end_time) {
                    return 1;
                }
                else if (end_time < another.end_time) {
                    return -1;
                }
                else {
                    if (channel > another.channel) {
                        return 1;
                    }
                    else if (channel < another.channel) {
                        return -1;
                    }
                    return 0;
                }
            }
        }
    }

    @Override
    public int compare(Program lhs, Program rhs) {
        if (lhs.start_time - rhs.start_time != 0) {
            return (lhs.start_time - rhs.start_time);
        }

        return (lhs.end_time - rhs.end_time);
    }

    public static ArrayList<Program> generateDummyPrograms() {
        ArrayList<Program> x = new ArrayList<>();

        x.add(new Program(1, 0, 80, "A1"));//Should be painted in the first step
        x.add(new Program(1, 80, 70, "A2"));
        x.add(new Program(1, 150, 40, "A3"));
        x.add(new Program(1, 190, 10, "A4"));
        x.add(new Program(1, 200, 70, "A5"));
        x.add(new Program(1, 270, 35, "A6"));
        x.add(new Program(1, 235, 70, "A7"));
        x.add(new Program(1, 305, 35, "A8"));
        x.add(new Program(1, 340, 70, "A9"));
        x.add(new Program(1, 410, 35, "A10"));
        x.add(new Program(1, 450, 40, "A11"));

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

//        x.add(new Program(4, 10, 40, "D1"));//Should be painted in the first step
//        x.add(new Program(4, 50, 80, "D2"));//Should be painted in the first step
//        x.add(new Program(4, 130, 60, "D3"));
//        x.add(new Program(4, 210, 40, "D4"));
//        x.add(new Program(4, 250, 80, "D5"));
//        x.add(new Program(4, 330, 20, "D6"));

        Collections.sort(x);

        for (int i = 0; i < x.size(); i++) {
            x.get(i).setPosition(i);
        }

        return x;
    }
}
