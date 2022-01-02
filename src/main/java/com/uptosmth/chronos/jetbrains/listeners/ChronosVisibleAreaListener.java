package com.uptosmth.chronos.jetbrains.listeners;

import java.awt.*;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.VisibleAreaEvent;
import com.intellij.openapi.editor.event.VisibleAreaListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.uptosmth.chronos.jetbrains.ChronosService;
import com.uptosmth.chronos.jetbrains.HeartbeatSource;

public class ChronosVisibleAreaListener implements VisibleAreaListener {
    @Override
    public void visibleAreaChanged(@NotNull VisibleAreaEvent event) {
        Document document = event.getEditor().getDocument();

        if (isScrolled(event)) {
            ChronosService service = ChronosService.getInstance();

            Optional<Project> optionalProject = service.getDocumentProject(document);
            Optional<VirtualFile> optionalFile = service.getDocumentFile(document);

            if (optionalProject.isPresent() && optionalFile.isPresent()) {
                service.addHeartbeat(
                        HeartbeatSource.AREA, optionalFile.get(), optionalProject.get());
            }
        }
    }

    private boolean isScrolled(VisibleAreaEvent event) {
        Rectangle newRectangle = event.getNewRectangle();
        Rectangle oldRectangle = event.getOldRectangle();

        if (oldRectangle == null) return false;

        if (newRectangle.x != oldRectangle.x || newRectangle.y != oldRectangle.y) {
            return true;
        }

        return false;
    }
}
