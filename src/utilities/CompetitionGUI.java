package utilities;

import game.arena.ArenaFactory;
import game.arena.WinterArena;
import game.competition.*;
import game.entities.sportsman.*;
import game.enums.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * @authors Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class CompetitionGUI - a class that handles the GUI and multi-threads program
 */
public class CompetitionGUI extends JFrame implements Observer {
    private final JLayeredPane imgContainer;      //Arena image
    private final JButton startCompetition_btn;
    private final JButton showInfo_btn;
    private JButton buildArena_btn;
    private JButton createCompetition_btn;
    private JButton defaultCompetition_btn;
    private JButton addCompetitor_btn;
    private JButton cloneCompetitor_btn;
    private JButton decorateCompetitor_btn;
    private JFrame infoFrame;
    private WinterArena winterArena;
    private WinterCompetition winterCompetition;
    private int maxCompetitors;
    private WinterSportsman competitor;
    private final JTable showInfoTable;
    private DefaultTableModel tableModel;
    private JLabel backgroundLabel;
    private final JPanel iconPanel;
    private String competition_type_str;
    private final ArrayList<JLabel> iconCompetitors;
    private final Vector<Thread> competitorsVector;
    private boolean competitionStatus;
    private int IWSid;
    private JComboBox<String> arenaTypeComboBox;
    private double arena_length;
    private JComboBox<SnowSurface> snow_surface;
    private JComboBox<WeatherCondition> weather_condition;
    private ExecutorService executer;
    private Queue<IndependantWinterSportman> threadPool;
    private int ARENA_WIDTH = 1000;
    private volatile boolean isCompetitionFinished = false;
    private JFrame decoratorFrame;
    private JPanel decoratorPanel;
    private JButton addColoredIWS_btn;
    private JButton addSpeedyIWS_btn;
    private JButton addDefaultIWS_btn;
    private JTextField name_txtfield;
    private JTextField age_txtfield;
    private JTextField acceleration_txtfield;
    private JTextField maxSpeed_txtfield;
    private ArrayList<IWinterSportman> iwsList;
    private long competitionStartTime;
    private long injurySystemTime;
    private volatile boolean is_competition_finished;

