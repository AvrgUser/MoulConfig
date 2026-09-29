package io.github.notenoughupdates.moulconfig.test;

import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;

public class TestConfig extends Config {
    @Override
    public Component getTitle() {
        return Component.literal("1.20 Test").withColor(TextColor.GREEN);
    }

    @Override
    public boolean isValidRunnable(int runnableId) {
        return false;
    }

    @Category(name = "Cat a", desc = "Cat a desc")
    public TestCategoryA testCategoryA = new TestCategoryA();

    @Category(name = "Cat b", desc = "Cat b desc")
    public TestCategoryB testCategoryB = new TestCategoryB();

    public TestCategoryA getTestCategoryA() {
        return testCategoryA;
    }

    public TestCategoryB getTestCategoryB() {
        return testCategoryB;
    }
}
