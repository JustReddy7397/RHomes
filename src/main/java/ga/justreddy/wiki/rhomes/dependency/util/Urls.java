package ga.justreddy.wiki.rhomes.dependency.util;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import ga.justreddy.wiki.rhomes.dependency.DLoader;
import ga.justreddy.wiki.rhomes.dependency.base.Dependency;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("WeakerAccess")
public final class Urls {

    private static final List<String> REPOSITORIES = new ArrayList<>();

    static {
        REPOSITORIES.add("https://repo1.maven.org/maven2/");
        REPOSITORIES.add("https://repo.extendedclip.com/content/repositories/placeholderapi/");
    }

    public static void addRepositories(@NotNull List<String> repositories) {
        REPOSITORIES.addAll(repositories.stream().map(Urls::fixUrl).collect(Collectors.toList()));
    }

    public static void addRepositories(@NotNull String... repositories) {
        addRepositories(Arrays.asList(repositories));
    }

    private Urls() {}

    @NotNull
    public static String getBaseUrl(@NotNull Dependency dependency) {
        return dependency.getGroupId().replace('.', '/') + '/' + dependency.getArtifactId() + '/' + dependency.getVersion() + '/';
    }

    @NotNull
    public static String getJarUrl(@NotNull Dependency dependency) {
        return getBaseUrl(dependency) + dependency.getJarName();
    }

    @NotNull
    public static String getPomUrl(@NotNull Dependency dependency) {
        return getBaseUrl(dependency) + dependency.getPomName();
    }

    @NotNull
    public static String getMetaUrl(@NotNull Dependency dependency) {
        return getBaseUrl(dependency) + "maven-metadata.xml";
    }

    @NotNull
    public static String fixUrl(@NotNull String original) {
        return original.isEmpty() || original.endsWith("/") ? original : original + '/';
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void download(@NotNull Dependency dependency, @NotNull File folder, @NotNull BiConsumer<File, File> whenDone) {
        final File jarFile = new File(folder, dependency.getJarName()), pomFile = new File(folder, dependency.getPomName());

        boolean alwaysUpdate = dependency.getOptions().isAlwaysUpdate(), isSnapShot = dependency.getVersion().endsWith("-SNAPSHOT");

        if (jarFile.exists() && !isSnapShot && !alwaysUpdate) {
            whenDone.accept(jarFile, pomFile);
            return;
        }

        if (!folder.exists()) folder.mkdirs();

        try {

            final String pomUrl, jarUrl;
            final String customRepo = dependency.getOptions().getCustomRepository();

            if (isSnapShot) {
                final File metaFile = new File(folder, "meta.xml");
                tryDownload(getMetaUrl(dependency), metaFile, customRepo);

                final String latestSnapShot = Xmls.readLatestSnapshot(dependency, metaFile);
                final String latestFileName = dependency.getArtifactId() + "-" + latestSnapShot;

                final File latestFile = new File(folder, latestFileName);
                if (latestFile.exists() && !alwaysUpdate) {
                    whenDone.accept(jarFile, pomFile);
                    return;
                } else {
                    if (pomFile.exists()) FileUtils.forceDelete(pomFile);
                    if (jarFile.exists()) FileUtils.forceDelete(jarFile);
                }

                pomUrl = getBaseUrl(dependency) + latestFileName + ".pom";
                jarUrl = getBaseUrl(dependency) + latestFileName + ".jar";

                latestFile.createNewFile();
            } else {
                pomUrl = getPomUrl(dependency);
                jarUrl = getJarUrl(dependency);
            }

            tryDownload(pomUrl, pomFile, customRepo);
            tryDownload(jarUrl, jarFile, customRepo);

            whenDone.accept(jarFile, pomFile);
        } catch (Exception e) {
            e.printStackTrace();
           // DLoader.log(Level.SEVERE, "Failed to download dependency " + dependency.getName());
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.translateAlternateColorCodes('&',
                            "[RHomes] &cFailed to download the dependency " + dependency.getName())
            );
        }
    }


    private static void tryDownload(@NotNull String fileUrl, @NotNull File file, @NotNull String... customUrl) throws Exception {
        DLoader.debug("Attempting to download " + fileUrl);

        if (customUrl.length > 0 && !customUrl[0].isEmpty()) {
            openStream(customUrl[0] + fileUrl, (url, stream) -> pullFromStreamToFile(stream, url, file));
            return;
        }

        for (String url : REPOSITORIES) {
            final String actualUrl = url + fileUrl;
            DLoader.debug("URL is '" + actualUrl + "'");
            try {
                openStream(actualUrl, (fUrl, stream) -> pullFromStreamToFile(stream, fUrl, file));
                return;
            } catch (IOException e) {
                DLoader.log(Level.WARNING, "Failed to download from repo '" + url + "'");
            }
        }

        DLoader.log(Level.SEVERE, "Failed to download " + fileUrl);
    }

    private static void openStream(@NotNull String url, @NotNull BiConsumer<String, InputStream> block) throws IOException {
        try (InputStream stream = new URL(url).openStream()) {
            block.accept(url, stream);
        }
    }

    private static void pullFromStreamToFile(@NotNull InputStream stream, @NotNull String url, @NotNull File file) {
        try {
            FileUtils.copyInputStreamToFile(stream, file);

            if (!file.getName().endsWith(".jar") || !DLoader.isEnforcingFileCheck()) return;

            openStream(url + ".sha1", (shaUrl, shaStream) -> {

                try {
                    final String mavenSha1 = IOUtils.toString(shaStream);
                    final String fileSha1  = Files.hash(file, Hashing.sha1()).toString();

                    DLoader.debug("Maven SHA-1: " + mavenSha1, "File SHA-1: " + fileSha1);

                    if (!mavenSha1.equals(fileSha1)) {
                        FileUtils.forceDelete(file);
                        throw new IllegalStateException("Failed to validate downloaded file " + file.getName());
                    }

                    DLoader.debug("File " + file.getName() + " passed validation");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
          //  DLoader.log(Level.SEVERE, "Failed to download url to file " + file.getName());
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.translateAlternateColorCodes('&',
                            "[RHomes] &cFailed to download url to file  " + file.getName())
            );
        }
    }

}
