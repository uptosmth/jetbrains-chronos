package com.uptosmth.chronos.jetbrains;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public class ChronosNotifier {
    public static void notifyError(@Nullable Project project, String content) {
        NotificationGroupManager.getInstance().getNotificationGroup("notification")
                .createNotification(content, NotificationType.ERROR)
                .notify(project);
    }
}
