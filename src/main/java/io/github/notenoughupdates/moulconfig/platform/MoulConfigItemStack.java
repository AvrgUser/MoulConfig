package io.github.notenoughupdates.moulconfig.platform;

import io.github.notenoughupdates.moulconfig.common.IItemStack;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import lombok.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@Value
@NullMarked
public class MoulConfigItemStack implements IItemStack {
    ItemStack itemStack;

    @Override
    public List<Component> getLore() {
        return itemStack.getTooltipLines(Item.TooltipContext.EMPTY, Minecraft.getInstance().player, TooltipFlag.NORMAL)
            .stream()
            .map(MoulConfigPlatform::wrap)
            .toList();
    }

    @Override
    public Component getDisplayName() {
        return MoulConfigPlatform.wrap(itemStack.getStyledHoverName());
    }

    @Override
    public int getStackSize() {
        return itemStack.getCount();
    }

    @Override
    public Identifier getItemId() {
        return MoulConfigPlatform.wrap(BuiltInRegistries.ITEM.getKey(itemStack.getItem()));
    }
}
