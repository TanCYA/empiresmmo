package commandLine;

import java.awt.Point;

import org.apache.commons.lang.UnhandledException;

import empire.EmpireMap;
import empire.EmptyPointException;
import empire.OccupiedPointException;
import empire.Output;
import empire.PointNotInSightException;
import empire.buildings.Builder;


public class GameCommandLine {

	private final EmpireMap map;
	private final Builder builder;
	private final Output output;

	public GameCommandLine(final EmpireMap map, final Builder builder, final Output output) {
		this.map = map;
		this.builder = builder;
		this.output = output;
		command("print");
	}

	public void command(final String command) {
		output.writeLine(processCommand(command));
	}

	private String processCommand(final String command) {
		final String[] commandArgs = command.split("\\s+");
		
		final String commandName = commandArgs[0];
		if (commandName.equals("add")){
			if (commandArgs.length < 4)
				return ("Invalid number of args: add buildingName x y");
			
			final String buildingType = commandArgs[1];
			final String x = commandArgs[2];
			final String y = commandArgs[3];
			
			//TODO: this should be checked the same way OccupiedPointException and others exceptions 
			if (!builder.isBuildingType(buildingType)){
				return "Unknown building type " + buildingType + " Buildings: " + BuildingTypesPrinter.printBuildingsList(builder);
			}
			
			try {
				map.addBuilding(builder.createBuilding(buildingType), new Point(Integer.valueOf(x), Integer.valueOf(y)));
			} catch (final NumberFormatException e) {
				throw new UnhandledException(e);
			} catch (final OccupiedPointException e) {
				return "The point " + x + ", " + y + " is occupied already";
			} catch (final PointNotInSightException e) {
				return "The point " + x + ", " + y + " is not visible";
			}
			
			return "Building " + buildingType + " added in " + x + " " + y+"\n"+EmpireMapTextPrinter.printMapWithCoords(map);
		}else if (commandName.equals("remove")){
			if (commandArgs.length < 3)
				return ("Invalid number of args: remove x y");
			
			final String x = commandArgs[1];
			final String y = commandArgs[2];
			
			try {
				map.removeBuilding(new Point(Integer.valueOf(x), Integer.valueOf(y)));
			} catch (final NumberFormatException e) {
				throw new UnhandledException(e);
			} catch (final EmptyPointException e) {
				return "The point is empty";
			}
			return "Building at (" + x +","+y+") removed\n"+EmpireMapTextPrinter.printMapWithCoords(map);
		}else if (commandName.equals("print")){
			return  EmpireMapTextPrinter.printMapWithCoords(map);
		}
		
		return "Command " + commandName + " is not valid. Commands: add, print";
	}

}
