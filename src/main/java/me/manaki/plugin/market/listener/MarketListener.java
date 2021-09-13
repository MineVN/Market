package me.manaki.plugin.market.listener;

import me.manaki.plugin.market.gui.CommodityGUI;
import me.manaki.plugin.market.gui.MarketGUI;
import me.manaki.plugin.market.gui.SellGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class MarketListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        MarketGUI.eventHandling(e);
        CommodityGUI.onClick(e);
        SellGUI.onClick(e);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        CommodityGUI.onClose(e);
        SellGUI.onClose(e);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (e.getView().getTitle().equals(MarketGUI.TITLE)) e.setCancelled(true);
    }


}
