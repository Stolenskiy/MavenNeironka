import nic.com.Diplomka.controller.IndexViewController;
import nic.com.Diplomka.service.FavoriteService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SaveFavoritesUniqueNamesTest {
    @Test
    void testUniqueNamesWhenSavingFavorites() throws IOException {
        FavoriteService service = new FavoriteService();
        IndexViewController controller = new IndexViewController(service);
        Path favDir = Paths.get("src/main/resources/favorite_images");
        if (Files.exists(favDir)) {
            Files.walk(favDir)
                    .filter(Files::isRegularFile)
                    .forEach(p -> {
                        try { Files.delete(p); } catch (IOException ignored) {}
                    });
        } else {
            Files.createDirectories(favDir);
        }

        String imgPath = "image/0_hsb.png";
        controller.saveFavorites(List.of(imgPath));
        controller.saveFavorites(List.of(imgPath));

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(favDir, "*.png")) {
            int fileCount = 0;
            for (Path p : stream) {
                fileCount++;
                String name = p.getFileName().toString();
                assertTrue(name.startsWith("0_hsb_") && name.endsWith(".png"));
            }
            assertEquals(2, fileCount);
        }
    }
}
