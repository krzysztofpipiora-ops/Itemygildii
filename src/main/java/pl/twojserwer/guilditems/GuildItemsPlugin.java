package pl.twojserwer.guilditems;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.*;
import java.util.*;

public class GuildItemsPlugin extends JavaPlugin implements Listener {

    private final HashMap<UUID, Map<String, Long>> cooldowns = new HashMap<>();
    private final NamespacedKey itemKey = new NamespacedKey(this, "guild_item_id");

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        registerAllItems();
    }

    private void registerAllItems() {
        createRecipe("vampire_sword", Material.NETHERITE_SWORD, "§4§lOstrze Wampira", "RNR", "RSR", " R ", 'R', Material.REDSTONE_BLOCK, 'N', Material.NETHERITE_INGOT, 'S', Material.NETHERITE_SWORD);
        createRecipe("shield_breaker", Material.NETHERITE_AXE, "§6§lTopór Rozpruwacz", "NNN", "NAN", " G ", 'N', Material.NETHERITE_INGOT, 'A', Material.NETHERITE_AXE, 'G', Material.GOLD_BLOCK);
        createRecipe("thor_hammer", Material.NETHERITE_AXE, "§e§lMłot Thora", "III", "ISI", " S ", 'I', Material.IRON_BLOCK, 'S', Material.BLAZE_ROD);
        createRecipe("void_pickaxe", Material.NETHERITE_PICKAXE, "§d§lKilof Próżni", "EEE", " P ", " P ", 'E', Material.END_CRYSTAL, 'P', Material.NETHERITE_PICKAXE);
        createRecipe("web_ball", Material.SNOWBALL, "§f§lKula Pajęczyny", "WWW", "WSW", "WWW", 'W', Material.COBWEB, 'S', Material.SNOWBALL);
        createRecipe("escape_totem", Material.CHORUS_FRUIT, "§5§lTotem Ucieczki", "CCC", "CTC", "CCC", 'C', Material.CHORUS_FRUIT, 'T', Material.TOTEM_OF_UNDYING);
        createRecipe("berserk_pot", Material.POTION, "§4§lWywar Berserkera", "RGR", "GPG", "RGR", 'R', Material.REDSTONE_BLOCK, 'G', Material.GLOWSTONE, 'P', Material.POTION);
    }

    private void createRecipe(String id, Material mat, String name, String s1, String s2, String s3, Object... ing) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING, id);
            item.setItemMeta(meta);
            
            ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, id), item);
            recipe.shape(s1, s2, s3);
            for (int i = 0; i < ing.length; i += 2) {
                recipe.setIngredient((Character) ing[i], (Material) ing[i + 1]);
            }
            Bukkit.addRecipe(recipe);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (item == null || !item.hasItemMeta()) return;
        String id = item.getItemMeta().getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);
        if (id == null) return;

        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!checkCooldown(p, id, 10)) return;

            if (id.equals("vampire_sword")) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
                p.sendMessage("§4Wampiryzm!");
            } else if (id.equals("escape_totem")) {
                p.teleport(p.getLocation().add(Math.random()*20-10, 0, Math.random()*20-10));
                p.sendMessage("§5Teleportacja!");
            }
        }
    }

    private boolean checkCooldown(Player p, String item, int sec) {
        cooldowns.putIfAbsent(p.getUniqueId(), new HashMap<>());
        long now = System.currentTimeMillis();
        long last = cooldowns.get(p.getUniqueId()).getOrDefault(item, 0L);
        if (now - last < sec * 1000L) {
            p.sendMessage("§cZaczekaj!");
            return false;
        }
        cooldowns.get(p.getUniqueId()).put(item, now);
        return true;
    }
}
