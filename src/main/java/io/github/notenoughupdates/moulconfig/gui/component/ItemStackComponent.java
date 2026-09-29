package io.github.notenoughupdates.moulconfig.gui.component;

import net.minecraft.network.chat.Component;
import io.github.notenoughupdates.moulconfig.gui.GuiComponent;
import io.github.notenoughupdates.moulconfig.gui.GuiImmediateContext;
import io.github.notenoughupdates.moulconfig.observer.GetSetter;
import net.minecraft.world.item.ItemStack;

public class ItemStackComponent extends GuiComponent {
    private final GetSetter<ItemStack> itemStack;

    public ItemStackComponent(GetSetter<ItemStack> itemStack) {
        this.itemStack = itemStack;
    }

    public GetSetter<ItemStack> getItemStack() {
        return itemStack;
    }

    @Override
    public int getWidth() {
        return 18;
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public void render(GuiImmediateContext context) {
        context.getRenderContext().renderItemStack(itemStack.get(), 1, 1, Component.empty());
    }
}
