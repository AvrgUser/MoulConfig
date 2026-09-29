package io.github.notenoughupdates.moulconfig.test;

import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import io.github.notenoughupdates.moulconfig.internal.Warnings;
import io.github.notenoughupdates.moulconfig.managed.ManagedConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import java.io.File;

import static io.github.notenoughupdates.moulconfig.test.CommandUtils.literal;

public class FabricMain implements ModInitializer {
    @Override
    public void onInitialize() {
        Warnings.shouldCrash = false;
        ManagedConfig<TestConfig> config = ManagedConfig.create(new File("config/moulconfig/test.json"), TestConfig.class);
        ClientCommandRegistrationCallback.EVENT.register((a, b) -> {
            a.register(literal("moulconfig").executes(ctx -> {
                Minecraft.getInstance().schedule(() -> {
                    var editor = config.getEditor();
                    editor.setWide(config.getInstance().getTestCategoryA().isWide());
                    IMinecraft.INSTANCE.openWrappedScreen(editor);
                });
                return 0;
            }));
        });
    }
}
