package demo;


/**
 * Created by cuong on 10/22/2015.
 */
public class DemoJSONParser {

    public static void main(String args[]) {
        String input = "[[[\"xin chào\",\"hello\",,,0]],,\"en\"]";
        int index = input.indexOf("\"");
        int index2 = input.indexOf("\"", index+1);

        if (index != -1 && index2 != -1) {
            System.out.println(input.substring(index+1, index2));
        }
    }


}
