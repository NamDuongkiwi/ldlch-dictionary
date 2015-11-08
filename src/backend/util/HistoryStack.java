package backend.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by cuong on 10/30/2015.
 */
public class HistoryStack extends Stack<Pair> {
    private List<Pair> data;

    public HistoryStack() {
        data = new ArrayList<>();
    }

    public Pair push(Pair p) {
        data.add(p);
        return p;
    }

    public Pair pop() {
        return (data.isEmpty()) ? null : data.remove(data.size()-1);
    }

    public Pair peek() {
        return (data.isEmpty()) ? null : data.get(data.size() - 1);
    }
}
