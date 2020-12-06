package hotspot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends JavaPlugin implements Listener, TabCompleter {
    ArrayList<Location> spot = new ArrayList<Location>();

    @Override
    public void onEnable() {
        System.out.println("Hotspot is activated");
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("/spot").setExecutor(this::onCommand);
        getCommand("/spot").setTabCompleter(this::onTabComplete);
        getCommand("/go").setExecutor(this::onCommand);
    }

    @Override
    public void onDisable() {
        System.out.println("Hotspot is deactivated");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("/spot")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hotspot developed by Semin");
            }
            else if (args[0].equalsIgnoreCase("add")) {
                spot.add(player.getLocation());
                sender.sendMessage(ChatColor.GREEN + "add spot at " + player.getLocation());
            }
            else if (args[0].equalsIgnoreCase("remove")) {
                if (!(args[1].isEmpty())) {
                    if (isInt(args[1])) {
                        int num = Integer.parseInt(args[0]);
                        if (num > 0 && num <= spot.size()) {
                            spot.remove(num);
                        }
                        else {
                            sender.sendMessage(ChatColor.RED + "please write integer argument from 1 to " + spot.size());
                        }
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "please write integer argument");
                    }
                }
                else {
                    sender.sendMessage(ChatColor.RED + "please write integer argument");
                }
            }
            else if (args[0].equalsIgnoreCase("getlist")) {
                ItemStack stack = new ItemStack(Material.PAPER);
                ItemMeta meta = stack.getItemMeta();

                meta.setDisplayName("spot list");
                meta.setLore(Arrays.asList("list for see all spot"));
                stack.setItemMeta(meta);

                player.getInventory().addItem(stack);
            }
        }
        else if (command.getName().equalsIgnoreCase("/go")) {
            if (args.length > 0) {
                sender.sendMessage(ChatColor.RED + "please write integer argument");
            }
            else {
                if (isInt(args[0])) {
                    int num = Integer.parseInt(args[0]);
                    player.teleport(spot.get(num - 1));
                    sender.sendMessage(ChatColor.GOLD + "thwip!");
                }
                else {
                    sender.sendMessage(ChatColor.RED + "please write integer argument");
                }
            }
        }
        return true;
    }

    boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("/spot")) {
            if (args.length == 1) {
                List<String> arguments = new ArrayList<>();
                arguments.add("add");
                arguments.add("remove");
                arguments.add("getlist");

                return arguments;
            }
        }
        return null;
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("spot list")) {
            if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                for (int i = 0; i < spot.size(); i++) {
                    player.sendMessage((i + 1) + ": " + spot.get(i));
                }
            }
        }
    }
}
