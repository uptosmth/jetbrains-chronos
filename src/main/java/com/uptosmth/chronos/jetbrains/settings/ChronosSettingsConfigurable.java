package com.uptosmth.chronos.jetbrains.settings;

import javax.swing.*;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.options.Configurable;

public class ChronosSettingsConfigurable implements Configurable {
    private ChronosSettingsComponent chronosSettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Chronos";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return chronosSettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        chronosSettingsComponent = new ChronosSettingsComponent();
        return chronosSettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        ChronosSettings settings = ChronosSettings.getInstance();
        boolean modified = !chronosSettingsComponent.getUrlText().equals(settings.url);
        modified |= chronosSettingsComponent.getFlushingPeriodSecond() != settings.flushingPeriodSecond;
        return modified;
    }

    @Override
    public void apply() {
        ChronosSettings settings = ChronosSettings.getInstance();
        settings.url = chronosSettingsComponent.getUrlText();
        settings.flushingPeriodSecond = chronosSettingsComponent.getFlushingPeriodSecond();
    }

    @Override
    public void reset() {
        ChronosSettings settings = ChronosSettings.getInstance();
        chronosSettingsComponent.setUrlText(settings.url);
        chronosSettingsComponent.setFlushingPeriodSecond(settings.flushingPeriodSecond);
    }

    @Override
    public void disposeUIResources() {
        chronosSettingsComponent = null;
    }
}
