/*
 * Copyright (C) 2023 NotEnoughUpdates contributors
 *
 * This file is part of MoulConfig.
 *
 * MoulConfig is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * MoulConfig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MoulConfig. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package io.github.notenoughupdates.moulconfig;

import io.github.notenoughupdates.moulconfig.gui.HorizontalAlign;
import io.github.notenoughupdates.moulconfig.processor.ProcessedCategory;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import java.util.ArrayList;
import java.util.List;

public abstract class Config {
    @Deprecated
    public void executeRunnable(int runnableId) {
    }

    public HorizontalAlign alignCategory(ProcessedCategory category, boolean isSelected) {
        return HorizontalAlign.CENTER;
    }

    public Component formatCategoryName(ProcessedCategory category, boolean isSelected) {
        if (isSelected) {
            return category.getDisplayName().plainCopy()
                .setStyle(Style.EMPTY.withUnderlined(true).withColor(ChatFormatting.AQUA));
        } else if (category.getParentCategoryId() == null) {
            return category.getDisplayName().plainCopy()
                .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY));
        } else {
            return category.getDisplayName().plainCopy()
                .setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY));
        }
    }

    public List<Social> getSocials() {
        return new ArrayList<>();
    }

    public Component getTitle() {
        return Component.literal("Config GUI");
    }

    public void saveNow() {
        for (Runnable saveRunnable : saveRunnables) {
            saveRunnable.run();
        }
    }

    public DescriptionRendereringBehaviour getDescriptionBehaviour(ProcessedOption option) {
        return DescriptionRendereringBehaviour.SCALE_TEXT;
    }

    public TitleRenderingBehaviour getTitleRenderingBehaviour(ProcessedOption option) {
        return TitleRenderingBehaviour.LEFT;
    }

    public boolean shouldAutoFocusSearchbar() {
        return false;
    }

    public boolean shouldSearchCategoryNames() {
        return true;
    }

    public transient List<Runnable> saveRunnables = new ArrayList<>();

    /**
     * Verify that calling {@link #executeRunnable} is valid. A project moving away from runnable ids can return {@code false} to throw warnings whenever this gets invoked.
     */
    public boolean isValidRunnable(int runnableId) {
        return true;
    }
}
