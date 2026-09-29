package io.github.notenoughupdates.moulconfig.test;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class InnerCategory {
    @Expose
    @ConfigEditorBoolean
    @ConfigOption(name = "Test Option", desc = "Test toggle")
    public boolean shouldTestToggle = false;
}
