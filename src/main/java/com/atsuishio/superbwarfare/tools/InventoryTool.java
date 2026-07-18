package com.atsuishio.superbwarfare.tools;

import com.atsuishio.superbwarfare.data.gun.Ammo;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.item.common.ammo.AmmoBoxItem;
import com.atsuishio.superbwarfare.item.common.ammo.AmmoSupplierItem;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;

import java.util.function.Predicate;

public class InventoryTool {

    public static int countItem(@Nullable Player player, @NotNull Item item) {
        return countItem(player, stack -> stack.is(item));
    }

    public static int countItem(@Nullable Player player, @NotNull TagKey<Item> item) {
        return countItem(player, stack -> stack.is(item));
    }

    public static int countItem(@Nullable Player player, @NotNull Predicate<ItemStack> predicate) {
        if (player == null) return 0;
        return countContainerItems(player.getInventory(), predicate);
    }

    /**
     * 计算物品列表内指定物品的数量
     *
     * @param itemList 物品列表
     * @param item     物品类型
     */
    public static int countItem(@Nullable NonNullList<ItemStack> itemList, @NotNull Item item) {
        if (itemList == null) return 0;

        return itemList.stream()
                .filter(stack -> stack.is(item))
                .mapToInt(ItemStack::getCount)
                .sum();
    }

    /**
     * 计算实体物品栏内指定物品的数量
     *
     * @param entity 实体
     * @param item   物品类型
     */
    public static int countItem(@Nullable Entity entity, @NotNull Item item) {
        return countItem(entity, stack -> stack.is(item));
    }

    public static int countItem(@Nullable Entity entity, @NotNull Predicate<ItemStack> predicate) {
        if (entity instanceof Player player) {
            return countItem(player, predicate);
        }
        if (entity instanceof Container container) {
            return countContainerItems(container, predicate);
        }
        return 0;
    }

    public static int countAmmoItem(@Nullable Player player, @Nullable Ammo type) {
        if (player == null || type == null) return 0;
        return countContainerAmmo(player.getInventory(), type);
    }

    private static int countContainerItems(Container container, Predicate<ItemStack> predicate) {
        int count = 0;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (predicate.test(stack)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    private static int countContainerAmmo(Container container, Ammo type) {
        int count = 0;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            // AmmoSupplier Item
            if (stack.getItem() instanceof AmmoSupplierItem ammoSupplierItem && ammoSupplierItem.type == type) {
                count += ammoSupplierItem.ammoToAdd * stack.getCount();
            }

            // AmmoBox
            if (stack.getItem() instanceof AmmoBoxItem) {
                var stackAmmo = type.get(stack);
                if (stackAmmo > 0) {
                    count += stackAmmo;
                }
            }
        }

        return count;
    }

    public static int countAmmoItem(@Nullable Entity entity, @Nullable Ammo type) {
        if (entity instanceof Player player) {
            return countAmmoItem(player, type);
        }
        if (entity instanceof Container container && type != null) {
            return countContainerAmmo(container, type);
        }
        return 0;
    }

    public static int consumeAmmoItem(@Nullable Entity entity, @Nullable Ammo type, int count) {
        if (entity instanceof Player player) {
            return consumeAmmoItem(player, type, count);
        }
        if (entity instanceof Container container && type != null && count > 0) {
            return consumeContainerAmmo(container, type, count);
        }
        return 0;
    }

    public static int consumeAmmoItem(@Nullable Player player, @Nullable Ammo type, int count) {
        if (player == null || type == null || count <= 0) return 0;
        return consumeContainerAmmo(player.getInventory(), type, count);
    }

    private static int consumeContainerAmmo(Container container, Ammo type, int count) {
        int initialCount = count;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            // AmmoBox
            if (stack.getItem() instanceof AmmoBoxItem) {
                var stackAmmo = type.get(stack);
                if (stackAmmo > 0) {
                    var maxConsumable = Math.min(stackAmmo, count);
                    type.set(stack, stackAmmo - maxConsumable);
                    count -= maxConsumable;
                    continue;
                }
            }

            // AmmoSupplier Item
            if (!(stack.getItem() instanceof AmmoSupplierItem ammoSupplierItem && ammoSupplierItem.type == type))
                continue;

            var supplyCount = ammoSupplierItem.ammoToAdd;
            var required = (count % supplyCount == 0) ? count / supplyCount : count / supplyCount + 1;

            var countToShrink = Math.min(stack.getCount(), required);
            stack.shrink(countToShrink);
            count -= countToShrink * supplyCount;
            if (count <= 0) break;
        }
        int consumed = initialCount - count;
        if (consumed > 0) {
            container.setChanged();
        }
        return consumed;
    }

