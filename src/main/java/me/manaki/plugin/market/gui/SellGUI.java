package me.manaki.plugin.market.gui;

import me.manaki.plugin.market.Market;
import me.manaki.plugin.market.commodity.Commodities;
import me.manaki.plugin.market.commodity.Commodity;
import me.manaki.plugin.shops.storage.ItemStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SellGUI {

    public static final String TITLE = "§0§lBÁN";

    public static final int ICON_SLOT = 13;

    public static final int QUIT_SLOT = 31;
    public static final int SELL_ONE_SLOT = 29;
    public static final int SELL_ALL_SLOT = 33;

    public static void openGUI(Player player, Commodity commodity) {
        Inventory inv = Bukkit.createInventory(new SellGUIHolder(commodity), 45, TITLE);
        player.openInventory(inv);

        Bukkit.getScheduler().runTaskAsynchronously(Market.get(), () -> {
            for (int i = 0 ; i < inv.getSize() ; i++) inv.setItem(i, getBackButton());
            var is = ItemStorage.get(commodity.getID());
            is.setAmount(commodity.getAmount());
            if (is.getType() != Material.AIR) {
                inv.setItem(ICON_SLOT, is);
            }
            inv.setItem(SELL_ALL_SLOT, getSellAll(commodity));
            inv.setItem(SELL_ONE_SLOT, getSellOneTime(commodity));
            inv.setItem(QUIT_SLOT, getQuitButton());
        });
    }

    public static void onClick(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof SellGUIHolder)) return;
        e.setCancelled(true);
        if (e.getClickedInventory() != e.getWhoClicked().getOpenInventory().getTopInventory()) return;

        var player = (Player) e.getWhoClicked();
        var holder = (SellGUIHolder) e.getInventory().getHolder();
        int slot = e.getSlot();

        if (slot == SELL_ALL_SLOT) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            Commodities.sell(holder.getCommodity().getSlotId(), player, true);
        }
        if (slot == SELL_ONE_SLOT) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            Commodities.sell(holder.getCommodity().getSlotId(), player, false);
        }
        if (slot == QUIT_SLOT) {
            player.closeInventory();;
        }
    }

    public static void onClose(InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof SellGUIHolder)) return;

        var player = (Player) e.getPlayer();
        Bukkit.getScheduler().runTask(Market.get(), () -> {
            MarketGUI.openGUI(player, 1);
        });

    }

    public static ItemStack getBackButton() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        var meta = item.getItemMeta();
        meta.setDisplayName("§6");
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getSellOneTime(Commodity commodity) {
        ItemStack item = new ItemStack(Material.LIME_CONCRETE);
        var meta = item.getItemMeta();
        meta.setDisplayName("§a§lBán một lần (x" + commodity.getAmount() + ")");
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getSellAll(Commodity commodity) {
        ItemStack item = new ItemStack(Material.CYAN_CONCRETE);
        var meta = item.getItemMeta();
        meta.setDisplayName("§a§lBán hết");
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getQuitButton() {
        ItemStack item = new ItemStack(Material.BARRIER);
        var meta = item.getItemMeta();
        meta.setCustomModelData(160);

        meta.setDisplayName("§c§lThoát");
        item.setItemMeta(meta);

        return item;
    }

}

class SellGUIHolder implements InventoryHolder {

    private Commodity commodity;

    public SellGUIHolder(Commodity commodity) {
        this.commodity = commodity;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
