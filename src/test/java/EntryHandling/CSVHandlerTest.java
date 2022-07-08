package EntryHandling;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName ("The Handler ")
class CSVHandlerTest {

    @Test
    @DisplayName ("reads the File correctly")
    void test_1() throws IOException {
        File f = new File("src/test/resources/test1");
        f.createNewFile();

        FileHandler handler = new CSVHandler(f);
        EntryList e = handler.read();

        assertThat(e.list.get(0).name()).isEqualTo("Solo Leveling");
        assertThat(e.list.get(0).readto()).isEqualTo(3);
        assertThat(e.list.get(0).link()).isEqualTo("www.somelink.com");

        assertThat(e.list.get(1).name()).isEqualTo("The Beginning after the End");
        assertThat(e.list.get(1).readto()).isEqualTo(162);
        assertThat(e.list.get(1).link()).isEqualTo("www.someotherlink.com");
    }

    @Test
    @DisplayName ("writes the File correctly")
    void test_2() throws IOException {
        File f = new File("src/test/resources/test2");

        EntryList el = new EntryList();
        el.add(new String[] { "Solo Leveling", "3", "www.somelink.com" });
        el.add(new String[] { "The Beginning after the End", "162", "www.someotherlink.com" });

        FileHandler handler = new CSVHandler(f);
        handler.write(el);

        BufferedReader br = new BufferedReader(new FileReader(f));

        assertThat(br.readLine()).isEqualTo("Solo Leveling" + CSVHandler.standardSeparator + " 3" + CSVHandler.standardSeparator + " www" +
                ".somelink.com" + CSVHandler.standardSeparator + ' ');
        assertThat(br.readLine()).isEqualTo("The Beginning after the End" + CSVHandler.standardSeparator + " 162" + CSVHandler.standardSeparator + " www.someotherlink.com" + CSVHandler.standardSeparator + ' ');

        br.close();
    }
}