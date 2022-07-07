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
    @DisplayName ("process exit and stops")
    void test_1() {
        when(io.read()).thenReturn("exit");

        Manager m = mock(Manager.class);
        m.in = io;

        m.run();
    }

    @Test
    @DisplayName ("process read then exit and stops")
    void test_2() {
        when(io.read()).thenReturn("read \"Solo Leveling\" 5").thenReturn("exit");

        Manager m = new Manager();
        m.in = io;
        m.el = new EntryList();
        m.el.add(new String[] { "Solo Leveling", "2" });

        m.run();

        assertThat(m.el.get("Solo Leveling").readto()).isEqualTo(7);
    }

    @Test
    @DisplayName ("process readto then exit and stops")
    void test_3() {
        when(io.read()).thenReturn("readto \"Solo Leveling\" 5").thenReturn("exit");

        Manager m = new Manager();
        m.in = io;
        m.el = new EntryList();
        m.el.add(new String[] { "Solo Leveling", "2" });

        m.run();

        assertThat(m.el.get("Solo Leveling").readto()).isEqualTo(5);
    }
}