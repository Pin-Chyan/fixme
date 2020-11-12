package com.endlesshorizon.broker.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.server.UID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyPanel extends JPanel {
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 5000;
	public static JTextField guiTextArea;
	public static JTextField guiTextField;
	public static String AllText;
	private static JLabel Mlabel;
	private static ArrayList<String> marketList = new ArrayList<String>();
	private static ArrayList<String> insList = new ArrayList<String>();
	
    private static JButton jcomp1;
    private static JButton jcomp2;
    private static JList jcomp3;
    private static JLabel jcomp4;
    private static JLabel jcomp5;
    private static JMenuBar jcomp6;
    private static JLabel jcomp7;
    private static JLabel jcomp8;
    private static JTextField jcomp9;
    private static JLabel jcomp10;
    private static JTextField jcomp11;
    private static JLabel jcomp12;
    private static JLabel jcomp13;
	private static JLabel jcomp14;
	private static JLabel jcomp15;
    private static JMenuItem jcomp16;
	private static JMenu submenu;
	private static JMenu toolMenu;

	private static int Insamount;
	private static int Insprice;
	private static String Insname;
	private static String Insmarket;

	
	static String UID = "";
	static String[] Markets = null;
	static BufferedReader input = null;
	static PrintWriter out = null;


	@SuppressWarnings("unchecked")
	public MyPanel() throws UnknownHostException, IOException, InterruptedException {


		//construct preComponents
        String[] jcomp3Items = {};
        toolMenu = new JMenu ("Tools");
		submenu = new JMenu("Market List");

		toolMenu.add(submenu);
		for (int jj = 0; jj < Markets.length; jj++) {
			marketList.add(Markets[jj]);
		}
		createMarkets(marketList);

		jcomp16 = new JMenuItem();
		jcomp16.setText("Refresh");
		jcomp16.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
		});
		toolMenu.add(jcomp16);

        //construct components
		jcomp1 = new JButton ("Sell");
		jcomp1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sellInstrument();
            }
        });
		jcomp2 = new JButton ("Buy");
		jcomp2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buyInstrument();
            }
        });
		jcomp3 = new JList (jcomp3Items);
		jcomp3.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
					setIns();
                }
            }
        });
        jcomp4 = new JLabel ("Selected Instrument:");
        jcomp5 = new JLabel ("Instruments");
        jcomp6 = new JMenuBar();
        jcomp6.add (toolMenu);
        jcomp7 = new JLabel ("Selected Market:");
        jcomp8 = new JLabel ("Click on tools to select a market");
        jcomp9 = new JTextField (5);
        jcomp10 = new JLabel ("Price:");
        jcomp11 = new JTextField (5);
        jcomp12 = new JLabel ("Amount:");
        jcomp13 = new JLabel ("Value: N/A");
        jcomp14 = new JLabel ("Name: None");
        jcomp15 = new JLabel ("Status: N/A");

        //adjust size and set layout
        setPreferredSize (new Dimension (944, 601));
        setLayout (null);

        //add components
        add (jcomp1);
        add (jcomp2);
        add (jcomp3);
        add (jcomp4);
        add (jcomp5);
        add (jcomp6);
        add (jcomp7);
        add (jcomp8);
        add (jcomp9);
        add (jcomp10);
        add (jcomp11);
        add (jcomp12);
        add (jcomp13);
        add (jcomp14);
        add (jcomp15);

        //set component bounds (only needed by Absolute Positioning)
        jcomp1.setBounds (730, 505, 75, 25);
        jcomp2.setBounds (625, 505, 75, 25);
        jcomp3.setBounds (95, 80, 250, 500);
        jcomp4.setBounds (625, 240, 175, 30);
        jcomp5.setBounds (185, 55, 100, 25);
        jcomp6.setBounds (0, 0, 945, 25);
        jcomp7.setBounds (10, 30, 100, 25);
        jcomp8.setBounds (125, 30, 200, 25);
        jcomp9.setBounds (665, 465, 100, 25);
        jcomp10.setBounds (665, 440, 100, 25);
        jcomp11.setBounds (665, 395, 100, 25);
        jcomp12.setBounds (665, 365, 100, 25);
        jcomp13.setBounds (665, 325, 100, 25);
		jcomp14.setBounds (665, 285, 120, 25);
		jcomp15.setBounds (500, 545, 400, 25);
	}

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException{
		Socket client = new Socket(SERVER_IP, SERVER_PORT);
		input = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(client.getOutputStream(), true);
		String serverResponse = input.readLine();
		System.out.println(serverResponse);
		if (serverResponse.contains("UID:")) {
			final Pattern p = Pattern.compile( "(\\d{6})" );
			final Matcher m = p.matcher(serverResponse);
			if ( m.find() ) {
			  UID = m.group( 0 );
			}
		}

		int checkSum = genCheckSum(UID + " markets");
		out.println(UID + " markets" + " " + checkSum);
		String serverResponse2 = input.readLine();
		String serverResponse3 = input.readLine();
		System.out.println(serverResponse3);

		Markets = null;
		Markets = serverResponse3.split("=");


		JFrame frame = new JFrame("EndrisonZA Broker "+UID);
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add (new MyPanel());
        frame.pack();
		frame.setVisible (true);
		if (false){
			client.close();
		}
	}
	private static void refreshData(){
		updateMList();
		Insmarket = null;
		jcomp8.setText("Click on tools to select a market");
		jcomp14.setText("Name: N/A");
		jcomp13.setText("Value: N/A");
		Insname = null;
		Object[] allins = {};
		loadInstruments(allins);
	}
	private static void setIns(){
		if (jcomp3.getSelectedValue() != null){
			String[] values = jcomp3.getSelectedValue().toString().split("@",0);
			String[] v2 = values[0].split("\\s+",0);
			jcomp14.setText("Name: "+v2[1]);
			jcomp13.setText("Value: "+values[1]);
			String nem = values[0];
			String[] nem2 = nem.split("\\s",0);
			Insname = v2[1];
			// Insprice = Integer.parseInt(values[1].replaceAll("[^\\d.]", ""));
		}
	}
	private static void updateMList(){
		int checkSum = genCheckSum(UID + " markets");
		out.println(UID + " markets" + " " + checkSum);
		String serverResponse3 = "";
		try {
			String serverResponse2 = input.readLine();
			serverResponse3 = input.readLine();
		} catch (IOException exc435){

		}
		System.out.println(serverResponse3);

		Markets = null;
		Markets = serverResponse3.split("=");

		submenu.removeAll();
		submenu.repaint();
		marketList.clear();
		for (int jj = 0; jj < Markets.length; jj++) {
			System.out.println("mk-"+Markets[jj]);
			marketList.add(Markets[jj]);
		}
		createMarkets(marketList);
	}
	public static int genCheckSum(String message){
        int genCheckSum = 1;
        for (int i = 0; i < message.length(); i++){
            int temp = (int) (Math.floor(Math.log(message.charAt(i)) / Math.log(2))) + 1;
            genCheckSum += ((1 << temp) - 1) ^ message.charAt(i);
        }
        return genCheckSum;
    }
	private static void SetMarket(String Name) {
		updateMList();
		Insmarket = Name;
		jcomp8.setText(Name);
		jcomp14.setText("Name: N/A");
		jcomp13.setText("Value: N/A");
		Insname = null;
		int checkSum = genCheckSum(UID + " list "+Name);
		out.println(UID + " list " + Name + " " + checkSum);
		String serverResponse3 = "";
		try {
			String serverResponse2 = input.readLine();
			serverResponse3 = input.readLine();
		} catch (IOException eex){

		}
		System.out.println(serverResponse3);

		String[] inss = serverResponse3.substring(14).split("=");
		if (serverResponse3.contains("none")){
			System.out.println("none");
			Object[] allins = {};
			loadInstruments(allins);
		} else {
			ArrayList<String> jcomp3Items = new ArrayList<String>();
			for (int jj = 0; jj < inss.length; jj++) {
				System.out.println(inss[jj]);
				String[] inssV = inss[jj].split("-");
				jcomp3Items.add(inssV[2] +"x "+ inssV[0]+" @ R"+inssV[1]);
			}
			Object[] allins = jcomp3Items.toArray();
			loadInstruments(allins);
		}
	}
	@SuppressWarnings("unchecked")
	private static void loadInstruments(Object[] Instruments) {
		jcomp3.setListData(Instruments);
	}
	private static void createMarkets(ArrayList<String> markets) {
		for (String mk : markets){
			JMenuItem menuItem = new JMenuItem(mk);
			menuItem.addActionListener(new java.awt.event.ActionListener() {

				@Override
				public void actionPerformed(java.awt.event.ActionEvent arg0) {
					SetMarket(mk);
				}
			});
			submenu.add(menuItem);
		}
	}

	private static void buyInstrument() {
		Insprice = Integer.parseInt(jcomp9.getText());
		Insamount = Integer.parseInt(jcomp11.getText());
		if (Insname != null){
			if (Insmarket != null){
				if (Insamount > 0){
					if (Insprice > 0){
						System.out.printf("%nInstrument Purchase (%s|%s|%d|%d)%n",Insmarket, Insname,Insamount,Insprice);
						int checkSum = genCheckSum(UID + " buy "+Insname+" "+Insprice+" "+Insamount+" "+Insmarket);
						out.println(UID + " buy "+Insname+" "+Insprice+" "+Insamount+" "+Insmarket +" "+ checkSum);
						String serverResponse3 = "";
						try {
							String serverResponse2 = input.readLine();
							serverResponse3 = input.readLine();
						} catch (IOException exec23){

						}
						System.out.println(serverResponse3);
						jcomp15.setText("Status: "+serverResponse3.substring(14));
						SetMarket(Insmarket);
					} else {
						System.out.println("Price Can't Be Negative or 0.");
					}
				} else {
					System.out.println("Atleast 1 Instrument Quantity.");
				}
			} else {
				System.out.println("Please Select a Market.");
			}
		} else {
			System.out.println("Please Select a Instrument.");
		}
	}

	private static void sellInstrument() {
		Insprice = Integer.parseInt(jcomp9.getText());
		Insamount = Integer.parseInt(jcomp11.getText());
		if (Insname != null){
			if (Insmarket != null){
				if (Insamount > 0){
					if (Insprice > 0){
						System.out.printf("%nInstrument Sale (%s|%s|%d|%d)%n",Insmarket, Insname,Insamount,Insprice);
						int checkSum = genCheckSum(UID + " sell "+Insname+" "+Insprice+" "+Insamount+" "+Insmarket);
						out.println(UID + " sell "+Insname+" "+Insprice+" "+Insamount+" "+Insmarket +" "+ checkSum);
						String serverResponse3 = "";
						try {
							String serverResponse2 = input.readLine();
							serverResponse3 = input.readLine();
						} catch (IOException exec23){

						}
						System.out.println(serverResponse3);
						jcomp15.setText("Status: "+serverResponse3.substring(14));
						SetMarket(Insmarket);
					} else {
						System.out.println("Price Can't Be Negative or 0.");
					}
				} else {
					System.out.println("Atleast 1 Instrument Quantity.");
				}
			} else {
				System.out.println("Please Select a Market.");
			}
		} else {
			System.out.println("Please Select a Instrument.");
		}
	}
}



