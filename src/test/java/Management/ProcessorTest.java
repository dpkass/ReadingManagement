package Management;

import EntryHandling.Entry.EntryList;
import EntryHandling.Entry.EntryUtil;
import IOHandling.IOHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName ("The ProcessStarter should ")
public class ProcessorTest {

    @Test
    @DisplayName ("process exit and stop")
    void test_1() {
        ProcessStarter p = mock(ProcessStarter.class);

        assertThat(p.process("exit")).isFalse();
    }

    @Test
    @DisplayName ("process \"new\" and a new Entry")
    void test_2() {
        ProcessStarter p = new ProcessStarter(new EntryList(), null, mock(IOHandler.class));

        p.process("new \"Solo Leveling\"");

        assertThat(p.el.get("Solo Leveling")).isNotNull();
    }

    @Test
    @DisplayName ("process \"read\" and change the read-to")
    void test_3() {
        ProcessStarter p = new ProcessStarter(new EntryList(), null, mock(IOHandler.class));
        p.el.add(new String[] { "Solo Leveling", "2" });

        p.process("read \"Solo Leveling\" 5");

        assertThat(p.el.get("Solo Leveling").readto()).isEqualTo("7.0");
    }

    @Test
    @DisplayName ("process \"read-to\" and change the read to")
    void test_4() {
        ProcessStarter p = new ProcessStarter(new EntryList(), null, mock(IOHandler.class));
        p.el.add(new String[] { "Solo Leveling", "2" });

        p.process("read-to \"Solo Leveling\" 5");

        assertThat(p.el.get("Solo Leveling").readto()).isEqualTo("5");
    }

    @Test
    @DisplayName ("process \"add acronym\" and add the acronym")
    void test_5() {
        ProcessStarter p = new ProcessStarter(new EntryList(), null, mock(IOHandler.class));
        p.el.add(new String[] { "Solo Leveling" });

        p.process("add acronym \"Solo Leveling\" SL");

        assertThat(EntryUtil.hasAcronym(p.el.get("Solo Leveling"), "SL")).isTrue();
    }

    @Test
    @DisplayName ("process \"change link\" and change the link")
    void test_6() {
        ProcessStarter p = new ProcessStarter(new EntryList(), null, mock(IOHandler.class));
        p.el.add(new String[] { "Solo Leveling" });

        p.process("change link \"Solo Leveling\" www.somelink.com");

        assertThat(p.el.get("Solo Leveling").link()).isEqualTo("www.somelink.com");
    }
}