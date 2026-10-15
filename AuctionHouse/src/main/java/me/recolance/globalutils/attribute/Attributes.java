/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  com.google.common.base.Objects
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.Iterators
 *  com.google.common.collect.Maps
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.recolance.globalutils.attribute;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class Attributes {
    public ItemStack stack;
    private NbtFactory.NbtList attributes;

    public Attributes(ItemStack stack) {
        this.stack = NbtFactory.getCraftItemStack(stack);
        NbtFactory.NbtCompound nbt = NbtFactory.fromItemTag(this.stack);
        this.attributes = nbt.getList("AttributeModifiers", true);
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public int size() {
        return this.attributes.size();
    }

    public void add(Attribute attribute) {
        Preconditions.checkNotNull((Object) attribute.getName(), (Object) "must specify an attribute name.");
        this.attributes.add(attribute.data);
    }

    public boolean remove(Attribute attribute) {
        UUID uuid = attribute.getUUID();
        Iterator<Attribute> it = this.values().iterator();
        while (it.hasNext()) {
            if (!Objects.equal((Object) it.next().getUUID(), (Object) uuid)) continue;
            it.remove();
            return true;
        }
        return false;
    }

    public void clear() {
        this.attributes.clear();
    }

    public Attribute get(int index) {
        return new Attribute((NbtFactory.NbtCompound) this.attributes.get(index));
    }

    public Iterable<Attribute> values() {
        return new Iterable<Attribute>() {

            @Override
            public Iterator<Attribute> iterator() {
                return Iterators.transform(Attributes.this.attributes.iterator(), (Function) new Function<Object, Attribute>() {

                    public Attribute apply(@Nullable Object element) {
                        return new Attribute((NbtFactory.NbtCompound) element);
                    }
                });
            }
        };
    }

    public static enum Operation {
        ADD_NUMBER(0),
        MULTIPLY_PERCENTAGE(1),
        ADD_PERCENTAGE(2);

        private int id;

        private Operation(int id) {
            this.id = id;
        }

        public static Operation fromId(int id) {
            for (Operation op : Operation.values()) {
                if (op.getId() != id) continue;
                return op;
            }
            throw new IllegalArgumentException("Corrupt operation ID " + id + " detected.");
        }

        public int getId() {
            return this.id;
        }
    }

    public static class Attribute {
        private NbtFactory.NbtCompound data;

        private Attribute(Builder builder) {
            this.data = NbtFactory.createCompound();
            this.setAmount(builder.amount);
            this.setOperation(builder.operation);
            this.setAttributeType(builder.type);
            this.setName(builder.name);
            this.setUUID(builder.uuid);
        }

        private Attribute(NbtFactory.NbtCompound data) {
            this.data = data;
        }

        public static Builder newBuilder() {
            return new Builder().uuid(UUID.randomUUID()).operation(Operation.ADD_NUMBER);
        }

        private static /* synthetic */ void $$$reportNull$$$0(int n) {
            Object[] arrobject;
            Object[] arrobject2;
            Object[] arrobject3 = new Object[3];
            switch (n) {
                default: {
                    arrobject2 = arrobject3;
                    arrobject3[0] = "operation";
                    break;
                }
                case 1: {
                    arrobject2 = arrobject3;
                    arrobject3[0] = "type";
                    break;
                }
                case 2: {
                    arrobject2 = arrobject3;
                    arrobject3[0] = "name";
                    break;
                }
                case 3: {
                    arrobject2 = arrobject3;
                    arrobject3[0] = "id";
                    break;
                }
            }
            arrobject2[1] = "me/recolance/globalutils/attribute/Attributes$Attribute";
            switch (n) {
                default: {
                    arrobject = arrobject2;
                    arrobject2[2] = "setOperation";
                    break;
                }
                case 1: {
                    arrobject = arrobject2;
                    arrobject2[2] = "setAttributeType";
                    break;
                }
                case 2: {
                    arrobject = arrobject2;
                    arrobject2[2] = "setName";
                    break;
                }
                case 3: {
                    arrobject = arrobject2;
                    arrobject2[2] = "setUUID";
                    break;
                }
            }
            throw new IllegalArgumentException(String.format("Argument for @Nonnull parameter '%s' of %s.%s must not be null", arrobject));
        }

        public double getAmount() {
            return this.data.getDouble("Amount", 0.0);
        }

        public void setAmount(double amount) {
            this.data.put("Amount", (Object) amount);
        }

        public Operation getOperation() {
            return Operation.fromId(this.data.getInteger("Operation", 0));
        }

        public void setOperation(@Nonnull Operation operation) {
            if (operation == null) {
                Attribute.$$$reportNull$$$0(0);
            }
            Preconditions.checkNotNull((Object) ((Object) operation), (Object) "operation cannot be NULL.");
            this.data.put("Operation", (Object) operation.getId());
        }

        public AttributeType getAttributeType() {
            return AttributeType.fromId(this.data.getString("AttributeName", null));
        }

        public void setAttributeType(@Nonnull AttributeType type) {
            if (type == null) {
                Attribute.$$$reportNull$$$0(1);
            }
            Preconditions.checkNotNull((Object) type, (Object) "type cannot be NULL.");
            this.data.put("AttributeName", (Object) type.getMinecraftId());
        }

        public String getName() {
            return this.data.getString("Name", null);
        }

        public void setName(@Nonnull String name) {
            if (name == null) {
                Attribute.$$$reportNull$$$0(2);
            }
            Preconditions.checkNotNull((Object) name, (Object) "name cannot be NULL.");
            this.data.put("Name", (Object) name);
        }

        public UUID getUUID() {
            return new UUID(this.data.getLong("UUIDMost", null), this.data.getLong("UUIDLeast", null));
        }

        public void setUUID(@Nonnull UUID id) {
            if (id == null) {
                Attribute.$$$reportNull$$$0(3);
            }
            Preconditions.checkNotNull((Object) "id", (Object) "id cannot be NULL.");
            this.data.put("UUIDLeast", (Object) id.getLeastSignificantBits());
            this.data.put("UUIDMost", (Object) id.getMostSignificantBits());
        }

        public static class Builder {
            private double amount;
            private Operation operation = Operation.ADD_NUMBER;
            private AttributeType type;
            private String name;
            private UUID uuid;

            private Builder() {
            }

            public Builder amount(double amount) {
                this.amount = amount;
                return this;
            }

            public Builder operation(Operation operation) {
                this.operation = operation;
                return this;
            }

            public Builder type(AttributeType type) {
                this.type = type;
                return this;
            }

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder uuid(UUID uuid) {
                this.uuid = uuid;
                return this;
            }

            public Attribute build() {
                return new Attribute(this);
            }
        }
    }

    public static class AttributeType {
        private static ConcurrentMap<String, AttributeType> LOOKUP = Maps.newConcurrentMap();
        public static final AttributeType GENERIC_MAX_HEALTH = new AttributeType("generic.maxHealth").register();
        public static final AttributeType GENERIC_FOLLOW_RANGE = new AttributeType("generic.followRange").register();
        public static final AttributeType GENERIC_ATTACK_DAMAGE = new AttributeType("generic.attackDamage").register();
        public static final AttributeType GENERIC_MOVEMENT_SPEED = new AttributeType("generic.movementSpeed").register();
        public static final AttributeType GENERIC_KNOCKBACK_RESISTANCE = new AttributeType("generic.knockbackResistance").register();
        private final String minecraftId;

        public AttributeType(String minecraftId) {
            this.minecraftId = minecraftId;
        }

        public static AttributeType fromId(String minecraftId) {
            return (AttributeType) LOOKUP.get(minecraftId);
        }

        public static Iterable<AttributeType> values() {
            return LOOKUP.values();
        }

        public String getMinecraftId() {
            return this.minecraftId;
        }

        public AttributeType register() {
            AttributeType old = LOOKUP.putIfAbsent(this.minecraftId, this);
            return old != null ? old : this;
        }
    }
}

