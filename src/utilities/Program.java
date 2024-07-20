package utilities;
import game.GameEngine;
import game.arena.IArena;
import game.arena.WinterArena;
import game.competition.Competition;
import game.competition.SkiCompetition;
import game.competition.SnowboardCompetition;
import game.competition.WinterCompetition;
import game.entities.sportsman.Skier;
import game.entities.sportsman.Snowboarder;
import game.entities.sportsman.WinterSportsman;
import game.enums.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Itzhak Eretz Kdosha
 * Main class(run demo)
 */
public class Program {
	private static List<WinterSportsman> winterSportsmanList = new ArrayList<>();
	private static Competition competition;
	private static WinterArena arena;
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
		// Create the frame
		JFrame frame = new JFrame("Arena Builder");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);

		// Create the main layout
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		// Right Panel for controls
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		// Build Arena section
		JLabel buildArenaTitle = new JLabel("Build Arena");
		JTextField arenaLengthTextBox = new JTextField();
		arenaLengthTextBox.setToolTipText("Enter arena length");
		JComboBox<String> snowSurfaceChoiceBox = new JComboBox<>(new String[]{"POWDER", "CRUD", "ICE"});
		JComboBox<String> weatherConditionChoiceBox = new JComboBox<>(new String[]{"SUNNY", "CLOUDY", "STORMY"});
		JButton buildArenaButton = new JButton("Build Arena");

		buildArenaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {

					if (arena == null) {
						if (Integer.parseInt(arenaLengthTextBox.getText()) > 0) {
							SnowSurface snowSurface = SnowSurface.valueOf((String) snowSurfaceChoiceBox.getSelectedItem());
							int length = Integer.parseInt(arenaLengthTextBox.getText());
							WeatherCondition weatherCondition = WeatherCondition.valueOf((String) weatherConditionChoiceBox.getSelectedItem());
							arena = new WinterArena(length, snowSurface, weatherCondition);
							String message = " new Arena added: " + "\nLength: " + length
									+ "\nWeather Condition: " + weatherCondition
									+ "\nSnow Surface: " + snowSurface;
							JOptionPane.showMessageDialog(frame, message, "Arena Added", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
				catch (IllegalArgumentException e1)
				{
					JOptionPane.showMessageDialog(frame, e1.getMessage());
				}
			}

		});

		JPanel buildArenaPanel = new JPanel(new GridLayout(4, 2, 5, 5));
		buildArenaPanel.add(new JLabel("Arena Length:"));
		buildArenaPanel.add(arenaLengthTextBox);
		buildArenaPanel.add(new JLabel("Snow Surface:"));
		buildArenaPanel.add(snowSurfaceChoiceBox);
		buildArenaPanel.add(new JLabel("Weather Condition:"));
		buildArenaPanel.add(weatherConditionChoiceBox);
		buildArenaPanel.add(new JPanel()); // Empty cell
		buildArenaPanel.add(buildArenaButton);

		// Create Competition section
		JPanel buildCompetitionPanel = new JPanel(new GridLayout(7, 2, 5, 5));
		JLabel createCompetitionTitle = new JLabel("Create Competition");
		JComboBox<String> chooseCompetitionChoiceBox = new JComboBox<>(new String[]{"Ski", "Snowboard"});
		JTextField maxCompetitorsTextBox = new JTextField();
		maxCompetitorsTextBox.setToolTipText("Max competitors number");
		JComboBox<String> disciplineChoiceBox = new JComboBox<>(new String[]{"SLALOM", "GIANT_SLALOM", "DOWNHILL", "FREESTYLE"});
		JComboBox<String> leagueChoiceBox = new JComboBox<>(new String[]{"JUNIOR", "SENIOR"});
		JButton buildCompetitionButton = new JButton("Build Competition");
		// Gender radio buttons
		JLabel genderLabel = new JLabel("Gender:");
		JRadioButton maleRadioButton = new JRadioButton("Male");
		JRadioButton femaleRadioButton = new JRadioButton("Female");
		ButtonGroup genderGroup = new ButtonGroup();
		genderGroup.add(maleRadioButton);
		genderGroup.add(femaleRadioButton);
		JPanel genderPanel = new JPanel(new GridLayout(1, 3));
		genderPanel.add(maleRadioButton);
		genderPanel.add(femaleRadioButton);
		buildCompetitionPanel.add(chooseCompetitionChoiceBox);
		buildCompetitionPanel.add(maxCompetitorsTextBox);
		buildCompetitionPanel.add(disciplineChoiceBox);
		buildCompetitionPanel.add(leagueChoiceBox);
		buildCompetitionPanel.add(genderPanel);
		buildCompetitionPanel.add(new JPanel());
		buildCompetitionPanel.add(buildCompetitionButton);
		buildCompetitionPanel.add(createCompetitionTitle);


		buildCompetitionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String competitionName = chooseCompetitionChoiceBox.getSelectedItem().toString();
				int maxCompetitors = Integer.parseInt(maxCompetitorsTextBox.getText());
				Discipline discipline = Discipline.valueOf(disciplineChoiceBox.getSelectedItem().toString());
				League league = League.valueOf(leagueChoiceBox.getSelectedItem().toString());
				Gender gender = Gender.valueOf(getSelectedGender(genderGroup));
				if (competitionName == "Ski")
					competition = new SkiCompetition(arena, maxCompetitors, discipline, league, gender);
				else
					competition = new SnowboardCompetition(arena, maxCompetitors, discipline, league, gender);
				String message = " new Competition added: " + "\nType: " + competitionName
						+ "\nMax Competitors: " + maxCompetitors
						+ "\nDiscipline: " + discipline +
						"\nLeague: " + league +
						"\nGender: " + gender;
				JOptionPane.showMessageDialog(frame, message, "Competition Added", JOptionPane.INFORMATION_MESSAGE);
			}

		});
		JPanel createCompetitionPanel = new JPanel(new GridLayout(5, 2, 5, 5));
		createCompetitionPanel.add(new JLabel("Choose Competition:"));
		createCompetitionPanel.add(chooseCompetitionChoiceBox);
		createCompetitionPanel.add(new JLabel("Max Competitors:"));
		createCompetitionPanel.add(maxCompetitorsTextBox);
		createCompetitionPanel.add(new JLabel("Discipline:"));
		createCompetitionPanel.add(disciplineChoiceBox);
		createCompetitionPanel.add(new JLabel("League:"));
		createCompetitionPanel.add(leagueChoiceBox);
		createCompetitionPanel.add(genderLabel);
		createCompetitionPanel.add(genderPanel);

		// Add Competitor section
		JLabel addCompetitorTitle = new JLabel("Add Competitor");
		JTextField nameTextBox = new JTextField();
		nameTextBox.setToolTipText("Enter name");
		JTextField ageTextBox = new JTextField();
		ageTextBox.setToolTipText("Enter age");
		JTextField maxSpeedTextBox = new JTextField();
		maxSpeedTextBox.setToolTipText("Enter max speed");
		JTextField accelerationTextBox = new JTextField();
		accelerationTextBox.setToolTipText("Enter acceleration");
		JButton addCompetitorButton = new JButton("Add Competitor");

		// Action Listener for Add Competitor button
		addCompetitorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nameTextBox.getText();
				int age = Integer.parseInt(ageTextBox.getText());
				double maxSpeed = Double.parseDouble(maxSpeedTextBox.getText());
				double acceleration = Double.parseDouble(accelerationTextBox.getText());

				WinterSportsman newCompetitor = new Skier(name, age, Gender.MALE, maxSpeed, acceleration, Discipline.DOWNHILL);
				winterSportsmanList.add(newCompetitor);



				String message = "Name: " + name + "\nAge: " + age + "\nMax Speed: " + maxSpeed + "\nAcceleration: " + acceleration;
				JOptionPane.showMessageDialog(frame, message, "Competitor Added", JOptionPane.INFORMATION_MESSAGE);
			}
		});


		JPanel addCompetitorPanel = new JPanel(new GridLayout(5, 2, 5, 5));
		addCompetitorPanel.add(new JLabel("Name:"));
		addCompetitorPanel.add(nameTextBox);
		addCompetitorPanel.add(new JLabel("Age:"));
		addCompetitorPanel.add(ageTextBox);
		addCompetitorPanel.add(new JLabel("Max Speed:"));
		addCompetitorPanel.add(maxSpeedTextBox);
		addCompetitorPanel.add(new JLabel("Acceleration:"));
		addCompetitorPanel.add(accelerationTextBox);
		addCompetitorPanel.add(new JPanel()); // Empty cell
		addCompetitorPanel.add(addCompetitorButton);

		// Add sections to right panel
		rightPanel.add(buildArenaTitle);
		rightPanel.add(buildArenaPanel);
		rightPanel.add(createCompetitionTitle);
		rightPanel.add(createCompetitionPanel);
		rightPanel.add(addCompetitorTitle);
		rightPanel.add(addCompetitorPanel);

		// Add right panel to main panel
		mainPanel.add(rightPanel, BorderLayout.EAST);

		// Add main panel to frame
		frame.add(mainPanel);
		frame.setVisible(true);
		frame.revalidate();
		frame.repaint();
	}
	private static String getSelectedGender(ButtonGroup genderGroup) {
		for (AbstractButton button : java.util.Collections.list(genderGroup.getElements())) {
			if (button.isSelected()) {
				return button.getText();
			}
		}
		return null; // Or some default value if no button is selected
	}

}
