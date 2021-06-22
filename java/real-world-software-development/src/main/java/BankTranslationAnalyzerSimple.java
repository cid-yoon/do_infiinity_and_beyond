import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BankTranslationAnalyzerSimple {
    private static final String RESOURCE = "src/main/resources/";

    public static void main(String[] args) throws IOException {

        final String fileName = "bank.csv";
        final Path path = Paths.get(RESOURCE + fileName);
        final List<String> lines = Files.readAllLines(path);
        double total = 0d;
        for (final String line : lines) {
            final String[] column = line.split(",");
            final double amount = Double.parseDouble(column[1]);
            total += amount;

        }
        System.out.println("The total for all transactions is " + total);

    }
}
