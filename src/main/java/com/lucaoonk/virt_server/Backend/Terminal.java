package com.lucaoonk.virt_server.Backend;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Terminal {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    
    public enum Color {
        ANSI_BLACK, ANSI_RED, ANSI_GREEN, ANSI_YELLOW,
        ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN,  ANSI_WHITE
    }

    public static String colorText(String text, String color){

        if(color.equals(ANSI_BLACK)){
            return ANSI_BLACK+text+ANSI_RESET;
        }
        if(color.equals(ANSI_RED)){
            return ANSI_RED+text+ANSI_RESET;

        }
        if(color.equals(ANSI_GREEN)){
            return ANSI_GREEN+text+ANSI_RESET;

        }
        if(color.equals(ANSI_YELLOW)){
            return ANSI_YELLOW+text+ANSI_RESET;

        }
        if(color.equals(ANSI_BLUE)){
            return ANSI_BLUE+text+ANSI_RESET;

        }
        if(color.equals(ANSI_PURPLE)){
            return ANSI_PURPLE+text+ANSI_RESET;

        }
        if(color.equals(ANSI_CYAN)){
            return ANSI_CYAN+text+ANSI_RESET;

        }
        if(color.equals(ANSI_WHITE)){
            return ANSI_WHITE+text+ANSI_RESET;

        }

        return text;


    }

    public static String getTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        return dtf.format(now);  
    }
}
