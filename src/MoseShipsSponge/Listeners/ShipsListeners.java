package MoseShipsSponge.Listeners;

import java.util.Map;
import java.util.Optional;

import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import MoseShipsSponge.ShipsMain;
import MoseShipsSponge.Ships.VesselTypes.ShipType;
import MoseShipsSponge.Signs.ShipsSigns;
import MoseShipsSponge.Signs.ShipsSigns.SignTypes;

public class ShipsListeners {

	public void playerInteractEvent(InteractBlockEvent event, @Root Player player) {
		Optional<Location<World>> opLoc = event.getTargetBlock().getLocation();
		if (opLoc.isPresent()) {
			Location<World> loc = opLoc.get();
			Optional<TileEntity> opEntity = loc.getTileEntity();
			if (opEntity.isPresent()) {
				TileEntity tile = opEntity.get();
				if (tile instanceof Sign) {
					Sign sign = (Sign) tile;
					Optional<SignTypes> opSignType = ShipsSigns.getSignType(sign);
					if (opSignType.isPresent()) {
						if (event instanceof InteractBlockEvent.Secondary) {
							Optional<ShipType> opShipType = ShipType.getShip(opSignType.get(), sign);
							if (opShipType.isPresent()) {
								ShipType ship = opShipType.get();
								switch (opSignType.get()) {
									case EOT:

										break;
									case LICENCE:
										Map<Text, Object> info = ship.getInfo();
										player.sendMessage(ShipsMain.format("Information about " + ship.getName(), false));
										info.entrySet().forEach(e -> player.sendMessage(e.getKey()));
										break;
									case MOVE:
										
										break;
									case WHEEL:
										break;
								}
							}
						}
					}
				}
			}
		}
	}

}
