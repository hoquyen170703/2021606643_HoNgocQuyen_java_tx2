package tx1;

import java.util.List;

public interface TVManager {
    boolean addTV(TV t);

    boolean editTV(TV t);

    boolean delTV(TV t);

    List<TV> searchTV(String name);

    List<TV> sortedTV(double price, int choice);

}


