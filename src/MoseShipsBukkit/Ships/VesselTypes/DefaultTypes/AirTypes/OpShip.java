package MoseShipsBukkit.Ships.VesselTypes.DefaultTypes.AirTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import MoseShipsBukkit.Causes.MovementResult;
import MoseShipsBukkit.Configs.BasicConfig;
import MoseShipsBukkit.Ships.ShipsData;
import MoseShipsBukkit.Ships.Movement.StoredMovement;
import MoseShipsBukkit.Ships.Movement.AutoPilot.AutoPilot;
import MoseShipsBukkit.Ships.Movement.Movement.Rotate;
import MoseShipsBukkit.Ships.Movement.MovingBlock.MovingBlock;
import MoseShipsBukkit.Ships.VesselTypes.LoadableShip;
import MoseShipsBukkit.Ships.VesselTypes.DataTypes.Live.LiveAutoPilotable;
import MoseShipsBukkit.Ships.VesselTypes.DataTypes.Live.LiveFallable;
import MoseShipsBukkit.Ships.VesselTypes.DefaultTypes.AirType;
import MoseShipsBukkit.Ships.VesselTypes.Loading.ShipsLocalDatabase;
import MoseShipsBukkit.Ships.VesselTypes.Satic.StaticShipType;
import MoseShipsBukkit.Ships.VesselTypes.Satic.StaticShipTypeUtil;
import MoseShipsBukkit.Utils.State.BlockState;

public class OpShip extends AirType implements LiveAutoPilotable, LiveFallable {

	AutoPilot g_auto_pilot;
	
	public OpShip(String name, Block sign, Location teleport) {
		super(name, sign, teleport);
	}

	public OpShip(ShipsData ship) {
		super(ship);
	}
	
	@Override
	public Optional<AutoPilot> getAutoPilotData() {
		return Optional.ofNullable(g_auto_pilot);
	}
	
	@Override
	public OpShip setAutoPilotData(AutoPilot pilot){
		if(g_auto_pilot != null){
			g_auto_pilot.stop();
		}
		g_auto_pilot = pilot;
		return this;
	}

	@Override
	public void onRemove(Player player) {
	}

	@Override
	public Optional<MovementResult> hasRequirements(List<MovingBlock> blocks) {
		return Optional.empty();
	}

	@Override
	public boolean shouldFall() {
		return false;
	}

	@Override
	public int getMaxBlocks() {
		return 300;
	}

	@Override
	public void onSave(ShipsLocalDatabase database) {
	}

	@Override
	public int getMinBlocks() {
		return 0;
	}

	@Override
	public Map<String, Object> getInfo() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (g_user == null) {
			map.put("Owner", "None");
		} else {
			map.put("Owner", g_user.getName());
		}
		map.put("size", updateBasicStructure().size());
		map.put("type", "OPShip");
		map.put("is loaded", this.isLoaded());
		map.put("is moving", this.isMoving());
		return map;
	}

	@Override
	public StaticShipType getStatic() {
		return StaticShipTypeUtil.getType(StaticOPShip.class).get();
	}

	@Override
	public Optional<MovementResult> move(BlockFace dir, int speed, BlockState... movingTo) {
		return super.move(dir, speed, new BlockState(Material.AIR));
	}

	@Override
	public Optional<MovementResult> rotate(Rotate type, BlockState... movingTo) {
		return super.rotate(type, new BlockState(Material.AIR));
	}

	@Override
	public Optional<MovementResult> rotateRight(BlockState... movingTo) {
		return super.rotateRight(new BlockState(Material.AIR));
	}

	@Override
	public Optional<MovementResult> rotateLeft(BlockState... movingTo) {
		return super.rotateLeft(new BlockState(Material.AIR));
	}

	@Override
	public Optional<MovementResult> teleport(Location loc, BlockState... movingTo) {
		return super.teleport(loc, new BlockState(Material.AIR));
	}

	@Override
	public Optional<MovementResult> teleport(StoredMovement move, BlockState... movingTo) {
		return super.teleport(move, new BlockState(Material.AIR));
	}

	@Override
	public Optional<MovementResult> teleport(Location loc, int X, int Y, int Z, BlockState... movingTo) {
		return super.teleport(loc, X, Y, Z, new BlockState(Material.AIR));
	}

	public static class StaticOPShip implements StaticShipType {

		public StaticOPShip() {
			StaticShipTypeUtil.inject(this);
		}

		@Override
		public String getName() {
			return "OPShip";
		}

		@Override
		public int getDefaultSpeed() {
			return 2;
		}

		@Override
		public int getBoostSpeed() {
			return 2;
		}

		@Override
		public int getAltitudeSpeed() {
			return 2;
		}

		@Override
		public boolean autoPilot() {
			return true;
		}

		@Override
		public Optional<LoadableShip> createVessel(String name, Block sign) {
			return Optional.of((LoadableShip) new OpShip(name, sign, sign.getLocation()));
		}

		@Override
		public Optional<LoadableShip> loadVessel(ShipsData data, BasicConfig config) {
			return Optional.of((LoadableShip) new OpShip(data));
		}

	}
}