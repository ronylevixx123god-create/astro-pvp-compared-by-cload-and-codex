package com.conaxgames.customskulls.util;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemStackingUtils {
    public static List<ItemStack> updateCollect(List<ItemStack> itemStackList) {


        Map<Integer, ItemStack> lootGainedFromOpening = new HashMap<>();

        int a = itemStackList.size()-1;
        while(a!=0) {
            ItemStack itemStack = itemStackList.get(a);
            lootGainedFromOpening.put(a, itemStack);
            a--;
        }


        HashMap<ItemStack, Integer> fixedItems = new HashMap<>();
        List<ItemStack> giveItems = new ArrayList<>();

        for(ItemStack itemStack : lootGainedFromOpening.values()) { // LOOP THROUGH ALL THE REWARDS

            int amountOfItem = itemStack.getAmount(); // store the value in a variable so we know how much they have
            itemStack.setAmount(1); // set the amount to one so we can access the value in the hashmap properly

            if(fixedItems.containsKey(itemStack)) { // CHECK IF THE FIXED HASHMAP CONTAINS THE REWARD ALREADY

                if(amountOfItem + fixedItems.get(itemStack) <= itemStack.getMaxStackSize()) { // check if current value in hash map + latest reward is smaller than or equal to 63
                    fixedItems.put(itemStack, amountOfItem + fixedItems.get(itemStack)); // put that value in the hashmap

                } else { // otherwise
                    int overdraft = (fixedItems.get(itemStack) + amountOfItem) - itemStack.getMaxStackSize(); //
                    fixedItems.put(itemStack, overdraft); // put the remainder of the items back in the hashmap
                    itemStack.setAmount(itemStack.getMaxStackSize()); // set it to 64 as it has to be over 64
                    giveItems.add(itemStack); // put 64 of the item in the arrau

                }

            } else {

                fixedItems.put(itemStack, amountOfItem); // put the item in the hashmap
            }

        }


        for(Map.Entry<ItemStack, Integer> entry : fixedItems.entrySet()) {
            ItemStack itemStack = entry.getKey();
            itemStack.setAmount(entry.getValue());
            giveItems.add(itemStack);
        }
        // put all the items that dont need to stack above 64 in the array
        return giveItems;

    }

}
