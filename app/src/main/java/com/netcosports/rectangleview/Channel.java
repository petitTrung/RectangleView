package com.netcosports.rectangleview;

import java.util.ArrayList;

/**
 * Created by trung on 04/02/15.
 */
public class Channel {

    public int mChannel;
    public ArrayList<Program> mPrograms;

    public Channel(int channel, ArrayList<Program> programs) {
        mChannel = channel;
        mPrograms = programs;

        for (Program program : mPrograms) {
            program.channel = channel;
        }
    }

}
