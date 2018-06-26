package edu.wpi.cibr.oakyildiz.cibr_vr_gui;

import java.util.StringTokenizer;


//TODO: a string parser cpable of registering synonyms, "one_of"s and a dictionary of values to synonyms
// an injector method for synonyms and result  values
class Synonnym{
    public String label;
    private String values[];


}

class StringCommandParser {

    StringTokenizer st;

    public StringCommandParser(){}

    public void parse(String command){


        st = new StringTokenizer(command);

    }

}