    /**
     * 判断实体物品栏内是否有指定物品
     *
     * @param entity 实体
     * @param item   物品类型
     */
    public static boolean hasItem(@Nullable Entity entity, @NotNull Item item) {
        return !findFirst(entity, item).isEmpty();
    }

    /**
     * 判断物品列表内是否有指定物品
     *
     * @param itemList 物品列表
     * @param item     物品类型
     */
    public static boolean hasItem(@Nullable NonNullList<ItemStack> itemList, @NotNull Item item) {
        return !findFirst(itemList, item).isEmpty();
    }

    public static ItemStack findFirst(@Nullable Entity entity, @NotNull Item item) {
        if (entity instanceof Player player) {
            return findFirst(player, item);
        }
        if (entity instanceof Container container) {
            return findFirstInContainer(container, stack -> stack.is(item));
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack findFirst(@Nullable Player player, @NotNull Item item) {
        return findFirst(player, stack -> stack.is(item));
    }

    public static ItemStack findFirst(@Nullable NonNullList<ItemStack> list, @NotNull Item item) {
        return findFirst(list, stack -> stack.is(item));
    }

    public static ItemStack findFirst(@Nullable NonNullList<ItemStack> list, @NotNull Predicate<ItemStack> predicate) {
        if (list == null) return ItemStack.EMPTY;

        return list.stream().filter(predicate).findFirst().orElse(ItemStack.EMPTY);
    }

    public static ItemStack findFirst(@Nullable Player player, @NotNull Predicate<ItemStack> predicate) {
        if (player == null) return ItemStack.EMPTY;
        return findFirstInContainer(player.getInventory(), predicate);
    }

    private static ItemStack findFirstInContainer(Container container, Predicate<ItemStack> predicate) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (predicate.test(stack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean hasCreativeAmmoBox(@Nullable Player player) {
        return !findFirst(player, ModItems.CREATIVE_AMMO_BOX.get()).isEmpty();
    }

    /**
     * 判断物品列表内是否有创造模式弹药盒
     *
     * @param itemList 物品列表
     */
    public static boolean hasCreativeAmmoBox(@Nullable NonNullList<ItemStack> itemList) {
        return !findFirst(itemList, ModItems.CREATIVE_AMMO_BOX.get()).isEmpty();
    }

    /**
     * 判断实体物品栏内是否有创造模式弹药盒
     *
     * @param entity 实体
     */
    public static boolean hasCreativeAmmoBox(@Nullable Entity entity) {
        if (entity instanceof VehicleEntity vehicle) {
            return hasCreativeAmmoBoxForVehicle(vehicle);
        } else if (entity instanceof Player player) {
            return hasCreativeAmmoBox(player);
        }
        return false;
    }

    public static boolean hasCreativeAmmoBoxForVehicle(@NotNull VehicleEntity vehicle) {
        var passengers = vehicle.getPassengers();
        boolean flag = passengers.stream().anyMatch(e -> InventoryTool.hasItem(e, ModItems.CREATIVE_AMMO_BOX.get())) && vehicle.data().compute().usePassengerCreativeAmmoBox;
        return flag || hasItem(vehicle, ModItems.CREATIVE_AMMO_BOX.get());
    }

    /**
     * 消耗物品列表内指定物品
     *
     * @param item  物品类型
     * @param count 要消耗的数量
     * @return 成功消耗的物品数量
     */
    public static int consumeItem(@Nullable NonNullList<ItemStack> itemList, Item item, int count) {
        return consumeItem(itemList, stack -> stack.is(item), count);
    }

    /**
     * 消耗生物物品列表内指定物品
     *
     * @param living 物品类型
     * @param item   物品类型
     * @param count  要消耗的数量
     */
    public static void consumeItem(LivingEntity living, Item item, int count) {
        if (living instanceof Player player) {
            InventoryTool.consumeItem(player, item, count);
        }
    }

    public static int consumeItem(@Nullable NonNullList<ItemStack> itemList, Predicate<ItemStack> predicate, int count) {
        if (itemList == null || count <= 0) return 0;

        int initialCount = count;
        var items = itemList.stream().filter(predicate).toList();
        for (var stack : items) {
            var countToShrink = Math.min(stack.getCount(), count);
            stack.shrink(countToShrink);
            count -= countToShrink;
            if (count <= 0) break;
        }
        return initialCount - count;
    }

    public static int consumeItem(@Nullable Player player, Item item, int count) {
        return consumeItem(player, stack -> stack.is(item), count);
    }

    public static int consumeItem(@Nullable Player player, Predicate<ItemStack> predicate, int count) {
        if (player == null || count <= 0) return 0;

        return consumeContainerItem(player.getInventory(), predicate, count);
    }

    public static int consumeItem(@Nullable Entity entity, Predicate<ItemStack> predicate, int count) {
        if (entity instanceof Player player) {
            return consumeItem(player, predicate, count);
        }
        if (entity instanceof Container container) {
            return consumeContainerItem(container, predicate, count);
        }
        return 0;
    }

    private static int consumeContainerItem(Container container, Predicate<ItemStack> predicate, int count) {
        if (count <= 0) return 0;

        int initialCount = count;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!predicate.test(stack)) continue;

            var countToShrink = Math.min(stack.getCount(), count);
            stack.shrink(countToShrink);
            count -= countToShrink;
            if (count <= 0) break;
        }
        int consumed = initialCount - count;
        if (consumed > 0) {
            container.setChanged();
        }
        return consumed;
    }

    public static int insertItem(@Nullable Container container, @NotNull ItemStack stack) {
        if (container == null || stack.isEmpty()) return 0;

        int originalCount = stack.getCount();

        for (int i = 0; i < container.getContainerSize() && !stack.isEmpty(); i++) {
            ItemStack existing = container.getItem(i);
            if (existing.isEmpty() || !ItemStack.isSameItemSameTags(existing, stack)) continue;

            int limit = Math.min(container.getMaxStackSize(), existing.getMaxStackSize());
            int toInsert = Math.min(limit - existing.getCount(), stack.getCount());
            if (toInsert <= 0) continue;

            ItemStack candidate = stack.copyWithCount(toInsert);
            if (!canInsertIntoSlot(container, i, candidate)) continue;

            existing.grow(toInsert);
            stack.shrink(toInsert);
        }

        for (int i = 0; i < container.getContainerSize() && !stack.isEmpty(); i++) {
            if (!container.getItem(i).isEmpty()) continue;

            int limit = Math.min(container.getMaxStackSize(), stack.getMaxStackSize());
            int toInsert = Math.min(limit, stack.getCount());
            if (toInsert <= 0) continue;

            ItemStack candidate = stack.copyWithCount(toInsert);
            if (!canInsertIntoSlot(container, i, candidate)) continue;

            container.setItem(i, candidate);
            stack.shrink(toInsert);
        }

        int inserted = originalCount - stack.getCount();
        if (inserted > 0) {
            container.setChanged();
        }
        return inserted;
    }

    public static int insertItemInSlotOrder(@Nullable Container container, @NotNull ItemStack stack) {
        if (container == null || stack.isEmpty()) return 0;

        int originalCount = stack.getCount();
        for (int i = 0; i < container.getContainerSize() && !stack.isEmpty(); i++) {
            ItemStack existing = container.getItem(i);
            int limit;
            int toInsert;

            if (existing.isEmpty()) {
                limit = Math.min(container.getMaxStackSize(), stack.getMaxStackSize());
                toInsert = Math.min(limit, stack.getCount());
            } else if (ItemStack.isSameItemSameTags(existing, stack)) {
                limit = Math.min(container.getMaxStackSize(), existing.getMaxStackSize());
                toInsert = Math.min(limit - existing.getCount(), stack.getCount());
            } else {
                continue;
            }

            if (toInsert <= 0) continue;
            ItemStack candidate = stack.copyWithCount(toInsert);
            if (!canInsertIntoSlot(container, i, candidate)) continue;

            if (existing.isEmpty()) {
                container.setItem(i, candidate);
            } else {
                existing.grow(toInsert);
            }
            stack.shrink(toInsert);
        }

        int inserted = originalCount - stack.getCount();
        if (inserted > 0) {
            container.setChanged();
        }
        return inserted;
    }

    private static boolean canInsertIntoSlot(Container container, int slot, ItemStack stack) {
        if (!container.canPlaceItem(slot, stack)) return false;

        if (container instanceof Inventory inventory) {
            int armorStart = inventory.items.size();
            int armorIndex = slot - armorStart;
            if (armorIndex >= 0 && armorIndex < inventory.armor.size()) {
                EquipmentSlot equipmentSlot = EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, armorIndex);
                return LivingEntity.getEquipmentSlotForItem(stack) == equipmentSlot;
            }
        }

        return true;
    }

    /**
     * 尝试插入指定物品指定数量
     *
     * @param item  物品类型
     * @param count 要插入的数量
     * @return 未能成功插入的物品数量
     */
    public static int insertItem(@Nullable NonNullList<ItemStack> itemList, Item item, int count, int maxStackSize) {
        if (itemList == null || count <= 0) return count;

        var defaultStack = new ItemStack(item);
        maxStackSize = Math.min(maxStackSize, item.getMaxStackSize());

        for (int i = 0; i < itemList.size(); i++) {
            var stack = itemList.get(i);

            if (stack.is(item) && stack.getCount() < maxStackSize) {
                var countToAdd = Math.min(maxStackSize - stack.getCount(), count);
                stack.grow(countToAdd);
                count -= countToAdd;
            } else if (stack.isEmpty()) {
                var countToAdd = Math.min(maxStackSize, count);
                itemList.set(i, new ItemStack(item, countToAdd));
                count -= countToAdd;
            }

            if (count <= 0) break;
        }

        return count;
    }

    public static int insertItem(@Nullable NonNullList<ItemStack> itemList, ItemStack stack) {
        if (itemList == null) return stack.getCount();

        var maxStackSize = stack.getMaxStackSize();
        var originalCount = stack.getCount();

        for (int i = 0; i < itemList.size(); i++) {
            var currentStack = itemList.get(i);

            if (ItemStack.isSameItemSameTags(stack, currentStack) && currentStack.getCount() < maxStackSize) {
                var countToAdd = Math.min(maxStackSize - currentStack.getCount(), stack.getCount());
                currentStack.grow(countToAdd);
                stack.setCount(stack.getCount() - countToAdd);
            } else if (currentStack.isEmpty()) {
                int inserted = stack.getCount();
                itemList.set(i, stack.copy());
                stack.setCount(0);
                return inserted;
            }

            if (stack.getCount() <= 0) break;
        }

        return originalCount - stack.getCount();
    }
}
