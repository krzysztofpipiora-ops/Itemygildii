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
        // 1-5: Broń i Walka
        createRecipe("vampire_sword", Material.NETHERITE_SWORD, "§4§lOstrze Wampira", 1001, "RNR", "RSR", " R ", 'R', Material.REDSTONE_BLOCK, 'N', Material.NETHERITE_INGOT, 'S', Material.NETHERITE_SWORD);
        createRecipe("shield_breaker", Material.NETHERITE_AXE, "§6§lTopór Rozpruwacz", 1002, "NNN", "NAN", " G ", 'N', Material.NETHERITE_INGOT, 'A', Material.NETHERITE_AXE, 'G', Material.GOLD_BLOCK);
        createRecipe("thor_hammer", Material.NETHERITE_AXE, "§e§lMłot Thora", 1003, "III", "ISI", " S ", 'I', Material.IRON_BLOCK, 'S', Material.BLAZE_ROD);
        createRecipe("shadow_dagger", Material.NETHERITE_SWORD, "§8§lSztylet Cienia", 1004, " O ", " O ", " S ", 'O', Material.OBSIDIAN, 'S', Material.STICK);
        createRecipe("frost_bow", Material.BOW, "§b§lŁuk Mroźnego Wichru", 1005, " IS", "I S", " IS", 'I', Material.ICE, 'S', Material.STRING);

        // 6-10: Narzędzia i Pancerz
        createRecipe("void_pickaxe", Material.NETHERITE_PICKAXE, "§d§lKilof Próżni", 1006, "EEE", " P ", " P ", 'E', Material.END_CRYSTAL, 'P', Material.NETHERITE_PICKAXE);
        createRecipe("fortune_pick", Material.DIAMOND_PICKAXE, "§a§lKilof Geologa", 1007, "DDD", " S ", " S ", 'D', Material.DIAMOND_BLOCK, 'S', Material.STICK);
        createRecipe("hermes_boots", Material.NETHERITE_BOOTS, "§f§lButy Hermesa", 1008, "F F", "N N", "   ", 'F', Material.FEATHER, 'N', Material.NETHERITE_INGOT);
        createRecipe("magma_chest", Material.NETHERITE_CHESTPLATE, "§c§lZbroja Magmowa", 1009, "M M", "MNM", "MMM", 'M', Material.MAGMA_BLOCK, 'N', Material.NETHERITE_CHESTPLATE);
        createRecipe("hook_rod", Material.FISHING_ROD, "§3§lWędka Przyciągacz", 1010, "  I", " I ", "S  ", 'I', Material.IRON_INGOT, 'S', Material.STICK);

        // 11-15: Strategiczne i flagi
        createRecipe("strength_flag", Material.RED_BANNER, "§c§lSztandar Siły", 1011, "SSS", "SBS", " P ", 'S', Material.BLAZE_POWDER, 'B', Material.RED_BANNER, 'P', Material.BLAZE_ROD);
        createRecipe("defense_flag", Material.BLUE_BANNER, "§9§lSztandar Obrony", 1012, "III", "IBI", " P ", 'I', Material.IRON_BLOCK, 'B', Material.BLUE_BANNER, 'P', Material.BLAZE_ROD);
        createRecipe("escape_totem", Material.CHORUS_FRUIT, "§5§lTotem Ucieczki", 1013, "CCC", "CTC", "CCC", 'C', Material.CHORUS_FRUIT, 'T', Material.TOTEM_OF_UNDYING);
        createRecipe("web_ball", Material.SNOWBALL, "§f§lKula Pajęczyny", 1014, "WWW", "WSW", "WWW", 'W', Material.COBWEB, 'S', Material.SNOWBALL);
        createRecipe("anti_pearl", Material.ENDER_EYE, "§0§lAnty-Perła", 1015, "DOD", "OEO", "DOD", 'D', Material.DIAMOND, 'O', Material.OBSIDIAN, 'E', Material.ENDER_EYE);

        // 16-20: Specjalne
        createRecipe("horn_plenty", Material.GOAT_HORN, "§6§lRóg Obfitości", 1016, "GGG", "G H", "GGG", 'G', Material.GOLDEN_APPLE, 'H', Material.GOAT_HORN);
        createRecipe("berserk_pot", Material.POTION, "§4§lWywar Berserkera", 1017, "RGR", "GPG", "RGR", 'R', Material.REDSTONE_BLOCK, 'G', Material.GLOWSTONE, 'P', Material.POTION);
        createRecipe("ocean_heart", Material.HEART_OF_THE_SEA, "§b§lAmulet Oceanu", 1018, "CCC", "CHC", "CCC", 'C', Material.PRISMARINE_CRYSTALS, 'H', Material.HEART_OF_THE_SEA);
        createRecipe("vault_key", Material.TRIPWIRE_HOOK, "§e§lKlucz do Skarbca", 1019, "GGG", "GKG", "GGG", 'G', Material.GOLD_BLOCK, 'K', Material.TRIPWIRE_HOOK);
        createRecipe("scout_compass", Material.COMPASS, "§2§lKompas Zwiadowcy", 1020, "EEE", "ECE", "EEE", 'E', Material.EMERALD, 'C', Material.COMPASS);
    }

    private void createRecipe(String id, Material mat, String name, int cmd, String s1, String s2, String s3, Object... ing) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setCustomModelData(cmd);
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

            switch (id) {
                case "vampire_sword" -> {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
                    p.sendMessage("§4Wampiryzm!");
                }
                case "escape_totem" -> {
                    p.teleport(p.getLocation().add(Math.random()*20-10, 0, Math.random()*20-10));
                    p.sendMessage("§5Teleportacja!");
                }
                case "web_ball" -> {
                    p.launchProjectile(Snowball.class);
                    p.sendMessage("§fWystrzelono sieć!");
                }
                case "thor_hammer" -> {
                    p.getWorld().strikeLightning(p.getTargetBlockExact(50).getLocation());
                    p.sendMessage("§ePoczuj gniew Thora!");
                }
                case "berserk_pot" -> {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 1));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 1));
                    p.sendMessage("§4SZAŁ BERSERKERA!");
                }
            }
        }
    }

    private boolean checkCooldown(Player p, String item, int sec) {
        cooldowns.putIfAbsent(p.getUniqueId(), new HashMap<>());
        long now = System.currentTimeMillis();
        long last = cooldowns.get(p.getUniqueId()).getOrDefault(item, 0L);
        if (now - last < sec * 1000L) {
            p.sendMessage("§cZaczekaj jeszcze " + (sec - (now - last) / 1000) + "s!");
            return false;
        }
        cooldowns.get(p.getUniqueId()).put(item, now);
        return true;
    }
}
