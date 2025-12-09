import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// display changes are a different button and don't have an action listener attached to them
// make it so that they're the same button so that on click it will change color but not the button itself
// OR attach action listener to the other button that is created as well

// other buttons work if you click them before attempting to make a new reservation
// but if you try to make a new reservation and then click the other stuff it DOES NOT WORKKKKK

/**
 * ReservationClient
 *
 * This program allows a client to connect to the reservationServer
 * to log in, sign up, create new reservations, and view existing
 * reservations.
 *
 * @author Meraj Syeda Lab Sec L12
 * @version 11/24/25
 */

public class ReservationClient extends JFrame {

    private static final String HOST = "localhost";   // host address for server
    private static final int PORT = 4242;             // port number for server
    private static final DateTimeFormatter EVENT_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    private Socket socket;
    private BufferedReader br;
    private PrintWriter pr;

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField loginUserField;
    private JPasswordField loginPassField;
    private JTextArea loginOutputArea;

    public ReservationClient() {
        setTitle("Reservation Client");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        setupConnection();

        buildLoginPanel();
        buildMenuPanel();

        add(mainPanel);
        setVisible(true);
    }

    private void setupConnection() {
        try {
            socket = new Socket(HOST, PORT);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pr = new PrintWriter(socket.getOutputStream(), true);

            String venueLine = br.readLine();
            String eventsLine = br.readLine();

            if (venueLine != null && venueLine.startsWith("VENUENAME:")) {
                setTitle("Reservation Client - " + venueLine.substring("VENUENAME:".length()));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to server: " + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void buildLoginPanel() {
        JPanel loginPanel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(5, 1, 6, 6));

        form.add(new JLabel("Username:"));
        loginUserField = new JTextField();
        form.add(loginUserField);

        form.add(new JLabel("Password:"));
        loginPassField = new JPasswordField();
        form.add(loginPassField);

        loginUserField.addActionListener(e -> loginPassField.requestFocusInWindow());
        
        JPanel btnRow = new JPanel();
        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Sign Up");
        btnRow.add(loginBtn);
        btnRow.add(signupBtn);
        form.add(btnRow);

        loginOutputArea = new JTextArea(6, 40);
        loginOutputArea.setEditable(false);
        JScrollPane sp = new JScrollPane(loginOutputArea);

        loginPanel.add(form, BorderLayout.NORTH);
        loginPanel.add(sp, BorderLayout.CENTER);

        mainPanel.add(loginPanel, "Login");
        cardLayout.show(mainPanel, "Login");



        // login button listener
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginBtn.setEnabled(false);
                loginOutputArea.setText("");
                final String user = loginUserField.getText().trim();
                final String pass = new String(loginPassField.getPassword());

                new Thread(new Runnable() {
                    public void run() {
                        try {

                                pr.println("LOGIN");
                                pr.println(user);
                                pr.println(pass);
                                final String response = br.readLine();

                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    loginOutputArea.append(response + "\n");
                                    loginBtn.setEnabled(true);

                                    if (response != null && response.startsWith("Success")) {
                                        showMenu();

                                    }

                                }
                            });
                        } catch (IOException e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    JOptionPane.showMessageDialog(ReservationClient.this, "Network error during login: " + e.getMessage());
                                    loginBtn.setEnabled(true);
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        signupBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signupBtn.setEnabled(false);
                final String user = loginUserField.getText().trim();
                final String pass = new String(loginPassField.getPassword());
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            pr.println("SIGNUP");
                            pr.println(user);
                            pr.println(pass);
                            final String response = br.readLine();
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    JOptionPane.showMessageDialog(ReservationClient.this, response);
                                    signupBtn.setEnabled(true);
                                    if (response != null && response.startsWith("Success")) {
                                        showMenu();
                                    }
                                }
                            });
                        } catch (IOException e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    JOptionPane.showMessageDialog(ReservationClient.this, "Network error: " + e.getMessage());
                                    signupBtn.setEnabled(true);
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    private void buildMenuPanel() {
        JPanel menuPanel = new JPanel(new GridLayout(5, 1, 8 ,8));
        JButton newBtn = new JButton("New Reservation");
        JButton viewBtn = new JButton("View Reservations");
        JButton eventsBtn = new JButton("View Events");
        JButton logoutBtn = new JButton("Logout");
        JButton exitBtn = new JButton("Exit");

        menuPanel.add(newBtn);
        menuPanel.add(viewBtn);
        menuPanel.add(eventsBtn);
        menuPanel.add(logoutBtn);
        menuPanel.add(exitBtn);

        mainPanel.add(menuPanel, "Menu");

        newBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newBtn.setEnabled(false);
                System.out.println("Button pressed");
                startNewReservationFlow(new Runnable() {

                    public void run() {}
                });
                newBtn.setEnabled(true);
            }
        });

        viewBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewBtn.setEnabled(false);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            pr.println("VIEW");
                            final String raw = readReservationPayload();
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    if (raw == null || raw.trim().isEmpty()) {
                                        JOptionPane.showMessageDialog(ReservationClient.this, "No reservations found.");
                                    } else {
                                        String[] arr = raw.split("\\$\\$");
                                        StringBuilder sb = new StringBuilder();
                                        for (String r : arr) {
                                            String trimmed = r.trim();
                                            if (trimmed.isEmpty()) continue;
                                            sb.append("- ").append(trimmed).append("\n\n");
                                        }
                                        JTextArea ta = new JTextArea(sb.toString());
                                        ta.setEditable(false);
                                        JOptionPane.showMessageDialog(ReservationClient.this, new JScrollPane(ta), "Reservations", JOptionPane.PLAIN_MESSAGE);
                                    }
                                    viewBtn.setEnabled(true);
                                }
                            });
                        } catch (IOException ex) {
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    JOptionPane.showMessageDialog(ReservationClient.this, "Network error: " + ex.getMessage());
                                    viewBtn.setEnabled(true);
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        eventsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eventsBtn.setEnabled(false);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            pr.println("EVENTS");
                            final String raw = br.readLine();
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    List<String> events = parseEvents(raw);
                                    StringBuilder sb = new StringBuilder();
                                    for (int i = 0; i < events.size(); i++) {
                                        sb.append((i + 1)).append(") ").append(events.get(i)).append("\n");
                                    }
                                    JTextArea ta = new JTextArea(sb.toString());
                                    ta.setEditable(false);
                                    JOptionPane.showMessageDialog(ReservationClient.this, new JScrollPane(ta), "Events", JOptionPane.PLAIN_MESSAGE);
                                    eventsBtn.setEnabled(true);
                                }
                            });
                        } catch (IOException ex) {
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    JOptionPane.showMessageDialog(ReservationClient.this, "Network error: " + ex.getMessage());
                                    eventsBtn.setEnabled(true);
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logoutBtn.setEnabled(false);
                new Thread(new Runnable() {
                    public void run() {
                        pr.println("LOGOUT");
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {

                                JOptionPane.showMessageDialog(ReservationClient.this, "Logged out.");

                                try {
                                    br.readLine();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                cardLayout.show(mainPanel, "Login");
                                logoutBtn.setEnabled(true);
                            }
                        });
                    }
                }).start();
            }
        });

        exitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            pr.println("EXIT");
                            socket.close();
                        } catch (IOException ignored) {}
                        System.exit(0);
                    }
                }).start();
            }
        });

    }

    private void showMenu() {
        cardLayout.show(mainPanel, "Menu");
    }

    private void startNewReservationFlow(final Runnable finishedCallback) {

        new Thread(new Runnable() {
            public void run() {

                try {
                    pr.println("NEW");
                    final String eventsRaw = br.readLine(); // server sends events string
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            int chosenIndex = showEventSelectionDialog(eventsRaw);
                            if (chosenIndex == -1) {
                                System.out.println("USER PRESSED CANCEL BUTTON");
                                pr.println("********CANCEL********");
                                // user canceled
                                if (finishedCallback != null) finishedCallback.run();
                                return;
                            }
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        pr.println(String.valueOf(chosenIndex + 1)); // send 1-based index
                                        // read seating chart lines until ENDCHART
                                        List<String> chartLines = new ArrayList<String>();
                                        String line = br.readLine();
                                        while (line != null && !line.equals("ENDCHART")) {
                                            chartLines.add(line);
                                            line = br.readLine();
                                        }
                                        final String chartText = joinLines(chartLines);
                                        // Show seating chart dialog on EDT and get selected seats
                                        SwingUtilities.invokeLater(new Runnable() {
                                            public void run() {
                                                List<Point> selected = showSeatingChartDialog(chartText);
                                                if (selected == null || selected.isEmpty()) {
                                                    // user cancelled or didn't pick seats -> finish
                                                    System.out.println("USER CANCELED AT SEAT SELECTION PART");
                                                    pr.println("********CANCEL********");
                                                    if (finishedCallback != null) finishedCallback.run();
                                                    return;
                                                }
                                                // Show details dialog to get numPeople, time, date
                                                // ReservationDetails details = showDetailsDialog();
                                                /*
                                                if (details == null) {
                                                    System.out.println("USER CANCELED AT INFO INPUT PAGE");
                                                    pr.println("********CANCEL********");
                                                    // canceled
                                                    if (finishedCallback != null) finishedCallback.run();
                                                    return;
                                                }
                                                */
                                                // After details entered, send details and seats in background thread
                                                new Thread(new Runnable() {
                                                    public void run() {
                                                        try {
                                                            pr.println(selected.size());
                                                            pr.println(chosenIndex);
                                                            String seatStr = buildSeatString(selected); // "x1,y1,x2,y2"
                                                            pr.println(seatStr);
                                                            final String finalResp = br.readLine();
                                                            SwingUtilities.invokeLater(new Runnable() {
                                                                public void run() {
                                                                    JOptionPane.showMessageDialog(ReservationClient.this,
                                                                             finalResp);
                                                                    if (finishedCallback != null) finishedCallback.run();
                                                                }
                                                            });
                                                        } catch (IOException e) {
                                                            SwingUtilities.invokeLater(new Runnable() {
                                                                public void run() {
                                                                    JOptionPane.showMessageDialog(ReservationClient.this,
                                                                            "Network error sending reservation: " + e.getMessage());
                                                                    if (finishedCallback != null) finishedCallback.run();
                                                                }
                                                            });
                                                        }
                                                    }
                                                }).start();
                                            }
                                        });
                                    } catch (IOException e) {
                                        SwingUtilities.invokeLater(new Runnable() {
                                            public void run() {
                                                JOptionPane.showMessageDialog(ReservationClient.this, "Network error reading chart: " + e.getMessage());
                                                if (finishedCallback != null) finishedCallback.run();
                                            }
                                        });
                                    }
                                }
                            }).start();
                        }
                    });
                } catch (IOException e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            JOptionPane.showMessageDialog(ReservationClient.this, "Network error: " + e.getMessage());
                            if (finishedCallback != null) finishedCallback.run();
                        }
                    });
                }
            }
        }).start();
    }

    private int showEventSelectionDialog(String eventsRaw) {
        List<String> events = parseEvents(eventsRaw);
        if (events.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No events available.");
            return -1;
        }
        JList<String> list = new JList<String>(events.toArray(new String[0]));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sp = new JScrollPane(list);
        sp.setPreferredSize(new Dimension(400, 200));
        int option = JOptionPane.showConfirmDialog(this, sp, "Select Event", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option != JOptionPane.OK_OPTION) return -1;
        int sel = list.getSelectedIndex();
        if (sel < 0) {
            JOptionPane.showMessageDialog(this, "Please select an event.");
            return showEventSelectionDialog(eventsRaw); // ask again
        }
        return sel;
    }

    // show seating chart dialog and return list of selected Points (x=col,y=row), or null if canceled
    private List<Point> showSeatingChartDialog(String chartText) {
        String[] lines = chartText.split("\n");
        List<char[]> rows = new ArrayList<char[]>();
        for (String ln : lines) {
            if (ln.trim().isEmpty()) continue;
            String s = ln.trim();
            if (s.startsWith("[")) s = s.substring(1);
            String[] cells = s.split(",");
            char[] row = new char[cells.length];
            for (int i = 0; i < cells.length; i++) {
                String tok = cells[i].trim();
                row[i] = tok.length() > 0 ? tok.charAt(0) : 'o';
            }
            rows.add(row);
        }
        if (rows.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seating chart empty.");
            return null;
        }
        int R = rows.size();
        int C = rows.get(0).length;
        JPanel grid = new JPanel(new GridLayout(R, C, 4, 4));
        final List<JToggleButton> toggleList = new ArrayList<JToggleButton>();
        for (int r = 0; r < R; r++) {
            char[] row = rows.get(r);
            for (int c = 0; c < C; c++) {
                char ch = row[c];
                final JToggleButton btn = new JToggleButton();
                btn.setPreferredSize(new Dimension(40, 30));
                if (ch == 'x' || ch == 'X') {
                    btn.setText("X");
                    btn.setEnabled(false);
                    btn.setBackground(Color.LIGHT_GRAY);
                } else {
                    btn.setText("o");
                }
                // store coords in action command "c,r"
                btn.setActionCommand(c + "," + r);
                toggleList.add(btn);
                grid.add(btn);
            }
        }

        JScrollPane sp = new JScrollPane(grid);
        sp.setPreferredSize(new Dimension(400, 300));
        int option = JOptionPane.showConfirmDialog(this, sp, "Select Seats (click to toggle)", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option != JOptionPane.OK_OPTION) return null;
        List<Point> selected = new ArrayList<Point>();
        for (JToggleButton tb : toggleList) {
            if (tb.isSelected()) {
                String[] parts = tb.getActionCommand().split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                selected.add(new Point(x, y));
            }
        }
        if (selected.isEmpty()) {
            int retry = JOptionPane.showConfirmDialog(this, "No seats selected. Cancel or try again?", "No seats", JOptionPane.YES_NO_OPTION);
            if (retry == JOptionPane.NO_OPTION) {
                return showSeatingChartDialog(chartText);
            } else {
                return null;
            }
        }
        return selected;
    }

    private ReservationDetails showDetailsDialog() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 4, 4));
        panel.add(new JLabel("Number of people (integer):"));
        final JTextField numPeopleField = new JTextField();
        panel.add(numPeopleField);

        panel.add(new JLabel("Time (human readable, e.g. 6:00 PM):"));
        final JTextField timeField = new JTextField();
        panel.add(timeField);

        panel.add(new JLabel("Date (human readable, e.g. 2025-12-20):"));
        final JTextField dateField = new JTextField();
        panel.add(dateField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Reservation Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option != JOptionPane.OK_OPTION) return null;
        try {
            int num = Integer.parseInt(numPeopleField.getText().trim());
            String t = timeField.getText().trim();
            String d = dateField.getText().trim();
            if (t.isEmpty() || d.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Time and date cannot be empty.");
                return showDetailsDialog();
            }
            return new ReservationDetails(num, t, d);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Number of people must be an integer.");
            return showDetailsDialog();
        }
    }

    private String buildSeatString(List<Point> seats) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < seats.size(); i++) {
            Point p = seats.get(i);
            sb.append(p.x).append(",").append(p.y);
            if (i < seats.size() - 1) sb.append(",");
        }
        return sb.toString();
    }

    private static String joinLines(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        for (String s : lines) sb.append(s).append("\n");
        return sb.toString();
    }

    /**
     * Function that takes the event data string from the server and parses it to be displayed
     * @return String to be displayed
     * @throws IOException
     */
    private String readReservationPayload() throws IOException {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        boolean firstLine = true;
        if (line.isEmpty() && firstLine) {
            //error nothing returned
            br.readLine();
            return sb.toString();

        }
        sb.append("Reservation: \n");

        //Read infinate lines
        while (line != null) {
            if (line.isEmpty() && firstLine) {
                //error nothing returned
                break;
            }
            if (line.equals("END_OF_USER_EVENT_DETAILS**")) {
                break;
            }

            if (!firstLine) {
                sb.append("\n");
            }
            firstLine = false;
            sb.append(line);


            line = br.readLine();
        }
        return sb.toString();
    }

    // parse server event string into list of "Name on Day at Time"
    private List<String> parseEvents(String raw) {
        List<String> out = new ArrayList<String>();
        if (raw == null) return out;
        String s = raw.trim();
        if (s.startsWith("#") && s.endsWith("#")) s = s.substring(1, s.length() - 1);
        String[] eventEntries = s.split("\\$\\$");
        for (String e : eventEntries) {
            if (e.trim().isEmpty()) continue;
            String name = e;
            String day = "";
            String time = "";
            if (e.contains("%%")) {
                String[] parts = e.split("%%", 2);
                name = parts[0];
                String rest = parts.length > 1 ? parts[1] : "";
                if (rest.contains("@@")) {
                    String[] dt = rest.split("@@", 2);
                    day = dt[0];
                    time = dt.length > 1 ? dt[1] : "";
                } else day = rest;
            }
            String display = name;
            if (!day.isEmpty()) {
                try {
                    long epochDay = Long.parseLong(day);
                    LocalDate localDate = LocalDate.ofEpochDay(epochDay);
                    display += " on " + localDate.format(EVENT_DATE_FORMATTER);
                } catch (NumberFormatException ex) {
                   // if there is an error with epoch show just the # idk
                    display += " on " + day;
                }
            }

            if (!time.isEmpty()) {

                //TODO: format times

                int timeHour = (Integer.parseInt(time) / 100);

                String typeOfTime = "am";

                if (timeHour > 12)
                    typeOfTime = "pm";

                timeHour = timeHour % 12;

                display += " at " + timeHour + ":";

                int timeInMinutes = 00;

                if (typeOfTime.equals("pm")) {

                    timeInMinutes = Integer.parseInt(time) - (Integer.parseInt(time) / 100 * 100);

                }
                else if (typeOfTime.equals("am")) {

                    timeInMinutes =  Integer.parseInt(time) - (timeHour * 100);
                }

                display += String.format("%02d", timeInMinutes) + typeOfTime;



            }
            out.add(display);
        }
        System.out.println(out.getFirst());
        return out;
    }

    private static class ReservationDetails {
        int numPeople;
        String timeStr;
        String dateStr;
        ReservationDetails(int n, String t, String d) { numPeople = n; timeStr = t; dateStr = d; }
    }

    public static void main(String[] args) {

        // calls the main method of reservationServer so that you don't have to individually run server and client each time
        String[] arr = new String[0];
        reservationServer.main(arr);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ReservationClient();
            }
        });

    }



}
