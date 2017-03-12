package utils;

/**
 * Created by hassina on 12/03/2017.
 */
public class Pair {

    private String key, value;

    public Pair(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String toString(){
        return this.value;
    }

    public String getKey(){
        return this.key;
    }

    public String getValue(){
        return this.value;
    }
}


