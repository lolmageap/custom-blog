package checkedexception;

import java.io.*;

public class CheckedException {
    public static void main(String[] args) {
        File file = new File("test.txt");

        try(FileReader fileReader = new FileReader(file)) {
            BufferedReader reader = new BufferedReader(fileReader);
            reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}