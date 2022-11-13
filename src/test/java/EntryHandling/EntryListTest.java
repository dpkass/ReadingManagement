package EntryHandling;

import EntryHandling.Entry.EntryList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName ("The EntryList should ")
class EntryListTest {
    EntryList el;

    @BeforeEach
    private void pre() {
        el = new EntryList();
    }

    @Test
    @DisplayName ("get Entries by Name")
    void test_1() {
        el.add(List.of("Solo Leveling"));
        el.add(List.of("The Beginning after the End"));
        el.add(List.of("Tales of Demons and Gods"));

        assertThat(el.get("Solo Leveling")).isNotNull();
        assertThat(el.get("The Beginning after the End")).isNotNull();
        assertThat(el.get("Tales of Demons and Gods")).isNotNull();
    }

    @Test
    @DisplayName ("get Entries by Acronym")
    void test_2() {
        el.add(List.of("Solo Leveling", "0", "null", "SL", "sl"));
        el.add(List.of("The Beginning after the End", "0", "null", "TBATE", "tbate"));

        assertThat(el.get("SL").name()).isEqualTo("Solo Leveling");
        assertThat(el.get("sl").name()).isEqualTo("Solo Leveling");
        assertThat(el.get("tbate").name()).isEqualTo("The Beginning after the End");
    }

    @Test
    @DisplayName ("not find non existent Entries")
    void test_3() {
        el.add(List.of("Solo Leveling", "0", "null", "SL", "sl"));
        el.add(List.of("The Beginning after the End", "0", "null", "TBATE", "tbate"));
        el.add(List.of("Tales of Demons and Gods"));

        assertThat(el.get("sth")).isNull();
        assertThat(el.get("sth")).isNull();
    }
}