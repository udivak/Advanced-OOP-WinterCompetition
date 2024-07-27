package utilities;
import game.arena.WinterArena;
import game.competition.*;
import game.entities.sportsman.*;
import game.enums.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

public class CompetitionGUI extends JFrame implements Observer {
    private final JLayeredPane imgContainer;      //Arena image
    private final JButton startCompetition_btn;
    private final JButton showInfo_btn;
    private JButton buildArena_btn;
    private JButton createCompetition_btn;
    private JButton addCompetitor_btn;
    private JFrame infoFrame;
    private WinterArena winterArena;
    private WinterCompetition winterCompetition;
    private WinterSportsman competitor;
    private final JTable showInfoTable;
    private final DefaultTableModel tableModel;
    private JLabel backgroundLabel;
    private final JPanel iconPanel;
    private String competition_type_str;
    private ArrayList<JLabel> iconCompetitors;
    private Vector<Thread> competitorsVector;
    private boolean competitionStatus;
    private int IWSid;
    public CompetitionGUI() {
        competitionStatus = false;
        setTitle("Competition");
        setSize(1000, 700);
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

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        startCompetition_btn = new JButton("Start competition");
        StartCompetitionEvent();                                    // sets Start Competition button functionality
        bottomPanel.add(startCompetition_btn);

        showInfo_btn = new JButton("Show info");
        showInfoTable = new JTable();
        tableModel = new DefaultTableModel(new Object[]{"Position", "Name", "Speed", "Location", "is Finished"}, 0);
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
    }
    private JPanel createBuildArenaPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.setBorder(BorderFactory.createTitledBorder("BUILD ARENA"));

        panel.add(new JLabel("Arena length"));
        JTextField arena_length_txtfield = new JTextField(10);
        arena_length_txtfield.setText("700");
        panel.add(arena_length_txtfield);

        panel.add(new JLabel("Snow surface"));
        JComboBox<SnowSurface> snow_surface = new JComboBox<>(SnowSurface.values());
        panel.add(snow_surface);

        panel.add(new JLabel("Weather condition"));
        JComboBox<WeatherCondition> weather_condition = new JComboBox<>(WeatherCondition.values());
        panel.add(weather_condition);

        mainPanel.add(panel, BorderLayout.CENTER);

