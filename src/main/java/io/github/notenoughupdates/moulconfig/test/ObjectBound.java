package io.github.notenoughupdates.moulconfig.test;

import io.github.notenoughupdates.moulconfig.gui.CloseEventListener;
import io.github.notenoughupdates.moulconfig.observer.ObservableList;
import io.github.notenoughupdates.moulconfig.xml.Bind;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ObjectBound {
    @Bind
    public Runnable requestClose = null;

    @Bind
    public void afterClose() {
        System.out.println("After close");
    }

    @Bind
    public CloseEventListener.CloseAction beforeClose() {
        System.out.println("Before close");
        return CloseEventListener.CloseAction.NO_OBJECTIONS_TO_CLOSE;
    }

    @Bind
    public ItemStack itemStack = new ItemStack(Blocks.SAND);

    @Bind
    public boolean value = false;

    @Bind
    public String textField = "";

    @Bind
    public float slider = 0f;

    @Bind
    public void addElement() {
        data.add(new Element(textField));
        textField = "";
    }

    @Bind
    public ObservableList<Element> data =
        new ObservableList<>(new ArrayList<>(Arrays.asList(
            new Element("Test 1"), new Element("Test 2"), new Element("Test 3")
        )));

    public static class Element {
        @Bind
        public String text;

        @Bind
        public boolean enabled = false;

        public Element(String text) {
            this.text = text;
        }

        @Bind
        public void randomize() {
            text = "§" + "abcdef0123456789".charAt(new Random().nextInt(16)) + text.replaceAll("§.", "");
        }
    }
}
