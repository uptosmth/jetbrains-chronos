package com.uptosmth.chronos.jetbrains.settings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;

@State(
    name = "com.uptosmth.chronos.jetbrains.settings.ChronosSettings",
    storages = @Storage("ChronosPlugin.xml")
)
public class ChronosSettings implements PersistentStateComponent<ChronosSettings> {
    public String url = "http://localhost:10203/heartbeats/editor";
    public int flushingPeriodSecond = 30;

    public static ChronosSettings getInstance() {
        return ApplicationManager.getApplication().getService(ChronosSettings.class);
    }

    @Nullable
    @Override
    public ChronosSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ChronosSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
