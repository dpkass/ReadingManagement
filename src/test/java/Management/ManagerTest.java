package Management;

import EntryHandling.EntryList;
import IOHandling.IOHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName ("The Manager should ")
class ManagerTest {
    IOHandler io;

    @BeforeEach
    void pre() {
        io = mock(IOHandler.class);
    }

    @Test
    @DisplayName ("process exit and stop")
    void test_1() {
        when(io.read()).thenReturn("exit");

        Manager m = mock(Manager.class);
        m.io = io;

        m.run();
    }

    @Test
    @DisplayName ("process \"new\" then exit and stop")
    void test_2() {
        when(io.read()).thenReturn("new \"Solo Leveling\"").thenReturn("exit");

        Manager m = new Manager();
        m.io = io;
        m.el = new EntryList();

        m.run();

        assertThat(m.el.get("Solo Leveling")).isNotNull();
    }

    @Test
    @DisplayName ("process \"read\" then exit and stop")
    void test_3() {
        when(io.read()).thenReturn("read \"Solo Leveling\" 5").thenReturn("exit");

        Manager m = new Manager();
        m.io = io;
        m.el = new EntryList();
        m.el.add(new String[] { "Solo Leveling", "2" });

        m.run();

        assertThat(m.el.get("Solo Leveling").readto()).isEqualTo(7);
    }

    @Test
    @DisplayName ("process \"readto\" then exit and stop")
    void test_4() {
        when(io.read()).thenReturn("readto \"Solo Leveling\" 5").thenReturn("exit");

        Manager m = new Manager();
        m.io = io;
        m.el = new EntryList();
        m.el.add(new String[] { "Solo Leveling", "2" });

        m.run();

        assertThat(m.el.get("Solo Leveling").readto()).isEqualTo(5);
    }

    @Test
    @DisplayName ("process \"add acronym\" then exit and stop")
    void test_5() {
        when(io.read()).thenReturn("add acronym \"Solo Leveling\" SL").thenReturn("exit");

        Manager m = new Manager();
        m.io = io;
        m.el = new EntryList();
        m.el.add(new String[] { "Solo Leveling" });

        m.run();

        assertThat(m.el.get("Solo Leveling").hasAcronym("SL")).isTrue();
    }

    @Test
    @DisplayName ("process \"add link\" then exit and stop")
    void test_6() {
        when(io.read()).thenReturn("add link \"Solo Leveling\" www.somelink.com").thenReturn("exit");

        Manager m = new Manager();
        m.io = io;
        m.el = new EntryList();
        m.el.add(new String[] { "Solo Leveling" });

        m.run();

        assertThat(m.el.get("Solo Leveling").link()).isEqualTo("www.somelink.com");
    }
}