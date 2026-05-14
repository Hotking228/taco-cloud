package tacos.config;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

public class Splitter {

    public Collection<String> split(String s){
        return Arrays.asList(s.split(","));
    }
}
