package com.uptosmth.chronos.jetbrains.settings;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

public class ChronosSettingsComponent {
    private final JPanel mainPanel;
    private final JBTextField urlTextField = new JBTextField();
    private final JBTextField flushingPeriodSecondTextField = new JBTextField();

    public ChronosSettingsComponent() {
        mainPanel =
                FormBuilder.createFormBuilder()
                        .addLabeledComponent(new JBLabel("URL: "), urlTextField, 1, false)
                        .addLabeledComponent(
                                new JBLabel("Flushing period (s): "),
                                flushingPeriodSecondTextField,
                                1,
                                false)
                        .addComponentFillVertically(new JPanel(), 0)
                        .getPanel();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return urlTextField;
    }

    @NotNull
    public String getUrlText() {
        return urlTextField.getText();
    }

    public void setUrlText(@NotNull String newText) {
        urlTextField.setText(newText);
    }

    public int getFlushingPeriodSecond() {
        return Integer.parseInt(flushingPeriodSecondTextField.getText());
    }

    public void setFlushingPeriodSecond(int flushingPeriodSecond) {
        flushingPeriodSecondTextField.setText(Integer.toString(flushingPeriodSecond));
    }
}
