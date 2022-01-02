package com.uptosmth.chronos.jetbrains.listeners;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.uptosmth.chronos.jetbrains.ChronosService;
import com.uptosmth.chronos.jetbrains.HeartbeatSource;

public class ChronosEditorMouseListener implements EditorMouseListener {
    @Override
    public void mousePressed(@NotNull EditorMouseEvent event) {
        ChronosService service = ChronosService.getInstance();

        Document document = event.getEditor().getDocument();

        Optional<Project> optionalProject = service.getDocumentProject(document);
        Optional<VirtualFile> optionalFile = service.getDocumentFile(document);

        if (optionalProject.isPresent() && optionalFile.isPresent()) {
            service.addHeartbeat(HeartbeatSource.MOUSE, optionalFile.get(), optionalProject.get());
        }
    }

    @Override
    public void mouseClicked(@NotNull EditorMouseEvent event) {}

    @Override
    public void mouseReleased(@NotNull EditorMouseEvent event) {}

    @Override
    public void mouseEntered(@NotNull EditorMouseEvent event) {}

    @Override
    public void mouseExited(@NotNull EditorMouseEvent event) {}
}
