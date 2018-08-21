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

        System.out.println("test");
        return new DiscordReporter.Builder()
                .hook("481484161278803968", "P4Hw9PoyaDf1_dMDr4Km0lsxLq2vkN8IFPMFJKTh6ok8Pk4jbaNFKaz7hsV5y2T6GT9G")
                .focusOn(this)
                .build();
    }
}
