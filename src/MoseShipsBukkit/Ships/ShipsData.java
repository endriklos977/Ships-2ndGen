package MoseShipsBukkit.Ships;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;

import MoseShips.CustomDataHolder.DataHolder;

import MoseShipsBukkit.BlockFinder.BlockFinderUtils;
import MoseShipsBukkit.Configs.Files.ShipsConfig;
import MoseShipsBukkit.Utils.LocationUtils;

public class ShipsData extends DataHolder {

	public static final String DATABASE_NAME = "ShipsMeta.Name";
	public static final String DATABASE_TYPE = "ShipsMeta.Type";
	public static final String DATABASE_PILOT = "ShipsMeta.Pilot";
	public static final String DATABASE_SUB_PILOTS = "ShipsMeta.Sub_Pilots";
	public static final String DATABASE_WORLD = "ShipsMeta.Location.World";
	public static final String DATABASE_BLOCK = "ShipsMeta.Location.Block";
	public static final String DATABASE_TELEPORT = "ShipsMeta.Location.Teleport";
	public static final String DATABASE_STRUCTURE = "ShipsStructure.BasicStructure";

	protected String g_name;
	protected OfflinePlayer g_user;
	protected List<OfflinePlayer> SUB_PILOTS = new ArrayList<OfflinePlayer>();
	protected List<Block> STRUCTURE = new ArrayList<Block>();
	protected Block g_main;
	protected Location TELEPORT;

	public ShipsData(String name, Block sign, Location teleport) {
		g_name = name;
		g_main = sign;
		if (teleport == null) {
			TELEPORT = sign.getLocation();
		} else {
			TELEPORT = teleport;
		}
	}

	public ShipsData(ShipsData data) {
		data.cloneOnto(this);
	}

	public Optional<Sign> getLicence() {
		if (g_main.getState() instanceof Sign) {
			return Optional.of((Sign) g_main.getState());
		}
		return Optional.empty();
	}

	public String getName() {
		return g_name;
	}

	public Optional<OfflinePlayer> getOwner() {
		return Optional.ofNullable(g_user);
	}

	public ShipsData setOwner(OfflinePlayer user) {
		g_user = user;
		return this;
	}

	public List<OfflinePlayer> getSubPilots() {
		return SUB_PILOTS;
	}

	public World getWorld() {
		return g_main.getWorld();
	}

	public Map<String, String> getBasicData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Name: ", g_name);
		map.put("Licence: ", g_main.getX() + ", " + g_main.getY() + ", " + g_main.getZ() + ", " + g_main.getWorld().getName());
		map.put("Teleport: ", TELEPORT.getX() + ", " + TELEPORT.getY() + ", " + TELEPORT.getZ() + ", " + TELEPORT.getWorld().getName());
		map.put("Structure size:", STRUCTURE.size() + "");
		return map;
	}

	public List<Block> getBasicStructure() {
		return STRUCTURE;
	}

	public List<Entity> getEntities() {
		List<Entity> entities = new ArrayList<Entity>();
		for (Entity entity : g_main.getWorld().getEntities()) {
			Block block = entity.getLocation().getBlock().getRelative(BlockFace.DOWN);
			if (STRUCTURE.contains(block)) {
				entities.add(entity);
			}
		}
		return entities;
	}

	public boolean hasLocation(Location loc) {
		if (!loc.getWorld().equals(getWorld())) {
			return false;
		}
		for (Block block : STRUCTURE) {
			if (LocationUtils.blocksEqual(block.getLocation(), loc)) {
				return true;
			}
		}
		return false;
	}

	public List<Block> updateBasicStructure() {
		Integer trackLimit = ShipsConfig.CONFIG.get(Integer.class,
				ShipsConfig.PATH_STRUCTURE_STRUCTURELIMITS_TRACKLIMIT);
		if (trackLimit == null) {
			trackLimit = 5000;
		}
		List<Block> list = BlockFinderUtils.getConfigSelected().getConnectedBlocks(trackLimit, g_main);
		STRUCTURE = list;
		return list;
	}

	public List<Block> setBasicStructure(List<Block> locs, Block licence) {
		STRUCTURE = locs;
		g_main = licence;
		return locs;
	}

	public List<Block> setBasicStructure(List<Block> locs, Block licence, Location teleport) {
		STRUCTURE = locs;
		g_main = licence;
		TELEPORT = teleport;
		return locs;
	}

	public Location getLocation() {
		return g_main.getLocation();
	}

	public Location getTeleportToLocation() {
		return TELEPORT;
	}

	public ShipsData setTeleportToLocation(Location loc) {
		TELEPORT = loc;
		return this;
	}

	public ShipsData cloneOnto(ShipsData data) {
		data.g_main = this.g_main;
		data.g_name = this.g_name;
		data.STRUCTURE = this.STRUCTURE;
		data.SUB_PILOTS = this.SUB_PILOTS;
		data.TELEPORT = this.TELEPORT;
		data.g_user = this.g_user;
		data.DATA = this.DATA;
		return data;
	}

}