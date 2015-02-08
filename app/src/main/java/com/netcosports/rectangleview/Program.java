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
    public int start_time, end_time;
    public int channel;
    public int duration;
    public String name;

    public int color;

    public Program(int start_time, int duration, String name) {
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

    public static ArrayList<Program> generateFirstDummyPrograms() {
        ArrayList<Program> x = new ArrayList<>();

        x.add(new Program(0, 80, "A1"));//Should be painted in the first step
        x.add(new Program(80, 70, "A2"));
        x.add(new Program(150, 40, "A3"));
        x.add(new Program(190, 10, "A4"));
        x.add(new Program(200, 70, "A5"));
        x.add(new Program(270, 35, "A6"));
        x.add(new Program(305, 35, "A7"));
        x.add(new Program(340, 70, "A8"));
        x.add(new Program(410, 35, "A9"));
        x.add(new Program(450, 40, "A10"));
        x.add(new Program(500, 60, "A11"));
        x.add(new Program(600, 55, "A12"));
        x.add(new Program(655, 45, "A13"));
        x.add(new Program(700, 80, "A14"));
        x.add(new Program(780, 32, "A15"));
        x.add(new Program(850, 40, "A16"));
        x.add(new Program(900, 60, "A17"));
        x.add(new Program(960, 35, "A18"));
        x.add(new Program(995, 5, "A19"));
        x.add(new Program(1000, 60, "A20"));

        return x;
    }

    public static ArrayList<Program> generateSecondDummyPrograms() {
        ArrayList<Program> x = new ArrayList<>();

        x.add(new Program(40, 40, "B1"));//Should be painted in the first step
        x.add(new Program(80, 60, "B2"));
        x.add(new Program(140, 60, "B3"));
        x.add(new Program(200, 55, "B4"));
        x.add(new Program(255, 70, "B5"));
        x.add(new Program(325, 60, "B6"));
        x.add(new Program(385, 70, "B7"));
        x.add(new Program(500, 60, "B8"));
        x.add(new Program(600, 60, "B9"));
        x.add(new Program(660, 60, "B10"));
        x.add(new Program(720, 70, "B11"));
        x.add(new Program(790, 10, "B12"));
        x.add(new Program(800, 90, "B13"));

        return x;
    }

    public static ArrayList<Program> generateThirdDummyPrograms() {
        ArrayList<Program> x = new ArrayList<>();

        x.add(new Program(25, 200, "C1"));//Should be painted in the first step
        x.add(new Program(225, 40, "C2"));
        x.add(new Program(265, 55, "C3"));
        x.add(new Program(320, 20, "C4"));
        x.add(new Program(340, 10, "C5"));
        x.add(new Program(360, 40, "C6"));
        x.add(new Program(400, 15, "C7"));
        x.add(new Program(415, 20, "C8"));
        x.add(new Program(500, 15, "C9"));
        x.add(new Program(515, 100, "C10"));
        x.add(new Program(620, 10, "C11"));
        x.add(new Program(650, 40, "C12"));
        x.add(new Program(690, 100, "C13"));
        x.add(new Program(800, 20, "C14"));
        x.add(new Program(820, 30, "C15"));
        x.add(new Program(850, 50, "C16"));

        return x;
    }

    public static ArrayList<Program> generateFourthDummyPrograms() {
        ArrayList<Program> x = new ArrayList<>();

        x.add(new Program(10, 40, "D1"));//Should be painted in the first step
        x.add(new Program(50, 80, "D2"));//Should be painted in the first step
        x.add(new Program(130, 60, "D3"));
        x.add(new Program(210, 40, "D4"));
        x.add(new Program(250, 80, "D5"));
        x.add(new Program(330, 20, "D6"));
        x.add(new Program(350, 60, "D7"));
        x.add(new Program(410, 40, "D8"));
        x.add(new Program(450, 80, "D9"));
        x.add(new Program(530, 20, "D10"));
        x.add(new Program(550, 60, "D11"));
        x.add(new Program(610, 100, "D12"));
        x.add(new Program(710, 80, "D13"));
        x.add(new Program(790, 20, "D14"));
        x.add(new Program(850, 60, "D15"));
        x.add(new Program(910, 10, "D16"));
        x.add(new Program(920, 20, "D17"));
        x.add(new Program(940, 30, "D18"));

        return x;
    }

    public static ArrayList<Program> generateFifthDummyPrograms() {
        ArrayList<Program> x = new ArrayList<>();

        x.add(new Program(10, 40, "E1"));//Should be painted in the first step
        x.add(new Program(50, 80, "E2"));//Should be painted in the first step
        x.add(new Program(130, 60, "E3"));
        x.add(new Program(210, 40, "E4"));
        x.add(new Program(250, 80, "E5"));
        x.add(new Program(330, 20, "E6"));
        x.add(new Program(350, 60, "E7"));
        x.add(new Program(410, 40, "E8"));
        x.add(new Program(450, 80, "E9"));
        x.add(new Program(530, 20, "E10"));
        x.add(new Program(550, 60, "E11"));
        x.add(new Program(610, 100, "E12"));
        x.add(new Program(710, 80, "E13"));
        x.add(new Program(790, 20, "E14"));
        x.add(new Program(850, 60, "E15"));
        x.add(new Program(910, 10, "E16"));
        x.add(new Program(920, 20, "E17"));
        x.add(new Program(940, 30, "E18"));

        sortProgram(x);

        return x;
    }

    public static ArrayList<Program> generateSixthDummyPrograms() {
        ArrayList<Program> x = new ArrayList<>();

        x.add(new Program(10, 40, "F1"));//Should be painted in the first step
        x.add(new Program(50, 80, "F2"));//Should be painted in the first step
        x.add(new Program(130, 60, "F3"));
        x.add(new Program(210, 40, "F4"));
        x.add(new Program(250, 80, "F5"));
        x.add(new Program(330, 20, "F6"));

        sortProgram(x);

        return x;
    }

    public static ArrayList<Program> generateSeventhDummyPrograms() {
        ArrayList<Program> x = new ArrayList<>();

        x.add(new Program(10, 40, "G1"));//Should be painted in the first step
        x.add(new Program(50, 80, "G2"));//Should be painted in the first step
        x.add(new Program(130, 60, "G3"));
        x.add(new Program(210, 40, "G4"));
        x.add(new Program(250, 80, "G5"));
        x.add(new Program(330, 20, "G6"));

        sortProgram(x);

        return x;
    }

    public static void sortProgram(final ArrayList<Program> x){
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
    }

    public int generateDummyColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
