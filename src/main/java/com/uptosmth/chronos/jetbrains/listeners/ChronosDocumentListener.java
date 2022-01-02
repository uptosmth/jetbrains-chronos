package com.uptosmth.chronos.jetbrains.listeners;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.uptosmth.chronos.jetbrains.ChronosService;
import com.uptosmth.chronos.jetbrains.HeartbeatSource;

public class ChronosDocumentListener implements DocumentListener {
    @Override
    public void beforeDocumentChange(@NotNull DocumentEvent event) {}

    @Override
    public void documentChanged(@NotNull DocumentEvent event) {
        Document document = event.getDocument();

        ChronosService service = ChronosService.getInstance();

        Optional<Project> optionalProject = service.getDocumentProject(document);
        Optional<VirtualFile> optionalFile = service.getDocumentFile(document);

        if (optionalProject.isPresent() && optionalFile.isPresent()) {
            service.addHeartbeat(
                    HeartbeatSource.DOCUMENT, optionalFile.get(), optionalProject.get());
        }
    }

    @Override
    public void bulkUpdateStarting(@NotNull Document document) {}

    @Override
    public void bulkUpdateFinished(@NotNull Document document) {}
}
