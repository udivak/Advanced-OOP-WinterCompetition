package utilities;
import game.GameEngine;
import game.arena.WinterArena;
import game.competition.SkiCompetition;
import game.competition.WinterCompetition;
import game.entities.sportsman.Skier;
import game.entities.sportsman.Snowboarder;
import game.enums.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * @author Itzhak Eretz Kdosha
 * Main class(run demo)
 */
public class Program {

	public static void main(String[] args) {
		CreateGUI();
		Skier skier1 = new Skier("sk1",23, Gender.MALE, 4.5,60, Discipline.DOWNHILL);
		Skier skier2 = new Skier("sk2",25, Gender.MALE, 5.0,50, Discipline.DOWNHILL);
		Skier skier3 = new Skier("sk3",23, Gender.FEMALE, 3.5,45, Discipline.GIANT_SLALOM);
		Snowboarder jonsnowboarder = new Snowboarder("jon",25, Gender.FEMALE, 6.5,50, Discipline.DOWNHILL);
		Skier skier4 = new Skier("sk4",29, Gender.MALE, 4.6,75, Discipline.DOWNHILL);
		Skier skier5 = new Skier("sk5",50, Gender.MALE, 3.3,80, Discipline.DOWNHILL);

		WinterArena arena = new WinterArena(1000,SnowSurface.CRUD,WeatherCondition.SUNNY);

		SkiCompetition competition = new SkiCompetition(arena,3,Discipline.DOWNHILL, League.ADULT, Gender.MALE);
		competition.addCompetitor(skier1);
		competition.addCompetitor(skier2);
		System.out.println("--------------- Exception example 1 ---------------");
		try{
			competition.addCompetitor(skier3);
		}
		catch (IllegalArgumentException e){
			e.printStackTrace(System.out);
		}
		System.out.println("--------------- Exception example 2 ---------------");
		try{
			competition.addCompetitor(jonsnowboarder);
		}
		catch (IllegalArgumentException e){
			e.printStackTrace(System.out);
		}
		System.out.println("--------------- Exception example 3 ---------------");
		competition.addCompetitor(skier4);
		try{
			competition.addCompetitor(skier5);
		}
		catch (IllegalStateException e){
			e.printStackTrace(System.out);
		}
		System.out.println("--------------- COMPETE ---------------");
		GameEngine.getInstance().startRace(competition);
	}
	public static void CreateGUI() {
		JFrame frame = new JFrame("Competition");
		frame.setSize(1000, 700);

		JLabel SnowSurface_label = new JLabel("Snow Surface");
		SnowSurface_label.setSize(80, 80);
		frame.add(SnowSurface_label);
		GridLayout myGrid = new GridLayout(3,2);
		frame.setLayout(myGrid);
		frame.add(new JComboBox<SnowSurface>(SnowSurface.values()));
		final JLabel label = new JLabel("Make Competition");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(label);
		//frame.pack();
		frame.setVisible(true);
	}
}
