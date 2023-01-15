/*
 * This file is part of Cloth Config.
 * Copyright (C) 2020 - 2021 shedaniel
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package me.shedaniel.clothconfig2.gui.entries;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.clothconfig2.gui.AbstractConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ButtonEntry extends TooltipListEntry<Object> {
    public static final int LINE_HEIGHT = 12;
    private final Font textRenderer = Minecraft.getInstance().font;
    Component buttonInnerText;
    private int savedWidth = -1;
    private int savedX = -1;
    private int savedY = -1;
    
    public int xbuffer;
    public int ybuffer;
    
    private final List<AbstractWidget> widgets;
    private Button button;
    private List<FormattedCharSequence> wrappedLines;
    @ApiStatus.Internal
    @Deprecated
    public ButtonEntry(Component fieldName, Component text, Button.OnPress onPress, int xbuffer) {
        this(fieldName, text, null, onPress, xbuffer);
    }
    
    @ApiStatus.Internal
    @Deprecated
    public ButtonEntry(Component fieldName, Component text, Supplier<Optional<Component[]>> tooltipSupplier, Button.OnPress onPress, int xbuffer) {
        super(fieldName, tooltipSupplier);
        this.buttonInnerText = text;
        this.wrappedLines = Collections.emptyList();
        this.button = new Button(0, 0, Minecraft.getInstance().font.width(text) + 6 + xbuffer, 20, text, onPress);
        widgets = Lists.newArrayList(button);
    }
    
    @Override
    public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        if (this.savedWidth != entryWidth || this.savedX != x || this.savedY != y) {
            this.wrappedLines = this.textRenderer.split(this.buttonInnerText, entryWidth);
            this.savedWidth = entryWidth;
            this.savedX = x;
            this.savedY = y;
        }
        button.x = getConfigScreen().width / 2 - button.getWidth() / 2;
        button.y = y;
        button.render(matrices, mouseX, mouseY, delta);
        
        
        Style style = this.getTextAt(mouseX, mouseY);
        AbstractConfigScreen configScreen = this.getConfigScreen();
        if (style != null && configScreen != null) {
            configScreen.renderComponentHoverEffect(matrices, style, mouseX, mouseY);
        }
    }
    
    @Override
    public int getItemHeight() {
        if (savedWidth == -1) return LINE_HEIGHT;
        int lineCount = this.wrappedLines.size();
        return lineCount == 0 ? 0 : 15 + lineCount * LINE_HEIGHT;
    }
    
    @Nullable
    private Style getTextAt(double x, double y) {
        int lineCount = this.wrappedLines.size();
        
        if (lineCount > 0) {
            int textX = Mth.floor(x - this.savedX);
            int textY = Mth.floor(y - 4 - this.savedY);
            if (textX >= 0 && textY >= 0 && textX <= this.savedWidth && textY < LINE_HEIGHT * lineCount + lineCount) {
                int line = textY / LINE_HEIGHT;
                if (line < this.wrappedLines.size()) {
                    FormattedCharSequence orderedText = this.wrappedLines.get(line);
                    return this.textRenderer.getSplitter().componentStyleAtWidth(orderedText, textX);
                }
            }
        }
        return null;
    }
    
    @Override
    public Object getValue() {
        return null;
    }
    
    @Override
    public Optional<Object> getDefaultValue() {
        return Optional.empty();
    }
    
    @Override
    public void save() {
        
    }
    
    @Override
    public List<? extends GuiEventListener> children() {
        return widgets;
    }
    
    @Override
    public List<? extends NarratableEntry> narratables() {
        return Collections.emptyList();
    }
}