// private static void generateFrame(){
	// 	//Creating the Frame
	// 	JMenuBar menuBar;
	// 	JMenu menu, submenu;
	// 	JMenuItem menuItem;
    //     JFrame frame = new JFrame("EndrisonZA Broker");
    //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     frame.setSize(600, 600);

    //     //Creating the MenuBar and adding components
	// 	menuBar = new JMenuBar();
    //     menu = new JMenu("Tools");
	// 	menu.getAccessibleContext().setAccessibleDescription(
	// 	        "Helpfull Tools");
	// 	menuBar.add(menu);


    //     menuBar.add(menu);
	// 	menu.addSeparator();
	// 	submenu = new JMenu("Market List");

	// 	menuItem = new JMenuItem("NASDAQ");
	// 	menuItem.addActionListener(new java.awt.event.ActionListener() {

	// 		@Override
	// 		public void actionPerformed(java.awt.event.ActionEvent arg0) {
	// 			SetMarket("NASDAQ");
	// 		}
	// 	});
	// 	submenu.add(menuItem);
		

	// 	menuItem = new JMenuItem("SA Trades");
	// 	menuItem.addActionListener(new java.awt.event.ActionListener() {

	// 		@Override
	// 		public void actionPerformed(java.awt.event.ActionEvent arg0) {
		
	// 			SetMarket("SA Trades");
	// 		}
	// 	});
	// 	submenu.add(menuItem);
	// 	menu.add(submenu);

    //     //Creating the panel at bottom and adding components
    //     JPanel panel = new JPanel(); // the panel is not visible in output
    //     JLabel label = new JLabel("Request");
    //     guiTextField = new JTextField(30); // accepts upto 10 characters
    //     JButton submit = new JButton("Submit");
    //     panel.add(label); // Components Added using Flow Layout
    //     panel.add(guiTextField);
    //     panel.add(submit);


	// 	JPanel Wpanel = new JPanel();
	// 	JPanel Wpanel2 = new JPanel();
	// 	JPanel Wpanel3 = new JPanel();
	// 	Wpanel.setLayout(new FlowLayout(FlowLayout.));
	// 	Wpanel2.setPreferredSize( new Dimension(60, 20) );
	// 	Wpanel3.setPreferredSize( new Dimension(60, 20) );
	// 	Wpanel.add(Wpanel2);
	// 	Wpanel.add(Wpanel3);
    //     JLabel Malabel = new JLabel("Market: ");
	// 	Wpanel2.add(Malabel);
	// 	Mlabel = new JLabel();
	// 	Wpanel3.add(Mlabel);


	// 	// String text = String.valueOf(i);
    //     //     JButton button = new JButton( text );
    //     //     button.addActionListener( numberAction );
    //     //     button.setBorder( new LineBorder(Color.BLACK) );
    //     //     button.setPreferredSize( new Dimension(30, 30) );
    //     //     buttonPanel.add( button );

    //     // Text Area at the Center
    //     guiTextArea = new JTextField();
	// 	guiTextArea.setEditable(false);
	// 	submit.addActionListener(new java.awt.event.ActionListener() {

	// 		@Override
	// 		public void actionPerformed(java.awt.event.ActionEvent e) {
	// 			guiTextArea.setText(guiTextArea.getText()+guiTextField.getText()+"\n");
	// 		}
	// 	});

    //     //Adding Components to the frame.
    //     frame.getContentPane().add(BorderLayout.SOUTH, panel);
	// 	frame.getContentPane().add(BorderLayout.NORTH, menuBar);
	// 	frame.getContentPane().add(BorderLayout.WEST, Wpanel);
    //     frame.getContentPane().add(BorderLayout.CENTER, guiTextArea);
    //     frame.setVisible(true);
	// }