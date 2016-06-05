package MoseShipsSponge.Ships.VesselTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import com.flowpowered.math.vector.Vector3i;

import MoseShipsSponge.Causes.FailedCause;
import MoseShipsSponge.Ships.ShipsData;
import MoseShipsSponge.Ships.Movement.Movement;
import MoseShipsSponge.Ships.Movement.Movement.Rotate;
import MoseShipsSponge.Ships.Movement.MovingBlock;
import MoseShipsSponge.Ships.Utils.StoredMovement;
import MoseShipsSponge.Ships.VesselTypes.DataTypes.LiveData;
import MoseShipsSponge.Signs.ShipsSigns.SignTypes;

public abstract class ShipType extends ShipsData {

	public abstract Optional<FailedCause> hasRequirements(List<MovingBlock> blocks, @Nullable User user);

	public abstract boolean shouldFall();

	public abstract int getMaxBlocks();

	public abstract int getMinBlocks();

	public abstract Map<Text, Object> getInfo();

	static List<ShipType> SHIPS = new ArrayList<>();

	public ShipType(String name, User host, Location<World> sign, Location<World> teleport) {
		super(name, host, sign, teleport);
	}
	
	public ShipType(ShipsData data){
		super(data);
	}
	
	public Optional<FailedCause> move(StoredMovement move){
		return Movement.move(this, move);
	}
	
	public Optional<FailedCause> move(Vector3i moveBy){
		return Movement.move(this, moveBy);
	}
	
	public Optional<FailedCause> move(int X, int Y, int Z){
		return Movement.move(this, X, Y, Z);
	}
	
	public Optional<FailedCause> rotateLeft(){
		return Movement.rotateLeft(this);
	}
	
	public Optional<FailedCause> rotateRight(){
		return Movement.rotateRight(this);
	}
	
	public Optional<FailedCause> rotate(Rotate type){
		return Movement.rotate(this, type);
	}
	
	@SuppressWarnings("unchecked")
	public Optional<FailedCause> teleport(Location<? extends Extent> loc){
		Location<World> loc2 = null;
		if(loc.getExtent() instanceof Chunk){
			Chunk chunk = (Chunk)loc.getExtent();
			loc2 = chunk.getWorld().getLocation(loc.getBlockPosition());
		}else{
			loc2 = (Location<World>)loc;
		}
		return Movement.teleport(this, loc2);
	}
	
	@SuppressWarnings("unchecked")
	public Optional<FailedCause> teleport(Location<? extends Extent> loc, int X, int Y, int Z){
		Location<World> loc2 = null;
		if(loc.getExtent() instanceof Chunk){
			Chunk chunk = (Chunk)loc.getExtent();
			loc2 = chunk.getWorld().getLocation(loc.getBlockPosition());
		}else{
			loc2 = (Location<World>)loc;
		}
		return Movement.teleport(this, loc2, X, Y, Z);
	}

	public static Optional<ShipType> getShip(String name) {
		return SHIPS.stream().filter(s -> s.getName().equals(name)).findFirst();
	}

	public static Optional<ShipType> getShip(Text text) {
		return getShip(text.toPlain());
	}

	public static Optional<ShipType> getShip(SignTypes type, Sign sign) {
		if (type.equals(SignTypes.LICENCE)) {
			Text text = sign.lines().get(2);
			return getShip(text.toPlain());
		}
		return Optional.empty();
	}

	@SuppressWarnings("unchecked")
	public static Optional<ShipType> getShip(Location<? extends Extent> loc) {
		if (loc.getExtent() instanceof Chunk) {
			Chunk chunk = (Chunk) loc.getExtent();
			loc = chunk.getWorld().getLocation(loc.getBlockPosition());
		}
		final Location<World> loc2 = (Location<World>) loc;
		return SHIPS.stream().filter(s -> s.getBasicStructure().stream().anyMatch(b -> (b.getBlockPosition().equals(loc2.getBlockPosition())) && (b.getExtent().equals(loc2.getExtent())))).findAny();
	}

	public static List<ShipType> getShips() {
		return SHIPS;
	}

	@SuppressWarnings("unchecked")
	public static <T extends ShipType> List<T> getShips(Class<T> type) {
		return (List<T>) SHIPS.stream().filter(s -> type.isInstance(s)).collect(Collectors.toList());
	}

	public static List<ShipType> getShipsByRequirements(Class<? extends LiveData> type) {
		return SHIPS.stream().filter(s -> type.isInstance(s)).collect(Collectors.toList());
	}

}