    public CompetitionGUI() {
        competitionStatus = false;
        setTitle("Competition");
        setSize(ARENA_WIDTH, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel(new BorderLayout());

        imgContainer = new JLayeredPane();
        imgContainer.setLayout(new BorderLayout());
        imgContainer.setBackground(Color.white);
        imgContainer.setOpaque(true);
        //adding new panel to load the competitors above the image
        iconPanel = new JPanel(null);
        iconPanel.setOpaque(false);
        imgContainer.add(iconPanel, BorderLayout.CENTER);

        // BUILD ARENA section
        JPanel buildArenaPanel = createBuildArenaPanel();
        // CREATE COMPETITION section
        JPanel createCompetitionPanel = createCreateCompetitionPanel();
        // ADD COMPETITOR section
        JPanel addCompetitorPanel = createAddCompetitorPanel();
        this.IWSid = 0;
        // Combine all panels
        JPanel rightPanel = new JPanel(new GridLayout(4, 1));
        rightPanel.add(buildArenaPanel);
        rightPanel.add(createCompetitionPanel);
        rightPanel.add(addCompetitorPanel);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        startCompetition_btn = new JButton("Start Competition");
        StartCompetitionEvent();                                    // sets Start Competition button functionality
        bottomPanel.add(startCompetition_btn);

        showInfo_btn = new JButton("Show info");
        showInfoTable = new JTable();
//        tableModel = new DefaultTableModel(new Object[]{"Position", "Name", "Speed", "Location", "is Finished", "State"}, 0);
        tableModel = new DefaultTableModel(new Object[]{"Position", "Name", "Speed", "Location", "State"}, 0);
        showInfoTable.setModel(tableModel);
        setShowInfoTable(showInfo_btn);
        bottomPanel.add(showInfo_btn);
        TableColumnModel columnModel = showInfoTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(20);             // Position Column

        rightPanel.add(bottomPanel);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(imgContainer, BorderLayout.CENTER);
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                resizeBackgroundImage();
            }
        });
        add(mainPanel);
        competitorsVector = new Vector<>();
        iwsList = new ArrayList<>();
        iconCompetitors = new ArrayList<>();
        threadPool = new LinkedList<>();
    }

    private JPanel createBuildArenaPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.setBorder(BorderFactory.createTitledBorder("BUILD ARENA"));

        panel.add(new JLabel("Arena Type"));
        arenaTypeComboBox = new JComboBox<>();
        arenaTypeComboBox.addItem("Winter");
        arenaTypeComboBox.addItem("Summer");
        panel.add(arenaTypeComboBox);

        panel.add(new JLabel("Arena length"));
        JTextField arena_length_txtfield = new JTextField(10);
        arena_length_txtfield.setText("700");
        panel.add(arena_length_txtfield);

        panel.add(new JLabel("Snow surface"));
        snow_surface = new JComboBox<>(SnowSurface.values());
        panel.add(snow_surface);

        panel.add(new JLabel("Weather condition"));
        weather_condition = new JComboBox<>(WeatherCondition.values());
        panel.add(weather_condition);

        mainPanel.add(panel, BorderLayout.CENTER);

        this.buildArena_btn = new JButton("Build Arena");
        mainPanel.add(buildArena_btn, BorderLayout.SOUTH);
        //Action Listener
        buildArena_btn.addActionListener(e -> {
            ArenaFactory arenaFactory = new ArenaFactory();
            String arenaType = (String) arenaTypeComboBox.getSelectedItem();
            WeatherCondition selectedWeather = (WeatherCondition) weather_condition.getSelectedItem();
            SnowSurface selectedSnowSurface = (SnowSurface) snow_surface.getSelectedItem();
            try {
                arena_length = Double.parseDouble(arena_length_txtfield.getText());
                if (arena_length < 700 || arena_length > 900)
                    JOptionPane.showMessageDialog(null, "Arena length must be between 700-900.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                else {                                              //arena length is valid
                    if (arenaType.equals("Winter")) {
                        updateMainFrameSize(ARENA_WIDTH, arena_length);
                        updateWeatherImage(selectedWeather);
                    }
//                        winterArena = new WinterArena(arena_length, selectedSnowSurface, selectedWeather);
                    winterArena = (WinterArena) arenaFactory.createArena((String) arenaTypeComboBox.getSelectedItem(), arena_length, selectedSnowSurface, selectedWeather);
                    winterCompetition = null;
                    competitor = null;
                    if (winterArena != null)
                        JOptionPane.showMessageDialog(null, "Arena built Successfully.",
                                "BUILD ARENA", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IllegalArgumentException err) {
                JOptionPane.showMessageDialog(null, "Arena length must be a number between 700-900.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return mainPanel;
    }

    private ArrayList<Integer> destiny() {
        ArrayList<Integer> competitorArray = new ArrayList<>();
        Random random = new Random();
        int firstInt = random.nextInt(winterCompetition.getActiveCompetitors().size());
        int secondInt;
        do {
            secondInt = random.nextInt(winterCompetition.getActiveCompetitors().size());
        } while (secondInt == firstInt);

        competitorArray.add(firstInt);
        competitorArray.add(secondInt);

        return competitorArray;
    }

    private JPanel createCreateCompetitionPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.setBorder(BorderFactory.createTitledBorder("CREATE COMPETITION"));

        panel.add(new JLabel("Choose competition"));
        JComboBox<String> competition_type = new JComboBox<>(new String[]{"Ski", "Snowboard"});
        panel.add(competition_type);

        panel.add(new JLabel("Max competitors number"));
        JTextField max_competitors_txtfield = new JTextField(10);
        max_competitors_txtfield.setText("10");
        panel.add(max_competitors_txtfield);

        panel.add(new JLabel("Discipline"));
        JComboBox<Discipline> disciplineJComboBox = new JComboBox<>(Discipline.values());
        panel.add(disciplineJComboBox);

        panel.add(new JLabel("League"));
        JComboBox<League> leagueJComboBox = new JComboBox<>(League.values());
        panel.add(leagueJComboBox);

        panel.add(new JLabel("Gender"));
        JComboBox<Gender> genderJComboBox = new JComboBox<>(Gender.values());
        panel.add(genderJComboBox);

        mainPanel.add(panel, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        this.createCompetition_btn = new JButton("Create Competition");
        this.defaultCompetition_btn = new JButton("Create Default Competition");
        buttonsPanel.add(defaultCompetition_btn);
        buttonsPanel.add(createCompetition_btn);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        //Action Listener
        createCompetition_btn.addActionListener(e -> {
            //
            iwsList.clear();
            iconCompetitors.clear();
            IWSid = 0;
            competitionStatus = true;
            competitorsVector.clear();
            //
            competition_type_str = (String) competition_type.getSelectedItem();
            Discipline selectedDiscipline = (Discipline) disciplineJComboBox.getSelectedItem();
            League selectedLeague = (League) leagueJComboBox.getSelectedItem();
            Gender selectedGender = (Gender) genderJComboBox.getSelectedItem();
            try {
                if (winterArena == null) {
                    JOptionPane.showMessageDialog(null, "Please build Arena before building Competition.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                maxCompetitors = Integer.parseInt(max_competitors_txtfield.getText());
                if (maxCompetitors < 1 || maxCompetitors > 20)
                    JOptionPane.showMessageDialog(null, "Max Competitors must be between 1-20.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                else {
                    if (maxCompetitors > 14) {
                        ARENA_WIDTH = 1300;
                        updateMainFrameSize(ARENA_WIDTH, CompetitionGUI.this.getHeight());
                    }
                    Class cls;
                    ClassLoader cl = ClassLoader.getSystemClassLoader();
                    Constructor ctor;

                    cls = cl.loadClass("game.competition." + competition_type_str + "Competition");
                    ctor = cls.getConstructor(WinterArena.class, int.class, Discipline.class, League.class, Gender.class);
                    winterCompetition = (WinterCompetition) ctor.newInstance(winterArena, maxCompetitors,
                            selectedDiscipline, selectedLeague, selectedGender);
                    clearCompetitorIcons();         // Clear all competitors icons from the screen
                    competitorsVector.clear();
                    CompetitionGUI.this.IWSid = 0;
                    CompetitionGUI.this.competitionStatus = false;
                    JOptionPane.showMessageDialog(null, "Competition built Successfully.",
                            "BUILD COMPETITION", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Max Competitors must be a number.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
                     InstantiationException | IllegalAccessException ex) {
                JOptionPane.showMessageDialog(null, "An error has occurred while loading the class.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        defaultCompetition_btn.addActionListener(e -> {
//                CompetitionGUI gui = CompetitionGUI.this;
//                gui.winterCompetition.clearActiveCompetitors();
//                gui.competitorsVector.clear();
//                IWSid = 0;
//                gui.clearCompetitorIcons();
//                System.out.println(gui.competitorsVector.size());
//                System.out.println(gui.winterCompetition.getActiveCompetitors().size());
//                JOptionPane.showMessageDialog(null, "All Competitors removed.", "Clear Competitors", JOptionPane.INFORMATION_MESSAGE);
            //
            iwsList.clear();
            iconCompetitors.clear();
            IWSid = 0;
            competitionStatus = true;
            //
            String input = JOptionPane.showInputDialog(null, "Enter number of maximum competitors :", "Input Max Competitors", JOptionPane.QUESTION_MESSAGE);
            try {
                maxCompetitors = Integer.parseInt(input);
            } catch (NumberFormatException error) {
                maxCompetitors = 10;
                JOptionPane.showMessageDialog(null, "No number written, 10 competitors added.", "Default Competition", JOptionPane.INFORMATION_MESSAGE);
            }
            if (maxCompetitors < 1 || maxCompetitors > 20)
                JOptionPane.showMessageDialog(null, "Max Competitors must be between 1-20.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            else {
                if (maxCompetitors > 14) {
                    ARENA_WIDTH = 1350;
                    updateMainFrameSize(ARENA_WIDTH, 700);
                }
                CompetitionBuilder competitionBuilder = new CompetitionBuilder(maxCompetitors);
                updateWeatherImage(WeatherCondition.SUNNY);
                competitionBuilder.buildArena("Winter", 700, SnowSurface.POWDER, WeatherCondition.SUNNY);
                winterArena = new WinterArena(700, SnowSurface.POWDER, WeatherCondition.SUNNY);
                competitionBuilder.changeCompetitionType("Ski");
                competition_type_str = "Ski";
                competitionBuilder.buildDiscipline(Discipline.DOWNHILL);
                competitionBuilder.buildLeague(League.JUNIOR);
                competitionBuilder.buildGender(Gender.MALE);
                competitionBuilder.buildDefaultCompetitors();
                winterCompetition = (WinterCompetition) competitionBuilder.getCompetition();
                clearCompetitorIcons();         // Clear all competitors icons from the screen

                competitorsVector.clear();
                for (Competitor comp : winterCompetition.getActiveCompetitors()) {
                    (CompetitionGUI.this).addCompetitorIWS((WinterSportsman) comp, IWSid++);
                }
                IWSid = 0;
                competitionStatus = false;
                JOptionPane.showMessageDialog(null, "Default Competition was built successfully.", "Default Competition", JOptionPane.INFORMATION_MESSAGE);
                decoratorFrame.setVisible(false);
            }
        });
        return mainPanel;
    }

    private void addCompetitorIWS(WinterSportsman competitor, int id) {
        IndependantWinterSportman iws = new IndependantWinterSportman(competitor, winterArena, id);
        iws.setObserver(CompetitionGUI.this.winterCompetition);
        iws.setObserver(CompetitionGUI.this);
        iwsList.add(iws);
        //
        threadPool.add(iws);
        //
//        Thread competitorThread = new Thread(iws);
//        competitorsVector.add(competitorThread);
        if (winterCompetition.getActiveCompetitors().size() < winterCompetition.getMaxCompetitors())
            winterCompetition.addCompetitor(competitor);
        String competitor_icon_path;
        competitor_icon_path = winterCompetition.getGender() == Gender.MALE ? "/icons/" + competition_type_str + "Male.png" :
                "/icons/" + competition_type_str + "Female.png";
        setCompetitorIcon(competitor_icon_path, null);
    }

    private void addColoredWS(WinterSportsman competitor, int id, Color color) {
        IndependantWinterSportman iws = new IndependantWinterSportman(competitor, winterArena, id);
        iws.setObserver(CompetitionGUI.this.winterCompetition);
        iws.setObserver(CompetitionGUI.this);
//        iwsList.add(iws);
        ColoredSportman coloredIWS = new ColoredSportman(iws, color);
        iwsList.add(coloredIWS);
//        Thread competitorThread = new Thread(coloredIWS);
//        competitorsVector.add(competitorThread);

        if (winterCompetition.getActiveCompetitors().size() < winterCompetition.getMaxCompetitors())
            winterCompetition.addCompetitor(competitor);
        String competitor_icon_path;
        competitor_icon_path = winterCompetition.getGender() == Gender.MALE ? "/icons/" + competition_type_str + "Male.png" :
                "/icons/" + competition_type_str + "Female.png";
        setCompetitorIcon(competitor_icon_path, color);
    }

    private void addSpeedyWS(WinterSportsman competitor, int id, double acceleration) {
        IndependantWinterSportman iws = new IndependantWinterSportman(competitor, winterArena, id);
        iws.setObserver(CompetitionGUI.this.winterCompetition);
        iws.setObserver(CompetitionGUI.this);
//        iwsList.add(iws);
        SpeedySportman speedyIWS = new SpeedySportman(iws, acceleration);
        iwsList.add(speedyIWS);
//        Thread competitorThread = new Thread(speedyIWS);
//        competitorsVector.add(competitorThread);

        if (winterCompetition.getActiveCompetitors().size() < winterCompetition.getMaxCompetitors())
            winterCompetition.addCompetitor(competitor);
        String competitor_icon_path;
        competitor_icon_path = winterCompetition.getGender() == Gender.MALE ? "/icons/" + competition_type_str + "Male.png" :
                "/icons/" + competition_type_str + "Female.png";
        setCompetitorIcon(competitor_icon_path, null);
    }

    private JPanel createAddCompetitorPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel addCompetitorForm = new JPanel(new GridLayout(0, 2));
        addCompetitorForm.setBorder(BorderFactory.createTitledBorder("ADD COMPETITOR"));

        addCompetitorForm.add(new JLabel("Name"));
        name_txtfield = new JTextField(10);
        addCompetitorForm.add(name_txtfield);

        addCompetitorForm.add(new JLabel("Age"));
        age_txtfield = new JTextField(10);
        addCompetitorForm.add(age_txtfield);

        addCompetitorForm.add(new JLabel("Max speed"));
        maxSpeed_txtfield = new JTextField(10);
        addCompetitorForm.add(maxSpeed_txtfield);

        addCompetitorForm.add(new JLabel("Acceleration"));
        acceleration_txtfield = new JTextField(10);
        addCompetitorForm.add(acceleration_txtfield);
        mainPanel.add(addCompetitorForm, BorderLayout.CENTER);

        decoratorFrame = new JFrame();
        decoratorFrame.setSize(300, 100);
        decoratorPanel = new JPanel(new FlowLayout());

        addColoredIWS_btn = new JButton("Colored");
        addColoredIWS_btn.addActionListener(e -> {
            try {
                competitor = buildWinterSportman();
                CompetitionGUI gui = CompetitionGUI.this;
                if (gui.winterCompetition.getActiveCompetitors().size() == gui.winterCompetition.getMaxCompetitors())
                    JOptionPane.showMessageDialog(null, "The Competition is full.", "Error", JOptionPane.ERROR_MESSAGE);

                else {
                    Color chosenColor = JColorChooser.showDialog(decoratorPanel, "Choose a Color", Color.WHITE);
                    gui.addColoredWS(competitor, IWSid, chosenColor);
                    IWSid++;
                    JOptionPane.showMessageDialog(null, "Competitor added Successfully.",
                            "Build Competitor", JOptionPane.INFORMATION_MESSAGE);
                    decoratorFrame.setVisible(false);
                }
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Age, Max Speed, Acceleration must be numbers.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalStateException err) {
                JOptionPane.showMessageDialog(null, err.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addSpeedyIWS_btn = new JButton("Speedy");
        addSpeedyIWS_btn.addActionListener(e -> {
            competitor = buildWinterSportman();
            String newAccelerationStr;
            double newAcceleration = 0;
            do {
                newAccelerationStr = JOptionPane.showInputDialog(CompetitionGUI.this, "Enter Acceleration for the competitor:", "New Acceleration", JOptionPane.QUESTION_MESSAGE);
                try {
                    newAcceleration = Double.parseDouble(newAccelerationStr);
                    JOptionPane.showMessageDialog(this, "Acceleration updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ignored) {
                }
            } while (newAccelerationStr == null && newAccelerationStr.isEmpty());

            CompetitionGUI gui = CompetitionGUI.this;
            if (gui.winterCompetition.getActiveCompetitors().size() == gui.winterCompetition.getMaxCompetitors()) {
                JOptionPane.showMessageDialog(null, "The Competition is full.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                gui.addSpeedyWS(competitor, IWSid, newAcceleration);
                IWSid++;
                JOptionPane.showMessageDialog(null, "Competitor added Successfully.",
                        "Build Competitor", JOptionPane.INFORMATION_MESSAGE);
                decoratorFrame.setVisible(false);
            }
        });

        addDefaultIWS_btn = new JButton("Default");
        decoratorPanel.add(addColoredIWS_btn);
        decoratorPanel.add(addSpeedyIWS_btn);
        decoratorPanel.add(addDefaultIWS_btn);
        decoratorFrame.add(decoratorPanel);

        JPanel addCompetitor_panel = new JPanel(new FlowLayout());
        this.addCompetitor_btn = new JButton("Add Competitor");
        addCompetitor_btn.addActionListener(e -> {
            if (winterArena == null || winterCompetition == null)
                JOptionPane.showMessageDialog(null, "Please build Arena, Competition before adding Competitors.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            else if (winterCompetition.getActiveCompetitors().size() == winterCompetition.getMaxCompetitors())
                JOptionPane.showMessageDialog(null, "This Competition is full.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            else if (isAddCompetitorFormEmpty())
                JOptionPane.showMessageDialog(null, "Please fill in all the data for the Competitor.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            else if (CompetitionGUI.this.competitionStatus)      //there is a finished competition - force user to create a new competition, then add competitors
                JOptionPane.showMessageDialog(null, "Please rebuild Competition before adding Competitors.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            else
                decoratorFrame.setVisible(true);
        });

        this.cloneCompetitor_btn = new JButton("Clone Competitor");
        cloneCompetitor_btn.addActionListener(e -> {
            if (winterCompetition.getActiveCompetitors().size() == winterCompetition.getMaxCompetitors())
                JOptionPane.showMessageDialog(null, "This Competition is full.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            else {
                if (CompetitionGUI.this.winterCompetition.getActiveCompetitors().isEmpty())
                    JOptionPane.showMessageDialog(null, "Please build competitor before Cloning.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                else {
                    JDialog activeCompetitorsDialog = new JDialog();
                    JButton cloneBtn = new JButton("Clone");
                    JButton cancelBtn = new JButton("Cancel");
                    cancelBtn.addActionListener(e1 -> {
                        activeCompetitorsDialog.dispose();      // Close the dialog
                    });
                    JComboBox<IWinterSportman> comboBox = getCompetitorsComboBox();

                    cloneBtn.addActionListener(e12 -> {
                        Object selectedCompetitor = comboBox.getSelectedItem();
                        WinterSportsman cmp;
                        if (selectedCompetitor instanceof WSDecorator) {
                            cmp = (WinterSportsman) ((WSDecorator) selectedCompetitor).getIWS().getCompetitor();
                        } else {
                            cmp = (WinterSportsman) ((IndependantWinterSportman) selectedCompetitor).getCompetitor();
                        }
                        competitor = cmp.clone();
                        // now open dialog for modifications
                        JDialog modificationsDialog = new JDialog();
                        modificationsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        JPanel itemsPanel = new JPanel(new BorderLayout());
                        JPanel idPanel = new JPanel(new GridLayout(1, 1));
                        JLabel idLabel = new JLabel("Enter new ID :");
                        JTextField idTextField = new JTextField(5);
                        JPanel colorPanel = new JPanel();
                        JPanel buttonsPanel = new JPanel(new FlowLayout());
                        Color chosenColor = JColorChooser.showDialog(colorPanel, "Choose a Color", Color.WHITE);
//                        JLabel colorLabel = new JLabel();
//                        if (chosenColor != null)
//                            colorLabel.setText(String.format("Color Chosen : %s", chosenColor));
                        JButton doneBtn = new JButton("Done");
                        JButton cancelBtn1 = new JButton("Cancel");
                        cancelBtn1.addActionListener(e121 -> {
                            modificationsDialog.dispose();
                            activeCompetitorsDialog.dispose();
                        });
                        doneBtn.addActionListener(e1212 -> {
                            try {
                                int id = Integer.parseInt(idTextField.getText());
                                competitor.setID(id);
                                (CompetitionGUI.this).addColoredWS(competitor, IWSid++, chosenColor);
                                modificationsDialog.dispose();
                                activeCompetitorsDialog.dispose();
                            } catch (NumberFormatException err) {
                                JOptionPane.showMessageDialog(null, "ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                                modificationsDialog.dispose();
                                activeCompetitorsDialog.dispose();
                            }
                        });
                        buttonsPanel.add(doneBtn);
                        buttonsPanel.add(cancelBtn1);
                        idPanel.add(idLabel);
                        idPanel.add(idTextField);
                        itemsPanel.add(idPanel, BorderLayout.NORTH);
//                        itemsPanel.add(colorLabel, BorderLayout.CENTER);
                        itemsPanel.add(buttonsPanel, BorderLayout.SOUTH);
                        modificationsDialog.add(itemsPanel);
                        modificationsDialog.pack();
                        modificationsDialog.setVisible(true);
                    });
                    cancelBtn.addActionListener(e13 -> {
                        activeCompetitorsDialog.dispose(); // Close the dialog
                    });
                    activeCompetitorsDialog.add(comboBox, BorderLayout.CENTER);
                    JPanel buttonsPanel = new JPanel(new FlowLayout());
                    buttonsPanel.add(cloneBtn);
                    buttonsPanel.add(cancelBtn);
                    activeCompetitorsDialog.add(buttonsPanel, BorderLayout.SOUTH);
                    activeCompetitorsDialog.pack();
                    activeCompetitorsDialog.setVisible(true);
                }
            }
        });

        this.decorateCompetitor_btn = new JButton("Decorate Competitor");
        decorateCompetitor_btn.addActionListener(l -> {
            JDialog activeCompetitorsDialog = new JDialog();
            JComboBox<IWinterSportman> comboBox = getCompetitorsComboBox();

            JPanel decoratorTypesPanel = new JPanel(new FlowLayout());

            JButton speedy_btn = new JButton("Speedy");
            speedy_btn.addActionListener(e23 -> {
                IWinterSportman selectedCompetitor = (IWinterSportman) comboBox.getSelectedItem();
                String newAccelerationStr;
                Double newAcceleration = 0.0;
                do {
                    // Prompt the user for input
                    newAccelerationStr = JOptionPane.showInputDialog(CompetitionGUI.this,
                            "Enter Acceleration for the competitor:", "New Acceleration", JOptionPane.QUESTION_MESSAGE);

                    // Check if the user pressed Cancel or closed the dialog
                    if (newAccelerationStr == null) {
                        int confirm = JOptionPane.showConfirmDialog(CompetitionGUI.this,
                                "Are you sure you want to cancel?", "Cancel Confirmation", JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);

                        if (confirm == JOptionPane.YES_OPTION) {
                            JOptionPane.showMessageDialog(CompetitionGUI.this, "Decoration canceled.",
                                    "Canceled", JOptionPane.INFORMATION_MESSAGE);
                            return;     // Exit the loop if the user confirms cancellation

                        } else {
                            continue;  // Continue the loop if the user decides not to cancel
                        }
                    }

                    // Check if the input is empty
                    if (newAccelerationStr.isEmpty()) {
                        JOptionPane.showMessageDialog(
                                CompetitionGUI.this,
                                "Input cannot be empty.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                        continue;  // Skip to the next iteration of the loop
                    }

                    try {
                        // Try to parse the input to a double
                        newAcceleration = Double.parseDouble(newAccelerationStr);
                        JOptionPane.showMessageDialog(
                                CompetitionGUI.this,
                                "Acceleration updated successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        break;  // Exit the loop if the input is valid
                    } catch (NumberFormatException ex) {
                        // Handle invalid number format
                        JOptionPane.showMessageDialog(
                                CompetitionGUI.this,
                                "Invalid input. Please enter a valid number.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } while (true);  // Loop until a valid input is provided

                int index = winterCompetition.getActiveCompetitors().indexOf(((IWinterSportman) comboBox.getSelectedItem()).getIWS().getCompetitor());
                if (selectedCompetitor instanceof WSDecorator) {
                    selectedCompetitor = new SpeedySportman((WSDecorator) selectedCompetitor, newAcceleration);
                } else
                    selectedCompetitor = new SpeedySportman(selectedCompetitor, newAcceleration);
                winterCompetition.getActiveCompetitors().set(index, selectedCompetitor.getIWS().getCompetitor());
                JOptionPane.showMessageDialog(null, "Decoration Succeed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                activeCompetitorsDialog.dispose();
            });

            JButton colored_btn = new JButton("Colored");
            colored_btn.addActionListener(e21 -> {
                IWinterSportman selectedCompetitor = (IWinterSportman) comboBox.getSelectedItem();
                Color chosenColor = JColorChooser.showDialog(null, "Choose a Color", Color.WHITE);
                if (chosenColor == null) {
                    int confirm = JOptionPane.showConfirmDialog(CompetitionGUI.this,
                            "Are you sure you want to cancel?", "Cancel Confirmation", JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(CompetitionGUI.this, "Decoration canceled.",
                                "Canceled", JOptionPane.INFORMATION_MESSAGE);
                        return;     // Exit the loop if the user confirms cancellation
                    }
                }
                int index = winterCompetition.getActiveCompetitors().indexOf(((IWinterSportman) comboBox.getSelectedItem()).getIWS().getCompetitor());
                if (selectedCompetitor instanceof WSDecorator) {
                    selectedCompetitor = new ColoredSportman((WSDecorator) selectedCompetitor, chosenColor);
                } else
                    selectedCompetitor = new ColoredSportman(selectedCompetitor, chosenColor);
                winterCompetition.getActiveCompetitors().set(index, selectedCompetitor.getIWS().getCompetitor());
                String competitor_icon_path;
                competitor_icon_path = winterCompetition.getGender() == Gender.MALE ? "/icons/" + competition_type_str + "Male.png" :
                        "/icons/" + competition_type_str + "Female.png";
                modifyCompetitorIcon(index, chosenColor, competitor_icon_path);
                JOptionPane.showMessageDialog(null, "Decoration Succeed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                activeCompetitorsDialog.dispose();
            });

            activeCompetitorsDialog.add(comboBox, BorderLayout.CENTER);
            decoratorTypesPanel.add(speedy_btn);
            decoratorTypesPanel.add(colored_btn);
            activeCompetitorsDialog.add(decoratorTypesPanel, BorderLayout.SOUTH);
            activeCompetitorsDialog.pack();
            activeCompetitorsDialog.setVisible(true);
        });

        addCompetitor_panel.add(cloneCompetitor_btn);
        addCompetitor_panel.add(decorateCompetitor_btn);
        addCompetitor_panel.add(addCompetitor_btn);
        mainPanel.add(addCompetitor_panel, BorderLayout.SOUTH);
        //Action Listener - Add Competitor
        addDefaultIWS_btn.addActionListener(e -> {
            if (CompetitionGUI.this.competitionStatus)      //there is a finished competition - force user to create a new competition, then add competitors
                JOptionPane.showMessageDialog(null, "Please rebuild Competition before adding Competitors.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            else {
                try {
                    if (winterArena == null || winterCompetition == null) {
                        JOptionPane.showMessageDialog(null, "Please build Arena, Competition before adding Competitors.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    //
                    competitor = buildWinterSportman();
                    //
//                    String name_str = name_txtfield.getText();
//                    int age = Integer.parseInt(age_txtfield.getText());
//                    double maxSpeed = Double.parseDouble(maxSpeed_txtfield.getText());
//                    double acceleration = Double.parseDouble(acceleration_txtfield.getText());
//                    if (winterCompetition == null)
//                        JOptionPane.showMessageDialog(null, "Please build Competition before building Competitor.",
//                                "Error", JOptionPane.ERROR_MESSAGE);
//                    else if (name_str.isEmpty())
//                        JOptionPane.showMessageDialog(null, "Please insert competitor's name.",
//                                "Error", JOptionPane.ERROR_MESSAGE);
//                    else if (!winterCompetition.getLeague().isInLeague(age))
//                        JOptionPane.showMessageDialog(null, "Age is not valid for the current Competition.",
//                                "Error", JOptionPane.ERROR_MESSAGE);
//                    else {
//                        Class cls;
//                        ClassLoader cl = ClassLoader.getSystemClassLoader();
//                        Constructor ctor;
//
//                        cls = cl.loadClass("game.entities.sportsman." + competition_type_str + "er");
//                        ctor = cls.getConstructor(String.class, double.class, Gender.class, double.class, double.class, Discipline.class, int.class);
//                        competitor = (WinterSportsman) ctor.newInstance(name_str, age, winterCompetition.getGender(),
//                                acceleration, maxSpeed, winterCompetition.getDiscipline(), IWSid);
                    CompetitionGUI gui = CompetitionGUI.this;
                    if (gui.winterCompetition.getActiveCompetitors().size() == gui.winterCompetition.getMaxCompetitors()) {
                        JOptionPane.showMessageDialog(null, "The Competition is full.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        gui.addCompetitorIWS(competitor, IWSid);
                        IWSid++;
                        JOptionPane.showMessageDialog(null, "Competitor added Successfully.",
                                "Build Competitor", JOptionPane.INFORMATION_MESSAGE);
                        decoratorFrame.setVisible(false);
                    }
                } catch (NumberFormatException err) {
                    JOptionPane.showMessageDialog(null, "Age, Max Speed, Acceleration must be numbers.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalStateException err) {
                    JOptionPane.showMessageDialog(null, err.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return mainPanel;
    }

    private void modifyCompetitorIcon(int index, Color newColor, String icon_path) {
        if (index < 0 || index >= iconCompetitors.size()) {
            System.out.println("Invalid index: " + index);
            return;
        }

        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(getClass().getResource(icon_path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;  // Exit the method if the image can't be loaded
        }

        BufferedImage finalImage = originalImage;
        if (newColor != null) {
            // Create a new BufferedImage to store the recolored image
            BufferedImage coloredImage = new BufferedImage(
                    originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            // Replace colors in the image
            for (int y = 0; y < originalImage.getHeight(); y++) {
                for (int x = 0; x < originalImage.getWidth(); x++) {
                    int pixel = originalImage.getRGB(x, y);
                    int alpha = (pixel >> 24) & 0xff;

                    if (alpha > 0) { // Only change non-transparent pixels
                        coloredImage.setRGB(x, y, (newColor.getRGB() & 0x00FFFFFF) | (alpha << 24));
                    } else {
                        coloredImage.setRGB(x, y, pixel); // Preserve transparency
                    }
                }
            }
            finalImage = coloredImage;
        }

        Image scaledImage;
        if (icon_path.charAt(8) == 'n') {  // Snowboard
            scaledImage = finalImage.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        } else {
            scaledImage = finalImage.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        }

        // Update the existing icon
        JLabel iconLabel = iconCompetitors.get(index);
        iconLabel.setIcon(new ImageIcon(scaledImage));

        iconPanel.revalidate();
        iconPanel.repaint();
    }

    private boolean isAddCompetitorFormEmpty() {
        String name = name_txtfield.getText();
        String age = age_txtfield.getText();
        String acc = acceleration_txtfield.getText();
        String max = maxSpeed_txtfield.getText();
        return name.isEmpty() || age.isEmpty() || acc.isEmpty() || max.isEmpty();
    }

    private JComboBox<IWinterSportman> getCompetitorsComboBox() {
        JComboBox<IWinterSportman> comboBox = new JComboBox<>();
        for (IWinterSportman iws : iwsList) {
            comboBox.addItem(iws);
        }
        return comboBox;
    }

    private WinterSportsman buildWinterSportman() {
        if (!isAddCompetitorFormEmpty()) {
            try {
                String name_str = name_txtfield.getText();
                int age = Integer.parseInt(age_txtfield.getText());
                if (name_str.isEmpty())
                    JOptionPane.showMessageDialog(null, "Please insert competitor's name.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                else if (!this.winterCompetition.getLeague().isInLeague(age))
                    JOptionPane.showMessageDialog(null, "Age is not valid for the current Competition.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                else {
                    double maxSpeed = Double.parseDouble(maxSpeed_txtfield.getText());
                    double acceleration = Double.parseDouble(acceleration_txtfield.getText());
                    if (name_str.isEmpty())
                        JOptionPane.showMessageDialog(null, "Please insert competitor's name.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    else if (!winterCompetition.getLeague().isInLeague(age))
                        JOptionPane.showMessageDialog(null, "Age is not valid for the current Competition.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    else {
                        Class cls;
                        ClassLoader cl = ClassLoader.getSystemClassLoader();
                        Constructor ctor;

                        cls = cl.loadClass("game.entities.sportsman." + competition_type_str + "er");
                        ctor = cls.getConstructor(String.class, double.class, Gender.class, double.class, double.class, Discipline.class, int.class);
                        competitor = (WinterSportsman) ctor.newInstance(name_str, age, winterCompetition.getGender(),
                                acceleration, maxSpeed, winterCompetition.getDiscipline(), IWSid);
                        return competitor;
                    }
                }
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(null, "Age, Max Speed, Acceleration must be numbers.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalStateException err) {
                JOptionPane.showMessageDialog(null, err.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
                     InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;                    //if competitor building process went wrong
    }

    private void setShowInfoTable(JButton show_info_btn) {
        show_info_btn.addActionListener(e -> {
            if (infoFrame == null) {
                infoFrame = new JFrame("Competitors Information");
                infoFrame.setSize(600, 400);
                JScrollPane showInfoTable_scroll = new JScrollPane(showInfoTable);
                infoFrame.add(showInfoTable_scroll);
                infoFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        infoFrame = null;
                    }
                });
            }
            try {
                updateInfoTable();
            } catch (Exception ignored) {
            }
            infoFrame.setVisible(true);
        });
    }

    private void updateWeatherImage(WeatherCondition weather) {
        String imagePath = switch (weather) {
            case SUNNY -> "/icons/Sunny.jpg";
            case CLOUDY -> "/icons/Cloudy.jpg";
            case STORMY -> "/icons/Stormy.jpg";
        };
        setBackground(imagePath);
    }

    private void setBackground(String img_path) {
        if (backgroundLabel == null) {
            backgroundLabel = new JLabel();
            imgContainer.add(backgroundLabel, BorderLayout.CENTER);
        }
        ImageIcon icon = new ImageIcon(CompetitionGUI.class.getResource(img_path));
        Image img = icon.getImage();
        int width = imgContainer.getWidth();
        int height = imgContainer.getHeight();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(resizedImg));
    }

    private void updateMainFrameSize(int arenaWidth, double arenaLength) {
        CompetitionGUI.this.setSize(arenaWidth, (int) arenaLength);
        resizeBackgroundImage();
        CompetitionGUI.this.iconPanel.setSize(arenaWidth, (int) arenaLength);
    }

    private void resizeBackgroundImage() {
        if (backgroundLabel != null && backgroundLabel.getIcon() != null) {
            Image img = ((ImageIcon) backgroundLabel.getIcon()).getImage();
            Image resizedImg = img.getScaledInstance(imgContainer.getWidth(), imgContainer.getHeight(), Image.SCALE_SMOOTH);
            backgroundLabel.setIcon(new ImageIcon(resizedImg));
        }
    }

    private void setCompetitorIcon(String icon_path, Color newColor) {
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(getClass().getResource(icon_path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;                     // Exit the method if the image can't be loaded
        }
        BufferedImage finalImage = originalImage;
        if (newColor != null) {
            // Create a new BufferedImage to store the recolored image
            BufferedImage coloredImage = new BufferedImage(
                    originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            // Replace colors in the image
            for (int y = 0; y < originalImage.getHeight(); y++) {
                for (int x = 0; x < originalImage.getWidth(); x++) {
                    int pixel = originalImage.getRGB(x, y);
                    int alpha = (pixel >> 24) & 0xff;

                    if (alpha > 0) { // Only change non-transparent pixels
                        coloredImage.setRGB(x, y, (newColor.getRGB() & 0x00FFFFFF) | (alpha << 24));
                    } else {
                        coloredImage.setRGB(x, y, pixel); // Preserve transparency
                    }
                }
            }
            finalImage = coloredImage;
        }

        Image scaledImage;
        JLabel iconLabel;
        int iconCount = iconPanel.getComponentCount();
        int x;
        int y = 10;
        ImageIcon icon_competitor;

        if (icon_path.charAt(8) == 'n') {       // Snowboard
            scaledImage = finalImage.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
            icon_competitor = new ImageIcon(scaledImage);
            iconLabel = new JLabel(icon_competitor);
            x = (iconCount * 55);
            iconLabel.setBounds(x, y, 65, 35);
        } else {
            scaledImage = finalImage.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            icon_competitor = new ImageIcon(scaledImage);
            iconLabel = new JLabel(icon_competitor);
            x = 10 + (iconCount * 45);
            iconLabel.setBounds(x, y, 35, 35);
        }

        iconCompetitors.add(iconLabel);
        iconPanel.add(iconLabel);
        iconPanel.revalidate();
        iconPanel.repaint();
    }

    private void clearCompetitorIcons() {
        iconCompetitors.clear();
        iconPanel.removeAll();
        imgContainer.revalidate();
        imgContainer.repaint();
    }

    private void StartCompetitionEvent() {
        startCompetition_btn.addActionListener(e -> {
            if (CompetitionGUI.this.winterCompetition == null || CompetitionGUI.this.winterCompetition.getActiveCompetitors().isEmpty())
                JOptionPane.showMessageDialog(null, "Please build Competition and add new Competitors before starting the Competition.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            else {
                if (!CompetitionGUI.this.competitionStatus) {
                    CompetitionGUI.this.competitionStatus = true;
                    setEnabledButtons(CompetitionGUI.this.competitionStatus);

                    ArrayList<Integer> statesIndex = destiny();
                    int first = statesIndex.getFirst();
                    IndependantWinterSportman modifiedIWS = iwsList.get(first).getIWS();
                    modifiedIWS.setStatus(1);
                    iwsList.set(first, modifiedIWS);

                    int last = statesIndex.getLast();
                    modifiedIWS = iwsList.get(last).getIWS();
                    modifiedIWS.setStatus(2);
                    iwsList.set(last, modifiedIWS);

                    for (IWinterSportman iws : iwsList) {
                        Thread iwsThread = new Thread(iws.getIWS());
                        competitorsVector.add(iwsThread);
                    }

                    competitionStartTime = System.currentTimeMillis();
                    for (Thread thread : competitorsVector) {                                  // WITHOUT THREAD POOL
                        thread.start();
                    }
                    isCompetitionFinished = false;
                } else
                    JOptionPane.showMessageDialog(null, "Please rebuild Competition and add new Competitors.",
                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    @Override
    public synchronized void update(Observable competitor, Object arg) {
        int id = ((IndependantWinterSportman) competitor).getID();
        String competitorName = ((IndependantWinterSportman) competitor).getCompetitor().getName();
        if (arg instanceof CompetitorState) {
            if (arg instanceof InjuredCompetitor) {
                injurySystemTime = System.currentTimeMillis();
                long timeUntilInjury = injurySystemTime - competitionStartTime;
                updateInfoTable();
                JOptionPane.showMessageDialog(null, String.format("%s got Injured at %d millis.", competitorName, timeUntilInjury), "Injured Competitor", JOptionPane.WARNING_MESSAGE);
            } else if (arg instanceof DisabledCompetitor)                                  // Disabled
                JOptionPane.showMessageDialog(null, String.format("%s is Disabled", competitorName), "Disabled Competitor", JOptionPane.WARNING_MESSAGE);
            else if (arg instanceof ActiveCompetitor) {
                JOptionPane.showMessageDialog(null, String.format("%s is back to Competition", competitorName), "Active Competitor", JOptionPane.WARNING_MESSAGE);

            }
        } else {
            Point location = (Point) arg;
            JLabel competitorIcon = iconCompetitors.get(id);
            competitorIcon.setBounds(competitorIcon.getX(), (int) location.getX(), competitorIcon.getWidth(), competitorIcon.getHeight());
            imgContainer.revalidate();
            imgContainer.repaint();
            // Update the info table if it's visible
            if (infoFrame != null && infoFrame.isVisible() && winterCompetition != null &&
                    (winterCompetition.hasActiveCompetitor() || winterCompetition.hasFinishedCompetitor())) {
                try {
                    updateInfoTable();
                } catch (Exception ignored) {
                }
            }
        }
        is_competition_finished = isCompetitionFinished();
        if (is_competition_finished) {
            isCompetitionFinished = true;  // Set the flag to prevent further execution
            JOptionPane.showMessageDialog(null, "The Competition has finished.",
                    "Competition Finished", JOptionPane.INFORMATION_MESSAGE);
            setEnabledButtons(!competitionStatus);
//            iwsList.clear();
//            competitorsVector.clear();
//            iconCompetitors.clear();
//            IWSid = 0;
//            competitionStatus = true;
        }

    }

    private void updateInfoTable() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            try {
                int index = 1;
                for (IWinterSportman iws : iwsList) {
                    Competitor cmp = iws.getIWS().getCompetitor();
                    if (cmp instanceof WinterSportsman WScmp) {
                        String state;
                        /// Handle with 2 type of iws
                        if (iws instanceof WSDecorator)
                            state = iws.getIWS().getState().toString();
                        else {
                            state = ((IndependantWinterSportman) iws).getState().toString();
                        }
                        tableModel.addRow(new Object[]{
                                index++,
                                WScmp.getName(),
                                WScmp.getSpeed(),
                                WScmp.getLocation().getX(),
//                                winterArena.isFinished(cmp),
                                state,
                        });


                    }
                }
//                for (Competitor cmp : winterCompetition.getFinishedCompetitors()) {
//                    if (cmp instanceof WinterSportsman) {
//                        tableModel.addRow(new Object[]{
//                                index++,
//                                ((WinterSportsman) cmp).getName(),
//                                ((WinterSportsman) cmp).getSpeed(),
//                                ((WinterSportsman) cmp).getLocation().getX(),
//                                winterArena.isFinished(cmp),
//                                "--State--"
//                        });
//                    }
//                }
            } catch (NullPointerException err) {
                JOptionPane.showMessageDialog(null, "Please build Arena, Competition and add Competitors to view info.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void setEnabledButtons(boolean status) {
        this.buildArena_btn.setEnabled(!status);
        this.createCompetition_btn.setEnabled(!status);
        this.addCompetitor_btn.setEnabled(!status);
        this.startCompetition_btn.setEnabled(!status);
        this.cloneCompetitor_btn.setEnabled(!status);
        this.defaultCompetition_btn.setEnabled(!status);
        this.decorateCompetitor_btn.setEnabled(!status);
    }

    private synchronized boolean isCompetitionFinished() {
        int count_finished = 0;
        int count_injured = 0;
        for (IWinterSportman iws : iwsList) {
            if (iws.getIWS().isFinished())
                count_finished++;
            if (Objects.equals(iws.getIWS().getState().toString(), "Injured"))
                count_injured++;
        }
        return (count_finished == (iwsList.size() - 2) && count_injured == 1) || (count_finished == (iwsList.size() - 1) && count_injured == 0);
    }
}