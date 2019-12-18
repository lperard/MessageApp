import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;

public class FileManager {

  public static void main(String[] args) {

    String message = "Hello World!\n";
    byte data[] = message.getBytes();
    Path chemin = Paths.get("../logs/log1.txt");

    try (OutputStream out = new BufferedOutputStream(
      Files.newOutputStream(chemin, CREATE, APPEND))) {
      out.write(data, 0, data.length);
    } catch (IOException x) {
      System.err.println(x);
    }
  }
}