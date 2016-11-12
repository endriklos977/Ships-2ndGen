package MoseShipsBukkit.BlockFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import MoseShipsBukkit.BlockFinder.Algorithms.Prototype3;
import MoseShipsBukkit.BlockFinder.Algorithms.Prototype4;
import MoseShipsBukkit.Configs.Files.ShipsConfig;
import MoseShipsBukkit.Signs.ShipsSigns;
import MoseShipsBukkit.Signs.ShipsSigns.SignType;

public class BlockFinderUtils {

	public static final BasicBlockFinder SHIPS5 = new Prototype3();
	public static final BasicBlockFinder SHIPS6 = new Prototype4();

	public static BasicBlockFinder getConfigSelected() {
		String name = ShipsConfig.CONFIG.get(String.class, ShipsConfig.PATH_ALGORITHMS_BLOCKFINDER);
		Optional<BasicBlockFinder> opBlock = getFinder(name);
		if (opBlock.isPresent()) {
			return opBlock.get();
		}
		return SHIPS6;
	}

	public static boolean isValid(List<Block> list) {
		for (Block block : list) {
			if (block.getState() instanceof Sign) {
				Sign sign = (Sign) block.getState();
				Optional<SignType> opType = ShipsSigns.getSignType(sign);
				if (opType.isPresent()) {
					if (opType.get().equals(SignType.LICENCE)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static List<BasicBlockFinder> getFinders() {
		List<BasicBlockFinder> finder = new ArrayList<BasicBlockFinder>(BasicBlockFinder.LIST);
		finder.add(SHIPS5);
		finder.add(SHIPS6);
		return finder;
	}

	public static Optional<BasicBlockFinder> getFinder(String name) {
		for (BasicBlockFinder finder : BasicBlockFinder.LIST) {
			System.out.println(finder.getName() + " | " + name);
			if (finder.getName().equalsIgnoreCase(name)) {
				return Optional.of(finder);
			}
		}
		return Optional.empty();
	}

}