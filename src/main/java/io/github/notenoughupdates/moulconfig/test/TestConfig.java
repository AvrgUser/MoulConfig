package io.github.notenoughupdates.moulconfig.test;

import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.Social;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import java.util.ArrayList;
import java.util.List;

public class TestConfig extends Config {
    @Override
    public Component getTitle() {
        return Component.literal("Test").withStyle(ChatFormatting.GREEN);
    }

    @Override
    public boolean isValidRunnable(int runnableId) {
        return false;
    }

    @Override
    public List<Social> getSocials() {
        return List.of(
            Social.forLink(
                Component.literal("GitHub"),
                // Why is this delete.png? Because I don't want to include a github icon in the resources
                Identifier.fromNamespaceAndPath("moulconfig", "delete.png"),
                "https://github.com/AvrgUser/MoulConfig/"
            )
        );
    }

    @Category(name = "Cat a", desc = "Cat a desc")
    public TestCategoryA testCategoryA = new TestCategoryA();

    @Category(name = "Cat b", desc = "Cat b desc")
    public TestCategoryB testCategoryB = new TestCategoryB();
}
