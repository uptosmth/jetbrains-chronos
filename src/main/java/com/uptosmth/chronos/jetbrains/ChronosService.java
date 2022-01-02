package com.uptosmth.chronos.jetbrains;

import java.awt.KeyboardFocusManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorEventMulticaster;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.uptosmth.chronos.jetbrains.listeners.ChronosDocumentListener;
import com.uptosmth.chronos.jetbrains.listeners.ChronosEditorMouseListener;
import com.uptosmth.chronos.jetbrains.listeners.ChronosVisibleAreaListener;
import com.uptosmth.chronos.jetbrains.settings.ChronosSettings;

public class ChronosService implements Disposable {
    private static final Logger log = Logger.getInstance(ChronosService.class);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> scheduled;

    private static final List<Heartbeat> heartbeats =
            Collections.synchronizedList(new ArrayList<>());

    private static int flushingPeriodSecond = 30;
    private static String chronosUrl;

    public ChronosService() {
        log.debug("Init service");

        loadSettings();

        ApplicationManager.getApplication()
                .invokeLater(
                        () -> {
                            EditorEventMulticaster mc =
                                    EditorFactory.getInstance().getEventMulticaster();

                            mc.addEditorMouseListener(
                                    new ChronosEditorMouseListener(), ChronosService.getInstance());

                            mc.addVisibleAreaListener(
                                    new ChronosVisibleAreaListener(), ChronosService.getInstance());

                            mc.addDocumentListener(
                                    new ChronosDocumentListener(), ChronosService.getInstance());

                            scheduled =
                                    scheduler.scheduleAtFixedRate(
                                            () -> ChronosService.getInstance().flushHeartbeats(),
                                            flushingPeriodSecond,
                                            flushingPeriodSecond,
                                            TimeUnit.SECONDS);
                        });
    }

    private void loadSettings() {
        chronosUrl = ChronosSettings.getInstance().url;
        flushingPeriodSecond = ChronosSettings.getInstance().flushingPeriodSecond;
    }

    public void addHeartbeat(HeartbeatSource source, VirtualFile file, Project project) {
        if (isInFocus()) {
            Instant now = Instant.now();

            if (heartbeats.size() > 0) {
                Heartbeat lastHeartbeat = heartbeats.get(heartbeats.size() - 1);

                if (lastHeartbeat.getFile().equals(file.getPath())
                        && lastHeartbeat.getProject().equals(project.getName())) {
                    long elapsedMilli = now.toEpochMilli() - lastHeartbeat.getTimestamp();

                    if (elapsedMilli < 5_000) {
                        return;
                    }
                }
            }

            log.debug(
                    String.format(
                            "Heartbeat: %s %s %s", source, file.getPath(), project.getName()));

            heartbeats.add(new Heartbeat(now.toEpochMilli(), file.getPath(), project.getName()));
        }
    }

    private void flushHeartbeats() {
        if (heartbeats.size() > 0) {
            log.debug("Flushing heartbeats");

            List<String> json = new ArrayList<>();

            synchronized (heartbeats) {
                for (Heartbeat heartbeat : heartbeats) {
                    json.add(heartbeat.toJsonString());
                }

                heartbeats.clear();
            }

            String body = "[" + String.join(",", json) + "]";

            try {
                httpPost(chronosUrl, body);
            } catch (Exception e) {
                log.error("Error flushing: " + e.getMessage(), e);

                ChronosNotifier.notifyError(null, "Error flushing heartbeats, check chronos url configuration");
            }
        }
    }

    public static String httpPost(String targetUrl, String body) throws IOException {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(targetUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setRequestProperty(
                    "Content-Length", Integer.toString(body.getBytes().length));

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(body);
            wr.close();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            return response.toString();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public boolean isInFocus() {
        return KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow() != null;
    }

    public Optional<Project> getDocumentProject(Document document) {
        Editor[] editors = EditorFactory.getInstance().getEditors(document);

        if (editors.length > 0) {
            Project project = editors[0].getProject();

            if (project != null && project.isInitialized()) {
                return Optional.of(project);
            }
        }

        return Optional.empty();
    }

    public Optional<VirtualFile> getDocumentFile(Document document) {
        if (document == null) return Optional.empty();

        FileDocumentManager instance = FileDocumentManager.getInstance();

        VirtualFile file = instance.getFile(document);
        if (file == null) return Optional.empty();

        return Optional.of(file);
    }

    public static ChronosService getInstance() {
        return ApplicationManager.getApplication().getService(ChronosService.class);
    }

    @Override
    public void dispose() {}
}
