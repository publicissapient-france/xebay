package fr.xebia.xebay.front.test;

import com.google.common.io.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.lang.String.format;

class PhantomJsDownloader {
    private final boolean isWindows;
    private final boolean isMac;
    private final boolean isLinux64;
    private final String version;

    PhantomJsDownloader() {
        String osName = System.getProperty("os.name");
        isWindows = osName.startsWith("Windows");
        isMac = osName.startsWith("Mac OS X") || osName.startsWith("Darwin");
        isLinux64 = System.getProperty("sun.arch.data.model").equals("64");
        version = "1.9.2";
    }

    public File downloadAndExtract() {
        File installDir = new File(new File(System.getProperty("user.home")), ".phantomjstest");

        String url;
        File phantomJsExe;
        if (isWindows) {
            url = format("https://phantomjs.googlecode.com/files/phantomjs-%s-windows.zip", version);
            phantomJsExe = new File(installDir, format("phantomjs-%s-windows/phantomjs.exe", version));
        } else if (isMac) {
            url = format("https://phantomjs.googlecode.com/files/phantomjs-%s-macosx.zip", version);
            phantomJsExe = new File(installDir, format("phantomjs-%s-macosx/bin/phantomjs", version));
        } else if (isLinux64) {
            url = format("https://phantomjs.googlecode.com/files/phantomjs-%s-linux-x86_64.tar.bz2", version);
            phantomJsExe = new File(installDir, format("phantomjs-%s-linux-x86_64/bin/phantomjs", version));
        } else {
            url = format("https://phantomjs.googlecode.com/files/phantomjs-%s-linux-i686.tar.bz2", version);
            phantomJsExe = new File(installDir, format("phantomjs-%s-linux-i686/bin/phantomjs", version));
        }

        extractExe(url, installDir, phantomJsExe);

        return phantomJsExe;
    }

    private void extractExe(String url, File phantomInstallDir, File phantomJsExe) {
        if (phantomJsExe.exists()) {
            return;
        }

        String zipFileName = format("phantomjs-%s.zip", version);
        File targetZip = new File(phantomInstallDir, zipFileName);
        downloadZip(url, targetZip);

        System.out.println("Extracting phantomjs...");
        try {
            if (isWindows) {
                unzip(targetZip, phantomInstallDir);
            } else if (isMac) {
                new ProcessBuilder().command("/usr/bin/unzip", "-qo", zipFileName).directory(phantomInstallDir).start().waitFor();
            } else {
                new ProcessBuilder().command("tar", "-xjvf", zipFileName).directory(phantomInstallDir).start().waitFor();
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unable to unzip phantomjs from " + targetZip.getAbsolutePath());
        }
    }

    private void downloadZip(String url, File targetZip) {
        if (targetZip.exists()) {
            return;
        }

        System.out.println("Downloading phantomjs from " + url + "...");

        File zipTemp = new File(targetZip.getAbsolutePath() + ".temp");
        try {
            zipTemp.getParentFile().mkdirs();

            InputSupplier<InputStream> input = Resources.newInputStreamSupplier(URI.create(url).toURL());
            OutputSupplier<FileOutputStream> ouput = Files.newOutputStreamSupplier(zipTemp);

            ByteStreams.copy(input, ouput);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to download phantomjs from " + url);
        }

        zipTemp.renameTo(targetZip);
    }

    private static void unzip(File zip, File toDir) throws IOException {
        try (ZipFile zipFile = new ZipFile(zip)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }

                File to = new File(toDir, entry.getName());
                to.getParentFile().mkdirs();

                Files.copy(new InputSupplier<InputStream>() {
                    @Override
                    public InputStream getInput() throws IOException {
                        return zipFile.getInputStream(entry);
                    }
                }, to);
            }
        }
    }
}
