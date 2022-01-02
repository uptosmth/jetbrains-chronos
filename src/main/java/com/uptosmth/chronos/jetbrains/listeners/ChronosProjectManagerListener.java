package com.uptosmth.chronos.jetbrains.listeners;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;

public class ChronosProjectManagerListener implements ProjectManagerListener {

    @Override
    public void projectOpened(@NotNull Project project) {}

    @Override
    public void projectClosed(@NotNull Project project) {}
}
