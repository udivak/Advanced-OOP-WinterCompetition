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
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
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
    private JFrame infoFrame;
    private WinterArena winterArena;
    private WinterCompetition winterCompetition;
    private int maxCompetitors;
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
    JTextField arena_type_txtfield;
    private double arena_length;
    JComboBox<SnowSurface> snow_surface;
    JComboBox<WeatherCondition> weather_condition;

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
        tableModel = new DefaultTableModel(new Object[]{"Position", "Name", "ID", "Speed", "Location", "is Finished"}, 0);
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
        iconCompetitors = new ArrayList<>();
    }

    private JPanel createBuildArenaPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.setBorder(BorderFactory.createTitledBorder("BUILD ARENA"));

        panel.add(new JLabel("Arena Type"));
        arena_type_txtfield = new JTextField(10);
        arena_type_txtfield.setText("Winter");
        panel.add(arena_type_txtfield);

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
        buildArena_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArenaFactory arenaFactory = new ArenaFactory();
                WeatherCondition selectedWeather = (WeatherCondition) weather_condition.getSelectedItem();
                SnowSurface selectedSnowSurface = (SnowSurface) snow_surface.getSelectedItem();
                try {
                    arena_length = Double.parseDouble(arena_length_txtfield.getText());
                    if (arena_length < 700 || arena_length > 900)
                        JOptionPane.showMessageDialog(null, "Arena length must be between 700-900.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    else {                                              //arena length is valid
                        updateMainFrameSize(1000, arena_length);
                        updateWeatherImage(selectedWeather);
//                        winterArena = new WinterArena(arena_length, selectedSnowSurface, selectedWeather);
                        winterArena = (WinterArena) arenaFactory.createArena(arena_type_txtfield.getText(), arena_length, selectedSnowSurface, selectedWeather);
                        winterCompetition = null;
                        competitor = null;
                        JOptionPane.showMessageDialog(null, "Arena built Successfully.",
                                "BUILD ARENA", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IllegalArgumentException err) {
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
                    maxCompetitors = Integer.parseInt(max_competitors_txtfield.getText());
                    if (maxCompetitors < 1 || maxCompetitors > 20)
                        JOptionPane.showMessageDialog(null, "Max Competitors must be between 1-20.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    else {

                        if (maxCompetitors == 20)
                            updateMainFrameSize(1300, CompetitionGUI.this.getHeight());
                        Class cls;
                        ClassLoader cl = ClassLoader.getSystemClassLoader();
                        Constructor ctor;

                        cls = cl.loadClass("game.competition." + competition_type_str + "Competition");
                        ctor = cls.getConstructor(WinterArena.class, int.class, Discipline.class, League.class, Gender.class);
                        winterCompetition = (WinterCompetition) ctor.newInstance(winterArena, maxCompetitors,
                                selectedDiscipline, selectedLeague, selectedGender);
//                        CompetitionBuilder competitionBuilder = new CompetitionBuilder(maxCompetitors);
//                        competitionBuilder.buildArena(arena_type_txtfield.getText(), arena_length,
//                                (SnowSurface) snow_surface.getSelectedItem(), (WeatherCondition) weather_condition.getSelectedItem());
//                        competitionBuilder.changeCompetitionType(competition_type_str);
//                        competitionBuilder.buildDiscipline(selectedDiscipline);
//                        competitionBuilder.buildLeague(selectedLeague);
//                        competitionBuilder.buildGender(selectedGender);
//                        winterCompetition = (WinterCompetition) competitionBuilder.getCompetition();
                        clearCompetitorIcons();         // Clear all competitors icons from the screen
                        competitorsVector.clear();
//                        for (Competitor comp : winterCompetition.getActiveCompetitors()) {
//                            (CompetitionGUI.this).addCompetitorIWS((WinterSportsman) comp, IWSid++, null);
//                        }
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
            }
        });
        defaultCompetition_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                CompetitionGUI gui = CompetitionGUI.this;
//                gui.winterCompetition.clearActiveCompetitors();
//                gui.competitorsVector.clear();
//                IWSid = 0;
//                gui.clearCompetitorIcons();
//                System.out.println(gui.competitorsVector.size());
//                System.out.println(gui.winterCompetition.getActiveCompetitors().size());
//                JOptionPane.showMessageDialog(null, "All Competitors removed.", "Clear Competitors", JOptionPane.INFORMATION_MESSAGE);
                maxCompetitors = 10;
                CompetitionBuilder competitionBuilder = new CompetitionBuilder(maxCompetitors);
//                competitionBuilder.buildArena(arena_type_txtfield.getText(), arena_length,
//                        (SnowSurface) snow_surface.getSelectedItem(), (WeatherCondition) weather_condition.getSelectedItem());
                updateMainFrameSize(1000, 700);
                updateWeatherImage(WeatherCondition.SUNNY);
                competitionBuilder.buildArena("Winter", 700, SnowSurface.CRUD, WeatherCondition.SUNNY);
                winterArena = new WinterArena(700, SnowSurface.CRUD, WeatherCondition.SUNNY);
                competitionBuilder.changeCompetitionType("Ski");
                competition_type_str = "Ski";
                competitionBuilder.buildDiscipline(Discipline.DOWNHILL);
                competitionBuilder.buildLeague(League.JUNIOR);
                competitionBuilder.buildGender(Gender.MALE);
                competitionBuilder.buildDefaultCompetitors();
                winterCompetition = (WinterCompetition) competitionBuilder.getCompetition();
                System.out.println("active competitors"+ winterCompetition.getActiveCompetitors().size());
                clearCompetitorIcons();         // Clear all competitors icons from the screen
                competitorsVector.clear();

                for (Competitor comp : winterCompetition.getActiveCompetitors()) {
                    (CompetitionGUI.this).addCompetitorIWS((WinterSportsman) comp, IWSid++, null);
                }
                IWSid = 0;
                competitionStatus = false;
                JOptionPane.showMessageDialog(null, "Default Competition was built successfully.", "Default Competition", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return mainPanel;
    }

    private void addCompetitorIWS(WinterSportsman competitor, int id, Color color) {
        IndependantWinterSportman iws = new IndependantWinterSportman(competitor, winterArena, id, color);
        iws.setObserver(CompetitionGUI.this.winterCompetition);
        iws.setObserver(CompetitionGUI.this);

        Thread competitorThread = new Thread(iws);
        competitorsVector.add(competitorThread);
        if (winterCompetition.getActiveCompetitors().size() < winterCompetition.getMaxCompetitors())
            winterCompetition.addCompetitor(competitor);
        String competitor_icon_path;
        competitor_icon_path = winterCompetition.getGender() == Gender.MALE ? "/icons/" + competition_type_str + "Male.png" :
                "/icons/" + competition_type_str + "Female.png";
        setCompetitorIcon(competitor_icon_path, color);
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

        JPanel addCompetitor_panel = new JPanel(new FlowLayout());
        this.addCompetitor_btn = new JButton("Add Competitor");
        this.cloneCompetitor_btn = new JButton("Clone Competitor");
        addCompetitor_panel.add(cloneCompetitor_btn);
        addCompetitor_panel.add(addCompetitor_btn);
        mainPanel.add(addCompetitor_panel, BorderLayout.SOUTH);
        //Action Listener - Add Competitor
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
                            ctor = cls.getConstructor(String.class, double.class, Gender.class, double.class, double.class, Discipline.class, int.class);
                            competitor = (WinterSportsman) ctor.newInstance(name_str, age, winterCompetition.getGender(),
                                    acceleration, maxSpeed, winterCompetition.getDiscipline(), IWSid);
                            CompetitionGUI gui = CompetitionGUI.this;
                            if (gui.winterCompetition.getActiveCompetitors().size() == gui.winterCompetition.getMaxCompetitors()) {
                                gui.winterCompetition.setMaxCompetitors(gui.winterCompetition.getMaxCompetitors() + 1);
                            }
                            gui.addCompetitorIWS(competitor, IWSid, null);
                            IWSid++;
                            JOptionPane.showMessageDialog(null, "Competitor added Successfully.",
                                    "Build Competitor", JOptionPane.INFORMATION_MESSAGE);
                            System.out.println("vector size" + competitorsVector.size());
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
        //Action Listener - Clone Competitor
        // create CloneFrame object for cloned competitor modifications
        cloneCompetitor_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (CompetitionGUI.this.competitor == null)
                    JOptionPane.showMessageDialog(null, "Please build competitor before Cloning.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                else {
                    JDialog activeCompetitorsDialog = new JDialog();
                    JButton cloneBtn = new JButton("Clone");
                    JButton cancelBtn = new JButton("Cancel");
                    cancelBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            activeCompetitorsDialog.dispose();      // Close the dialog
                        }
                    });
                    JComboBox<Competitor> comboBox = new JComboBox<>();
                    for (Competitor comp : CompetitionGUI.this.winterCompetition.getActiveCompetitors()) {
                        comboBox.addItem(comp);
                    }
                    cloneBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            WinterSportsman selectedCompetitor = (WinterSportsman) comboBox.getSelectedItem();
                            competitor = selectedCompetitor.clone();
                            System.out.println(competitor.toString());
                            // now open dialog for modifications
                            JDialog modificationsDialog = new JDialog();
                            modificationsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                            JPanel itemsPanel = new JPanel(new BorderLayout());
                            JPanel idPanel = new JPanel(new GridLayout(2, 1));
                            JLabel idLabel = new JLabel("Enter new ID :");
                            JTextField idTextField = new JTextField(5);
                            JPanel colorPanel = new JPanel();
