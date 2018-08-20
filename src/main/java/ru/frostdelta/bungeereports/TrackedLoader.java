package ru.frostdelta.bungeereports;

import ru.endlesscode.inspector.api.report.DiscordReporter;
import ru.endlesscode.inspector.api.report.Reporter;
import ru.endlesscode.inspector.bukkit.plugin.TrackedPlugin;

public class TrackedLoader extends TrackedPlugin {

    public TrackedLoader() {
        super(Loader.class);
    }

    @Override
    protected Reporter createReporter() {
        // Здесь необходимо изменить данные для использования вебхука
        return new DiscordReporter.Builder()
                .hook("<WEBHOOK_ID_HERE>", "<WEBHOOK_TOKEN_HERE>")
                .focusOn(this)
                .build();
    }
}
