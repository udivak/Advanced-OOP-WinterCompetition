package utilities;
import game.GameEngine;
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
import utilities.CreateGUI;
/**
 * @author Itzhak Eretz Kdosha
 * Main class(run demo)
 */
public class Program {
	private static List<WinterSportsman> winterSportsmanList = new ArrayList<>();
	private static Competition competition;
	private static WinterArena arena;
	public static void main(String[] args) {
		SwingUtilities.invokeLater(CreateGUI::new);

//		Skier skier1 = new Skier("sk1",23, Gender.MALE, 4.5,60, Discipline.DOWNHILL);
//		Skier skier2 = new Skier("sk2",25, Gender.MALE, 5.0,50, Discipline.DOWNHILL);
//		Skier skier3 = new Skier("sk3",23, Gender.FEMALE, 3.5,45, Discipline.GIANT_SLALOM);
//		Snowboarder jonsnowboarder = new Snowboarder("jon",25, Gender.FEMALE, 6.5,50, Discipline.DOWNHILL);
//		Skier skier4 = new Skier("sk4",29, Gender.MALE, 4.6,75, Discipline.DOWNHILL);
//		Skier skier5 = new Skier("sk5",50, Gender.MALE, 3.3,80, Discipline.DOWNHILL);
//
//		WinterArena arena = new WinterArena(1000,SnowSurface.CRUD,WeatherCondition.SUNNY);
//
//		SkiCompetition competition = new SkiCompetition(arena,3,Discipline.DOWNHILL, League.ADULT, Gender.MALE);
//		competition.addCompetitor(skier1);
//		competition.addCompetitor(skier2);
//		System.out.println("--------------- Exception example 1 ---------------");
//		try{
//			competition.addCompetitor(skier3);
//		}
//		catch (IllegalArgumentException e){
//			e.printStackTrace(System.out);
//		}
//		System.out.println("--------------- Exception example 2 ---------------");
//		try{
//			competition.addCompetitor(jonsnowboarder);
//		}
//		catch (IllegalArgumentException e){
//			e.printStackTrace(System.out);
//		}
//		System.out.println("--------------- Exception example 3 ---------------");
//		competition.addCompetitor(skier4);
//		try{
//			competition.addCompetitor(skier5);
//		}
//		catch (IllegalStateException e){
//			e.printStackTrace(System.out);
//		}
//		System.out.println("--------------- COMPETE ---------------");
//		GameEngine.getInstance().startRace(competition);
	}
//
private static void createAndShowGUI() {
	JFrame mainFrame = new JFrame("Arena Builder");
	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	mainFrame.setSize(600, 600);

	JPanel mainPanel = new JPanel(new BorderLayout());
	mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 5, 15, 15));
	JPanel rightPanel = new JPanel(new BorderLayout());
	rightPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 1, 0));
	rightPanel.add(createRightPanel(), BorderLayout.EAST);
	mainPanel.add(createImagePanel(), BorderLayout.WEST);
	mainPanel.add(rightPanel,  BorderLayout.CENTER);

	mainFrame.add(mainPanel);
	mainFrame.setVisible(true);
}

private static JPanel createImagePanel() {
	ImageIcon image = new ImageIcon("../../Assets/icons/icons/CLOUDY.jpg");
	JLabel imageLabel = new JLabel(image);
	imageLabel.setPreferredSize(new Dimension(image.getIconWidth(), image.getIconHeight()));

	JPanel imagePanel = new JPanel();
	imagePanel.add(imageLabel);

	return imagePanel;
}

private static JScrollPane createRightPanel() {
	JPanel rightPanel = new JPanel();
	rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
	rightPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 1, 5));

	rightPanel.add(new JLabel("Build Arena"));
	rightPanel.add(createBuildArenaPanel());
	rightPanel.add(new JLabel("Create Competition"));
	rightPanel.add(createCompetitionPanel());
	rightPanel.add(new JLabel("Add Competitor"));
	rightPanel.add(createAddCompetitorPanel());

	JScrollPane scrollPane = new JScrollPane(rightPanel);
	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	return scrollPane;
}

