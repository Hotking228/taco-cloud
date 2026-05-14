package tacos.config;

import java.util.Arrays;
import java.util.Collection;

public class OrderSplitter {

    public Collection<Object> splitOrder(String str){
        return Arrays.asList(str.split(","));
    }
}