//                          activeCompetitorsDialog.setModal(true);
                            JPanel buttonsPanel = new JPanel(new FlowLayout());
                            Color chosenColor = JColorChooser.showDialog(colorPanel, "Choose a Color", Color.WHITE);
                            JLabel colorLabel = new JLabel();
                            if (chosenColor != null)
                                colorLabel.setText(String.format("Color Chosen : %s", chosenColor));
                            JButton doneBtn = new JButton("Done");
                            JButton cancelBtn = new JButton("Cancel");
                            cancelBtn.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    modificationsDialog.dispose();
                                    activeCompetitorsDialog.dispose();
                                }
                            });
                            doneBtn.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        int id = Integer.parseInt(idTextField.getText());
                                        competitor.setID(id);
                                        (CompetitionGUI.this).addCompetitorIWS(competitor, IWSid++, chosenColor);
                                        System.out.println("vector size" + competitorsVector.size());
                                        modificationsDialog.dispose();
                                        activeCompetitorsDialog.dispose();
                                    } catch (NumberFormatException err) {
                                        JOptionPane.showMessageDialog(null, "ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                                        modificationsDialog.dispose();
                                        activeCompetitorsDialog.dispose();
                                    }
                                }
                            });
                            buttonsPanel.add(doneBtn);
                            buttonsPanel.add(cancelBtn);
                            idPanel.add(idLabel);
                            idPanel.add(idTextField);
                            itemsPanel.add(idPanel, BorderLayout.NORTH);
                            itemsPanel.add(colorLabel, BorderLayout.CENTER);
                            itemsPanel.add(buttonsPanel, BorderLayout.SOUTH);
                            modificationsDialog.add(itemsPanel);
                            modificationsDialog.pack();
                            modificationsDialog.setVisible(true);
                        }
                    });
                    cancelBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            activeCompetitorsDialog.dispose(); // Close the dialog
                        }
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
                try {
                    updateInfoTable();
                } catch (Exception ignored) {
                }
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

    private void updateMainFrameSize(int arenaWidth, double arenaLength) {
        this.setSize(arenaWidth, (int) arenaLength);
        resizeBackgroundImage();
        iconPanel.setSize(arenaWidth, (int) arenaLength);
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
        startCompetition_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //if (CompetitionGUI.this.winterCompetition == null || CompetitionGUI.this.competitor == null)
                if (CompetitionGUI.this.winterCompetition == null || CompetitionGUI.this.winterCompetition.getActiveCompetitors().isEmpty())
                    JOptionPane.showMessageDialog(null, "Please build Competition and add new Competitors before starting the Competition.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                else {
                    if (!CompetitionGUI.this.competitionStatus) {
                        CompetitionGUI.this.competitionStatus = true;
                        setEnabledButtons(CompetitionGUI.this.competitionStatus);
                        for (Thread thread : competitorsVector) {
                            thread.start();
                        }
                    } else
                        JOptionPane.showMessageDialog(null, "Please rebuild Competition and add new Competitors.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    @Override
    public synchronized void update(Observable competitor, Object arg) {
        Point location = (Point) arg;
        int id = ((IndependantWinterSportman) competitor).getID();
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
        synchronized (this) {
            if (iconCompetitors.size() == winterCompetition.getFinishedCompetitors().size()) {
                JOptionPane.showMessageDialog(null, "The Competition has finished.",
                        "Competition Finished", JOptionPane.INFORMATION_MESSAGE);
                setEnabledButtons(!CompetitionGUI.this.competitionStatus);
                //            clearCompetitorIcons();
            }
        }
    }

    private synchronized void updateInfoTable() {
        try {
            tableModel.setRowCount(0);
            int index = 1;
            for (Competitor cmp : winterCompetition.getActiveCompetitors()) {
                tableModel.addRow(new Object[]{
                        index++,
                        ((WinterSportsman) cmp).getName(),
                        ((WinterSportsman) cmp).getID(),
                        ((WinterSportsman) cmp).getSpeed(),
                        ((WinterSportsman) cmp).getLocation().getX(),
                        winterArena.isFinished(cmp)
                });
            }
            for (Competitor cmp : winterCompetition.getFinishedCompetitors()) {
                tableModel.addRow(new Object[]{
                        index++,
                        ((WinterSportsman) cmp).getName(),
                        ((WinterSportsman) cmp).getID(),
                        ((WinterSportsman) cmp).getSpeed(),
                        ((WinterSportsman) cmp).getLocation().getX(),
                        winterArena.isFinished(cmp)
                });
            }
        } catch (NullPointerException err) {
            JOptionPane.showMessageDialog(null, "Please build Arena, Competition and add Competitors" +
                    " to view info.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ignored) {}
    }

    private void setEnabledButtons(boolean status) {
        this.buildArena_btn.setEnabled(!status);
        this.createCompetition_btn.setEnabled(!status);
        this.addCompetitor_btn.setEnabled(!status);
        this.startCompetition_btn.setEnabled(!status);
        this.cloneCompetitor_btn.setEnabled(!status);
        this.defaultCompetition_btn.setEnabled(!status);
    }
}