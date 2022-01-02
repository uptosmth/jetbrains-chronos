package com.uptosmth.chronos.jetbrains.listeners;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.uptosmth.chronos.jetbrains.ChronosService;

public class ChronosVfsListener implements BulkFileListener {
    @Override
    public void after(@NotNull List<? extends VFileEvent> events) {
        ChronosService service = ChronosService.getInstance();

        for (VFileEvent event : events) {
            //service.addHeartbeat(HeartbeatSource.FILE, event.getFile());
        }
    }
}
