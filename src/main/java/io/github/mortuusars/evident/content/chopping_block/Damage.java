package io.github.mortuusars.evident.content.chopping_block;

import net.minecraft.util.StringRepresentable;

public enum Damage implements StringRepresentable {
    NONE("none"),
    CHIPPED("chipped"),
    DAMAGED("damaged");

    private final String name;

    private static final Damage[] allValues = values();

    public Damage next() {
        Damage next = allValues[(this.ordinal() + 1) % allValues.length];
        return next.ordinal() < this.ordinal() ? this : next;
    }

    public Damage previous() {
        if (this.ordinal() == 0)
            return this;

        return allValues[(this.ordinal() - 1)];
    }

    public boolean hasNext() {
        return ordinal() < allValues.length - 1;
    }

    Damage(String pName) {
        this.name = pName;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