        this.buildArena_btn = new JButton("Build Arena");
        mainPanel.add(buildArena_btn, BorderLayout.SOUTH);
        //Action Listener
        buildArena_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WeatherCondition selectedWeather = (WeatherCondition) weather_condition.getSelectedItem();
                SnowSurface selectedSnowSurface = (SnowSurface) snow_surface.getSelectedItem();
                try {
                    int arena_length = Integer.parseInt(arena_length_txtfield.getText());
                    if (arena_length < 700 || arena_length > 900)
                        JOptionPane.showMessageDialog(null, "Arena length must be between 700-900.",
                                                "Error", JOptionPane.ERROR_MESSAGE);
                    else {                                              //arena length is valid
                        updateMainFrameSize(1000, arena_length);
                        updateWeatherImage(selectedWeather);
                        winterArena = new WinterArena(arena_length, selectedSnowSurface, selectedWeather);
                        winterCompetition = null;
                        competitor = null;
                        JOptionPane.showMessageDialog(null, "Arena built Successfully.",
                            "BUILD ARENA", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                catch (IllegalArgumentException err) {
                    JOptionPane.showMessageDialog(null, "Arena length must be a number between 700-900.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return mainPanel;
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
        JComboBox<Discipline> disciplineJComboBox= new JComboBox<>(Discipline.values());
        panel.add(disciplineJComboBox);

        panel.add(new JLabel("League"));
        JComboBox<League> leagueJComboBox = new JComboBox<>(League.values());
        panel.add(leagueJComboBox);

        panel.add(new JLabel("Gender"));
        JComboBox<Gender> genderJComboBox = new JComboBox<>(Gender.values());
        panel.add(genderJComboBox);

        mainPanel.add(panel, BorderLayout.CENTER);
        this.createCompetition_btn = new JButton("Create Competition");
        mainPanel.add(createCompetition_btn, BorderLayout.SOUTH);
        //Action Listener
        createCompetition_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    int maxCompetitors = Integer.parseInt(max_competitors_txtfield.getText());
                    if (maxCompetitors < 1 || maxCompetitors > 20)
                        JOptionPane.showMessageDialog(null, "Max Competitors must be between 1-20.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    else {
                        iconCompetitors = new ArrayList<>();
                        if (maxCompetitors == 20)
                            updateMainFrameSize(1300, CompetitionGUI.this.getHeight());
                        Class cls;
                        ClassLoader cl = ClassLoader.getSystemClassLoader();
                        Constructor ctor;
                        cls = cl.loadClass("game.competition."+competition_type_str+"Competition");
                        ctor = cls.getConstructor(WinterArena.class, int.class, Discipline.class, League.class, Gender.class);
                        winterCompetition = (WinterCompetition) ctor.newInstance(winterArena, maxCompetitors,
                                selectedDiscipline, selectedLeague, selectedGender);
                        clearCompetitorIcons();         // Clear all competitors icons from the screen
                        competitorsVector.clear();
                        competitorsVector = new Vector<>();
                        CompetitionGUI.this.IWSid = 0;
                        CompetitionGUI.this.competitionStatus = false;
                        JOptionPane.showMessageDialog(null, "Competition built Successfully.",
                                "BUILD COMPETITION", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                catch (NumberFormatException err) {
                    JOptionPane.showMessageDialog(null, "Max Competitors must be a number.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
                         InstantiationException | IllegalAccessException ex) {
                    JOptionPane.showMessageDialog(null, "An error has occurred while loading the class.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return mainPanel;
    }
    private JPanel createAddCompetitorPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.setBorder(BorderFactory.createTitledBorder("ADD COMPETITOR"));

        panel.add(new JLabel("Name"));
        JTextField name_txtfield = new JTextField(10);
        panel.add(name_txtfield);

        panel.add(new JLabel("Age"));
        JTextField age_txtfield = new JTextField(10);
        panel.add(age_txtfield);

        panel.add(new JLabel("Max speed"));
        JTextField max_speed_txtfield = new JTextField(10);
        panel.add(max_speed_txtfield);

        panel.add(new JLabel("Acceleration"));
        JTextField acceleration_txtfield = new JTextField(10);
        panel.add(acceleration_txtfield);
        mainPanel.add(panel, BorderLayout.CENTER);

        this.addCompetitor_btn = new JButton("Add Competitor");
        mainPanel.add(addCompetitor_btn, BorderLayout.SOUTH);
        //Action Listener
        addCompetitor_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                        String competitor_icon_path = "";
                        String name_str = name_txtfield.getText();
                        int age = Integer.parseInt(age_txtfield.getText());
                        double maxSpeed = Double.parseDouble(max_speed_txtfield.getText());
                        double acceleration = Double.parseDouble(acceleration_txtfield.getText());
                        if (winterCompetition == null)
                            JOptionPane.showMessageDialog(null, "Please build Competition before building Competitor.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        else if (name_str.isEmpty())
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
                            ctor = cls.getConstructor(String.class, double.class, Gender.class, double.class, double.class, Discipline.class);
                            competitor = (WinterSportsman) ctor.newInstance(name_str, age, winterCompetition.getGender(),
                                    acceleration, maxSpeed, winterCompetition.getDiscipline());
                            IndependantWinterSportman iws = new IndependantWinterSportman(competitor, winterArena, IWSid++);
                            iws.setObserver(CompetitionGUI.this.winterCompetition);
                            iws.setObserver(CompetitionGUI.this);

                            Thread competitorThread = new Thread(iws);
                            competitorsVector.add(competitorThread);
                            winterCompetition.addCompetitor(competitor);
                            competitor_icon_path = winterCompetition.getGender() == Gender.MALE ? "/icons/" + competition_type_str + "Male.png" :
                                    "/icons/" + competition_type_str + "Female.png";
                            setCompetitorIcon(competitor_icon_path);
                            JOptionPane.showMessageDialog(null, "Competitor added Successfully.",
                                    "BUILD COMPETITION", JOptionPane.INFORMATION_MESSAGE);
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
            }
        });
        return mainPanel;
    }
    private void setShowInfoTable(JButton show_info_btn) {
        show_info_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

                updateInfoTable();
                infoFrame.setVisible(true);
            }
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
    private void updateMainFrameSize(int arenaWidth, int arenaLength) {
        this.setSize(arenaWidth, arenaLength);
        resizeBackgroundImage();
        iconPanel.setSize(arenaWidth, arenaLength);
    }
    private void resizeBackgroundImage() {
        if (backgroundLabel != null && backgroundLabel.getIcon() != null) {
            Image img = ((ImageIcon) backgroundLabel.getIcon()).getImage();
            Image resizedImg = img.getScaledInstance(imgContainer.getWidth(), imgContainer.getHeight(), Image.SCALE_SMOOTH);
            backgroundLabel.setIcon(new ImageIcon(resizedImg));
        }
    }
    private void setCompetitorIcon(String icon_path) {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(icon_path));
        Image scaledImage;
        JLabel iconLabel;
        int iconCount = iconPanel.getComponentCount();
        int x;
        int y = 10;
        ImageIcon icon_competitor;
        if (icon_path.charAt(8) == 'n') {        // Snowboard
            scaledImage = originalIcon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
            icon_competitor = new ImageIcon(scaledImage);
            iconLabel = new JLabel(icon_competitor);
            x = (iconCount * 45);
            iconLabel.setBounds(x, y, 65, 35);
        }
        else {
            scaledImage = originalIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            icon_competitor = new ImageIcon(scaledImage);
            iconLabel = new JLabel(icon_competitor);
            x = 10 + (iconCount * 35);
            iconLabel.setBounds(x, y, 35, 35);
        }
        iconCompetitors.add(iconLabel);
        iconPanel.add(iconLabel);
        iconPanel.revalidate();
        iconPanel.repaint();
    }
    private void clearCompetitorIcons() {
        iconPanel.removeAll();
        imgContainer.revalidate();
        imgContainer.repaint();
    }
    private void StartCompetitionEvent() {
        startCompetition_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (CompetitionGUI.this.winterCompetition == null || CompetitionGUI.this.competitor == null)
                    JOptionPane.showMessageDialog(null, "Please build Competition and add new Competitors before starting the Competition.",
                            "Competition Finished", JOptionPane.ERROR_MESSAGE);
                else {
                    if (!CompetitionGUI.this.competitionStatus) {
                        CompetitionGUI.this.competitionStatus = true;
                        setEnabledButtons(CompetitionGUI.this.competitionStatus);
                        for (Thread thread : competitorsVector) {
                            thread.start();
                        }
                    } else
                        JOptionPane.showMessageDialog(null, "Please rebuild Competition and add new Competitors.",
                                "Competition Finished", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    @Override
    public synchronized void update(Observable competitor, Object arg) {
        Point location = (Point) arg;
        int id = ((IndependantWinterSportman) competitor).getId();
        JLabel competitorIcon = iconCompetitors.get(id);
        competitorIcon.setBounds(competitorIcon.getX(), (int)location.getX(), competitorIcon.getWidth(), competitorIcon.getHeight());
        imgContainer.revalidate();
        imgContainer.repaint();
        // Update the info table if it's visible
        if (infoFrame != null && infoFrame.isVisible() && winterCompetition!=null &&
                (winterCompetition.hasActiveCompetitor() || winterCompetition.hasFinishedCompetitor()))
            updateInfoTable();

        if (iconCompetitors.size() == winterCompetition.getFinishedCompetitors().size()) {
            JOptionPane.showMessageDialog(null, "The Competition has finished.",
                                        "Competition Finished", JOptionPane.INFORMATION_MESSAGE);
            //CompetitionGUI.this.competitionStatus = false;
            //setEnabledButtons(false);
            setEnabledButtons(!CompetitionGUI.this.competitionStatus);
        }
    }
    private void updateInfoTable() {
        tableModel.setRowCount(0);
        try {
            int index = 1;
            for (Competitor cmp : winterCompetition.getActiveCompetitors()) {
                tableModel.addRow(new Object[]{
                        index++,
                        ((WinterSportsman) cmp).getName(),
                        ((WinterSportsman) cmp).getSpeed(),
                        ((WinterSportsman) cmp).getLocation().getX(),
                        winterArena.isFinished(cmp)
                });
            }
            for (Competitor cmp : winterCompetition.getFinishedCompetitors()) {
                tableModel.addRow(new Object[]{
                        index++,
                        ((WinterSportsman) cmp).getName(),
                        ((WinterSportsman) cmp).getSpeed(),
                        ((WinterSportsman) cmp).getLocation().getX(),
                        winterArena.isFinished(cmp)
                });
            }
        } catch (NullPointerException err) {
                JOptionPane.showMessageDialog(null, "Please build Arena, Competition and add Competitors" +
                        " to view info.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void setEnabledButtons(boolean status) {
        this.buildArena_btn.setEnabled(!status);
        this.createCompetition_btn.setEnabled(!status);
        this.addCompetitor_btn.setEnabled(!status);
        this.startCompetition_btn.setEnabled(!status);
    }
}