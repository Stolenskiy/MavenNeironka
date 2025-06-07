import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TemplateColumnsTest {
    @Test
    void testTemplateUsesFourColumns() throws IOException {
        String html = Files.readString(Paths.get("src/main/resources/templates/index.html"));
        assertTrue(html.contains("col-md-3"));
        assertFalse(html.contains("col-md-6"));
    }
}
