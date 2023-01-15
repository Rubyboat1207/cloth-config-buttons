package me.shedaniel.clothconfig2.impl.builders;

import me.shedaniel.clothconfig2.gui.entries.ButtonEntry;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class ButtonBuilder {
    Component value;
    Component id;
    Button.OnPress onPress;
    
    private Function<String, Optional<Component[]>> tooltipSupplier = str -> Optional.empty();
    int xbuffer = 0;
    
    public ButtonBuilder(Component component, Component value) {
        this.id = component;
        this.value = value;
    }
    public ButtonBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        this.tooltipSupplier = str -> tooltipSupplier.get();
        return this;
    }
    
    public ButtonBuilder setTooltipSupplier(Function<String, Optional<Component[]>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }
    
    public ButtonBuilder setTooltip(Optional<Component[]> tooltip) {
        this.tooltipSupplier = str -> tooltip;
        return this;
    }
    
    public ButtonBuilder setTooltip(Component... tooltip) {
        this.tooltipSupplier = str -> Optional.ofNullable(tooltip);
        return this;
    }
    
    public ButtonBuilder setOnPress(Button.OnPress onPress) {
        this.onPress = onPress;
        return this;
    }
    
    public  ButtonBuilder setWidthBuffer(int xbuffer) {
        this.xbuffer = xbuffer;
        return this;
    }
    
    public ButtonEntry build() {
        ButtonEntry button = new ButtonEntry(id, value, null ,onPress, xbuffer);
        button.setTooltipSupplier(() -> tooltipSupplier.apply(button.getValue().toString()));
        return button;
    }
}
