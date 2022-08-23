package me.shedaniel.clothconfig2.impl.builders;

import me.shedaniel.clothconfig2.gui.entries.ButtonEntry;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ButtonBuilder {
    Component value;
    Component id;
    Button.OnPress onPress;
    
    public ButtonBuilder(Component component, Component value) {
        this.id = component;
        this.value = value;
    }
    
    public ButtonBuilder setOnPress(Button.OnPress onPress) {
        this.onPress = onPress;
        return this;
    }
    
    public ButtonEntry build() {
        return new ButtonEntry(id, value, onPress);
    }
}