private static JPanel createBuildArenaPanel() {
	JPanel buildArenaPanel = new JPanel(new GridLayout(0, 1, 0, 0));

	JTextField arenaLengthTextBox = new JTextField();
	arenaLengthTextBox.setToolTipText("Enter arena length");

	JComboBox<String> snowSurfaceChoiceBox = new JComboBox<>(new String[]{"POWDER", "CRUD", "ICE"});
	JComboBox<String> weatherConditionChoiceBox = new JComboBox<>(new String[]{"SUNNY", "CLOUDY", "STORMY"});

	JButton buildArenaButton = new JButton("Build Arena");
	buildArenaButton.addActionListener(e -> {
		try {
			if (arena == null) {
				if (Integer.parseInt(arenaLengthTextBox.getText()) > 0) {
					SnowSurface snowSurface = SnowSurface.valueOf((String) snowSurfaceChoiceBox.getSelectedItem());
					int length = Integer.parseInt(arenaLengthTextBox.getText());
					WeatherCondition weatherCondition = WeatherCondition.valueOf((String) weatherConditionChoiceBox.getSelectedItem());
					arena = new WinterArena(length, snowSurface, weatherCondition);
					JOptionPane.showMessageDialog(null, "New Arena added: \nLength: " + length
							+ "\nWeather Condition: " + weatherCondition
							+ "\nSnow Surface: " + snowSurface, "Arena Added", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} catch (IllegalArgumentException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
	});

	buildArenaPanel.add(new JLabel("Arena Length:"));
	buildArenaPanel.add(arenaLengthTextBox);
	buildArenaPanel.add(new JLabel("Snow Surface:"));
	buildArenaPanel.add(snowSurfaceChoiceBox);
	buildArenaPanel.add(new JLabel("Weather Condition:"));
	buildArenaPanel.add(weatherConditionChoiceBox);
	buildArenaPanel.add(new JPanel()); // Empty cell
	buildArenaPanel.add(buildArenaButton);

	return buildArenaPanel;
}

private static JPanel createCompetitionPanel() {
	JPanel buildCompetitionPanel = new JPanel(new GridLayout(0, 1, 0, 0)); // 0 rows, 1 column

	JComboBox<String> chooseCompetitionChoiceBox = new JComboBox<>(new String[]{"Ski", "Snowboard"});
	JTextField maxCompetitorsTextBox = new JTextField();
	maxCompetitorsTextBox.setToolTipText("Max competitors number");

	JComboBox<String> disciplineChoiceBox = new JComboBox<>(new String[]{"SLALOM", "GIANT_SLALOM", "DOWNHILL", "FREESTYLE"});
	JComboBox<String> leagueChoiceBox = new JComboBox<>(new String[]{"JUNIOR", "SENIOR"});

	JLabel genderLabel = new JLabel("Gender:");
	JRadioButton maleRadioButton = new JRadioButton("Male");
	JRadioButton femaleRadioButton = new JRadioButton("Female");
	ButtonGroup genderGroup = new ButtonGroup();
	genderGroup.add(maleRadioButton);
	genderGroup.add(femaleRadioButton);
	JPanel genderPanel = new JPanel(new GridLayout(1, 2, 1, 1));
	genderPanel.add(maleRadioButton);
	genderPanel.add(femaleRadioButton);

	JButton buildCompetitionButton = new JButton("Build Competition");
	buildCompetitionButton.addActionListener(e -> {
		String competitionName = chooseCompetitionChoiceBox.getSelectedItem().toString();
		int maxCompetitors = Integer.parseInt(maxCompetitorsTextBox.getText());
		Discipline discipline = Discipline.valueOf(disciplineChoiceBox.getSelectedItem().toString());
		League league = League.valueOf(leagueChoiceBox.getSelectedItem().toString());
		Gender gender = Gender.valueOf(getSelectedGender(genderGroup));
		if (competitionName.equals("Ski")) {
			competition = new SkiCompetition(arena, maxCompetitors, discipline, league, gender);
		} else {
			competition = new SnowboardCompetition(arena, maxCompetitors, discipline, league, gender);
		}
		JOptionPane.showMessageDialog(null, "New Competition added: \nType: " + competitionName
				+ "\nMax Competitors: " + maxCompetitors
				+ "\nDiscipline: " + discipline
				+ "\nLeague: " + league
				+ "\nGender: " + gender, "Competition Added", JOptionPane.INFORMATION_MESSAGE);
	});

	buildCompetitionPanel.add(new JLabel("Choose Competition:"));
	buildCompetitionPanel.add(chooseCompetitionChoiceBox);
	buildCompetitionPanel.add(new JLabel("Max Competitors:"));
	buildCompetitionPanel.add(maxCompetitorsTextBox);
	buildCompetitionPanel.add(new JLabel("Discipline:"));
	buildCompetitionPanel.add(disciplineChoiceBox);
	buildCompetitionPanel.add(new JLabel("League:"));
	buildCompetitionPanel.add(leagueChoiceBox);
	buildCompetitionPanel.add(genderLabel);
	buildCompetitionPanel.add(genderPanel);
	buildCompetitionPanel.add(buildCompetitionButton);

	return buildCompetitionPanel;
}
private static JPanel createAddCompetitorPanel() {
	JPanel addCompetitorPanel = new JPanel(new GridLayout(5, 2, 0, 0));

	JTextField nameTextBox = new JTextField();
	nameTextBox.setToolTipText("Enter name");
	JTextField ageTextBox = new JTextField();
	ageTextBox.setToolTipText("Enter age");
	JTextField maxSpeedTextBox = new JTextField();
	maxSpeedTextBox.setToolTipText("Enter max speed");
	JTextField accelerationTextBox = new JTextField();
	accelerationTextBox.setToolTipText("Enter acceleration");

	JButton addCompetitorButton = new JButton("Add Competitor");
	addCompetitorButton.addActionListener(e -> {
		String name = nameTextBox.getText();
		int age = Integer.parseInt(ageTextBox.getText());
		double maxSpeed = Double.parseDouble(maxSpeedTextBox.getText());
		double acceleration = Double.parseDouble(accelerationTextBox.getText());

		WinterSportsman newCompetitor = new Skier(name, age, Gender.MALE, maxSpeed, acceleration, Discipline.DOWNHILL);
		winterSportsmanList.add(newCompetitor);

		JOptionPane.showMessageDialog(null, "Name: " + name + "\nAge: " + age + "\nMax Speed: " + maxSpeed + "\nAcceleration: " + acceleration, "Competitor Added", JOptionPane.INFORMATION_MESSAGE);
	});

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

	return addCompetitorPanel;
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
