package Management;

import EntryHandling.EntryList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName ("The Manager should ")
public class ProcessorTest {

    @Test
    @DisplayName ("process exit and stop")
    void test_1() {
        Manager m = mock(Manager.class);

        m.process("exit");
    }

    @Test
    @DisplayName ("process \"new\" and a new Entry")
    void test_2() {
        Manager m = new Manager();
        m.el = new EntryList();

        m.process("new \"Solo Leveling\"");

        assertThat(m.el.get("Solo Leveling")).isNotNull();
    }

    @Test
    @DisplayName ("process \"read\" and change the read-to")
    void test_3() {
        Manager m = new Manager();
        m.el = new EntryList();
        m.el.add(new String[] { "Solo Leveling", "2" });

        m.process("read \"Solo Leveling\" 5");

        assertThat(m.el.get("Solo Leveling").readto()).isEqualTo(7);
    }

    @Test
    @DisplayName ("process \"read-to\" and change the read to")
    void test_4() {
        Manager m = new Manager();
        m.el = new EntryList();
        m.el.add(new String[] { "Solo Leveling", "2" });

        m.process("read-to \"Solo Leveling\" 5");

        assertThat(m.el.get("Solo Leveling").readto()).isEqualTo(5);
    }

    @Test
    @DisplayName ("process \"add acronym\" and add the acronym")
    void test_5() {
        Manager m = new Manager();
        m.el = new EntryList();
        m.el.add(new String[] { "Solo Leveling" });

        m.process("add acronym \"Solo Leveling\" SL");

        assertThat(m.el.get("Solo Leveling").hasAcronym("SL")).isTrue();
    }

    @Test
    @DisplayName ("process \"change link\" and change the link")
    void test_6() {
        Manager m = new Manager();
        m.el = new EntryList();
        m.el.add(new String[] { "Solo Leveling" });

        m.process("change link \"Solo Leveling\" www.somelink.com");

        assertThat(m.el.get("Solo Leveling").link()).isEqualTo("www.somelink.com");
    }
}