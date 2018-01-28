package BlackJack;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;

//The frame
class GameWindow extends JFrame implements ActionListener, KeyListener {

	//Adding all of the components
	ImageIcon welcomeHeader = new ImageIcon("Dicaprio.gif");
	ImageIcon enterNameGif = new ImageIcon("enterName.png");
	ImageIcon enterDepositGif = new ImageIcon("enterDeposit.png");
	ImageIcon tableDicaprio = new ImageIcon("tableDicaprio.gif");
	ImageIcon fiveDollarChip = new ImageIcon("5dollar.png");
	ImageIcon twofiveDollarChip = new ImageIcon("25dollar.png");
	ImageIcon hundDollarChip = new ImageIcon("100dollar.png");
	ImageIcon fivehundDollarChip = new ImageIcon("500dollar.png");
	ImageIcon thouDollarChip = new ImageIcon("1000dollar.png");

	JLabel welcomeLabel = new JLabel(welcomeHeader);
	JLabel swishLabel = new JLabel("");
	JLabel enterNameLabel = new JLabel (enterNameGif);
	JLabel enterDepositLabel = new JLabel (enterDepositGif);
	JLabel currentBetLabel = new JLabel ("");
	JLabel backgroundLabel = new JLabel (new ImageIcon ("table.jpg"));
	JLabel userTotalValue = new JLabel ("Player total: ");
	JLabel computerTotalValue = new JLabel ("Computer total: ");
	
	Font buttonFont = new Font("Onyx", Font.ITALIC, 50);
	Font textFieldFont = new Font("SansSerif", Font.BOLD, 30);
	Font labelFont = new Font("SansSerif", Font.ITALIC, 20);
	Font defaultFont = new JLabel().getFont();
	
	JTextField enterNameField = new JTextField("");
	JTextField enterDepositField = new JTextField("");
	
	JButton continueButton = new JButton("Start game");
	JButton startButton = new JButton ("Start Game!");
	JButton hitButton = new JButton("Hit");
	JButton standButton = new JButton("Stand");
	JButton resetButton = new JButton ("Reset");
	JButton dealButton = new JButton ("Deal");
	JButton restartGameButton = new JButton("New game");
	JButton Chip5Button = new JButton(fiveDollarChip);
	JButton Chip25Button = new JButton (twofiveDollarChip);
	JButton Chip100Button = new JButton (hundDollarChip);
	JButton Chip500Button = new JButton (fivehundDollarChip);
	JButton Chip1000Button = new JButton (thouDollarChip);
	
	JPanel main = new JPanel (new GridLayout(0,1,0,0));
	JPanel header = new JPanel (new FlowLayout());
	JPanel welcomePage = new JPanel(new GridLayout(4,3,15,15));
	JPanel welcomeDown = new JPanel (new GridLayout (3,3,15,15));
	JPanel betPage = new JPanel (new GridLayout(2,5,15,15));
	JPanel user = new JPanel (new GridLayout (0,1,15,15));
	JPanel playTable = new JPanel();
	JPanel computer = new JPanel (new GridLayout (0,1,15,15));
	
	//Creating arrays for the important stuff
	int[] ChipsValues = {5,25,100,500,1000};
	int[] deckInt = new int[53];
	JPanel[] panels = {welcomePage, user, computer, header, welcomeDown, betPage, playTable, main};
	JButton[] Chips = {Chip5Button, Chip25Button, Chip100Button, Chip500Button, Chip1000Button};
	JButton[] Buttons = {continueButton, startButton, restartGameButton, hitButton, standButton, resetButton, dealButton, Chip5Button, Chip25Button, Chip100Button, Chip500Button, Chip1000Button};
	JLabel[] deckImgLabel = new JLabel[53];
	ImageIcon[] deckImg1 = new ImageIcon[53];
	JLabel[] emptyLabels = new JLabel[15];
	
	
	int n, currentBet=0, enteredDeposit=0, currentBalance, temp, dealerTotal=0, playerTotal=0, cardPosition=0, aceCounter=0;
	String enteredName="";	
	
	public GameWindow (){
		super("BlackJack");
		setSize(1100,650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		Container contentArea = getContentPane();
		contentArea.setBackground(Color.green);
		contentArea.add(main);

		arrayCreator ();
		
	}

	public void actionPerformed(ActionEvent event) {
		//Trying to keep the actionPerformed method clean by using methods to call instead of keeping
		//all of the lines of code in here
		if(event.getSource() == continueButton){
			continueToBet();
		}
		
		if(event.getSource() == resetButton){
			reset();
		}
		
		if(event.getSource() == startButton){
			continueToBlackJack();
		}
		
		if(event.getSource() == dealButton){
			cardPosition++;
			cardDeal();
		}
		//Woops
		if(event.getSource() == hitButton){
			cardPosition++;
			int temp2 = cardDraw(51);
			
			if(deckInt[temp2] == 11){
				aceCounter++;
			}
			
			playerTotal = playerTotal + deckInt[temp2];
			userTotalValue.setText("Player total: "+playerTotal);
			
			if(playerTotal>21){
				winOrLose();
			}
			if(6 > cardPosition){
			panels[3].add(deckImgLabel[temp2]);			
			}
		}

		if(event.getSource() == standButton){
			winOrLose();
		}
		
		if(event.getSource() == restartGameButton){
			restartGame();
			System.out.println("nej");
		}
		
		for (n=0;n<Chips.length;n++){
			if(event.getSource() == Chips[n]){
				currentBet = currentBet + ChipsValues[n];
				if(currentBet>currentBalance){
					currentBetLabel.setText("<html>You cannot bet more money</br> than you deposit!</html>");
					currentBet = currentBet - ChipsValues[n];
				}else
				currentBetLabel.setText("Current bet amount: "+currentBet);
				currentBalance = currentBalance - ChipsValues[n];
					emptyLabels[5].setText("Current balance: "+currentBalance);
			}
		}
	}
	
	public void keyReleased(KeyEvent kevent){
		//In the startPage, when enter is pressed the second textfield will grabFocus
		//Meaning that it will be easier to type without using the cursor
		int pressedID = kevent.getKeyCode();
		if(pressedID == KeyEvent.VK_ENTER){
			enterDepositField.grabFocus();
		}
	}
	
	public void arrayCreator() {
		//Creating all of the arrays
		
		//Adding an extra string array for the adding of pictures to labels
		String[] suits = {"hearts", "diamonds", "clubs", "spades"};
		
		//Adding actionlisteners to all of the buttons in the buttonarray
		for (n=0;n<Buttons.length;n++){
			Buttons[n].addActionListener(this);
		}
		
		//Creating alot of emptylabels for better looking panels
		for (n=0; n<emptyLabels.length;n++){
			emptyLabels[n] = new JLabel("");
		}
		
		//Assigning the picture files to ImageIcons by looping in two loops
		for (temp=0; temp<suits.length;temp++){  
	        for (n=0;n<13;n++){
	        	String temporary = n+"_of_"+suits[temp]+".png";
	        	deckImg1[13*temp + n] = new ImageIcon(temporary);        		
	        	}
	        }
		//The last index is for the cardback
		deckImg1[52] = new ImageIcon("Back.png");
		
		//Assigning the correct values for the correct cards
		//Such as 10 for J, Q and K, as well as 11 for ACE
		for(temp=0;temp<4;temp++){
			for(n=0;n<13;n++){
				deckInt[13*temp+n] = n+1;
				if(deckInt[13*temp+n] == 1 || deckInt[13*temp+n]>9){
					if(deckInt[13*temp+n]>9){
						deckInt[13*temp+n] = 10;
					}else{
					deckInt[13*temp+n] = 11;}
					
				}else{
					deckInt[13*temp+n] = n+1;
				}				
			}
		}
		
		//Resizing the pictures in the imageIcons in order to make them fit in to the panels
		for (n=0;n<deckImg1.length;n++){
			Image img = deckImg1[n].getImage();
			Image newimg = img.getScaledInstance(120,120,0);
			deckImg1[n] = new ImageIcon(newimg);
		}
		
		//Assigning the imageicons to labels respectively
		for (n=0;n<deckImgLabel.length;n++){
			deckImgLabel[n] = new JLabel(deckImg1[n]);
		}
		
		//Changing background for all of the panels
		for(n=0;n<panels.length;n++){
			panels[n].setBackground(new Color(23, 104, 32));
			panels[n].setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		}
		
		//Designing the betting chips
		for (n=0;n<Chips.length;n++){			
			Chips[n].setOpaque(false);
			Chips[n].setContentAreaFilled(false);
			Chips[n].setBorderPainted(false);
		}
		
		//Desingning Labels and Buttons and Field in order to make them look better
		swishLabel.setFont(labelFont);
		currentBetLabel.setFont(labelFont);
		enterNameField.setFont(textFieldFont);
		enterDepositField.setFont(textFieldFont);
		continueButton.setBackground(new Color(115, 183, 122));
		resetButton.setOpaque(false);
		standButton.setFont(buttonFont);
		standButton.setBorder(BorderFactory.createBevelBorder(1, Color.orange, Color.green, Color.blue, Color.red));
		standButton.setBackground(Color.DARK_GRAY);
		standButton.setForeground(Color.WHITE);
		hitButton.setFont(buttonFont);
		hitButton.setBorder(BorderFactory.createBevelBorder(1, Color.green, Color.orange, Color.red, Color.blue));
		hitButton.setBackground(Color.WHITE);
		hitButton.setForeground(Color.DARK_GRAY);
		currentBetLabel.setFont(defaultFont);
		enterNameField.addKeyListener(this);		
		
		//Calling the method where the game begins
		startPage();
	}
	
	public void startPage(){
		//Adding panels to the main "container panel"
		main.add(panels[3]);
		main.add(panels[0]);
		main.add(panels[4]);
		
		//Adding a label that contains a gif as a header
		panels[3].add(welcomeLabel);
		
		//Adding emptyLabels to make up for the lost space in the panel
		for (n=8;n<13;n++){
			if(n<=10){
				panels[0].add(emptyLabels[n]);
			}
			//Adding the rest of the components to the panels
			if(n==10){
				panels[0].add(currentBetLabel);
				panels[0].add(enterNameLabel);
				panels[0].add(enterNameField);
				panels[0].add(swishLabel);
				panels[0].add(enterDepositLabel);
				panels[0].add(enterDepositField);
			}
			//continuing with emptylabels 
			if(n>10){
				panels[0].add(emptyLabels[n]);
			}
		}
		//some banter
			currentBetLabel.setText("Do not forget to swish the creator:");	
			swishLabel.setText("0761927594 - Amin Alian");
		
		//Adding the components in the last panel
		for (n=0;n<8;n++){
			if (n<=3){
				panels[4].add(emptyLabels[n]);
			}
			if (n==3){
				panels[4].add(continueButton);
			}
			if (n>3){
				panels[4].add(emptyLabels[n]);
			}
		}
		
		//Revalidating/refreshing the panels, to update the changes I have made in the method
		for(n=0;n<panels.length;n++){
			panels[n].revalidate();
			panels[n].repaint();
		}
	}
	
	public void continueToBet(){
		//Method to go from the startPage to the betPage
		
		//Getting the entered name from the textField
		enteredName = enterNameField.getText();
		
		//Getting the entered integer from the textField
		String enteredDepositStr = enterDepositField.getText();
		enteredDepositStr.replaceAll("[*a-zA-Z]","");
		
		//Because the text you get from textfield is in string it has to be converted to integer
		enteredDeposit = Integer.parseInt(enteredDepositStr);
		//Adding that entered value to another integer
		currentBalance = enteredDeposit;
		
		//For loops found online in order to remove the components from the panels as the panels
		//are going to be used later, thus me wanting them to be clear from any components
		Component[] panels0 = panels[0].getComponents();
		for (Component c : panels0){
			panels[0].remove(c);
		}
		Component[] panels3 = panels[3].getComponents();
		for (Component c : panels3){
			panels[3].remove(c);
		}
		Component[] panels4 = panels[4].getComponents();
		for (Component c : panels4){
			panels[4].remove(c);
		}
		
		main.remove(panels[0]);
		main.remove(panels[3]);
		main.remove(panels[4]);
		
		//Moving on to the betting Page
		betPage();
	}
	
	public void betPage(){
		main.add(panels[5]);
		main.add(panels[4]);
		
		//Adding the betting chips at the top of the panel
		for (n=0;n<Chips.length;n++){			
			panels[5].add(Chips[n]);
		}
		
		//Changing the text of an emptyLabel to show the amount of money user inputted
		panels[5].add(emptyLabels[5]);
			emptyLabels[5].setText("Current balance: "+currentBalance);
			emptyLabels[5].setFont(defaultFont);
		
		//adding a resetbutton in case you pressed the wrong button
		panels[5].add(resetButton);
			resetButton.setText("Reset bet");
		//Showing how much user has entered as a bet
		panels[5].add(currentBetLabel);
			currentBetLabel.setFont(defaultFont);
			currentBetLabel.setText("Current bet amount: "+currentBet);
			currentBetLabel.setForeground(Color.white);
		panels[5].add(startButton);	
		
		panels[5].add(emptyLabels[6]);
		
	}
	
	public void continueToBlackJack(){
		//Continuing to the actual gameplay
		
		main.remove(panels[4]);
		main.remove(panels[5]);
		
		//Removing components for later use of panel
		Component[] panels4 = panels[4].getComponents();
		for (Component c : panels4){
			panels[4].remove(c);
		}
		
		//Revalidating/refreshing the panels, to update the changes I have made in the method
		for (n=0;n<panels.length;n++){
			panels[n].revalidate();
			panels[n].repaint();
		}
		
		blackJack();
	}

	public void blackJack(){
		//The method for the gamepage
		
		//Changing the gridLayout of the main container to make room for three different panels
		main.setLayout(new GridLayout (0,3,5,5));
		//Changing the size in order to make room for stuff
		setSize(1366,720);
		
		//Adding three different panels, one for the player, one for a betting table, one for a dealer
		main.add(panels[1]);
		main.add(panels[6]);
		main.add(panels[2]);
		
		//Adding two panels in the user panel to divide it up, because the layout of the cards is 
		//different from the layout of the buttons
		panels[1].add(panels[4]);
		panels[1].add(panels[3]);
		panels[4].setLayout(new GridLayout(3,2));
		panels[3].setLayout(new GridLayout(2,3));
		
		//Adding a table in between the player and the dealer
		panels[6].add(backgroundLabel);
		panels[6].add(emptyLabels[14]);
		
		//Adding labels showing the total score of the cards
		panels[4].add(userTotalValue);
			userTotalValue.setText("Player total: "+playerTotal);
		panels[2].add(computerTotalValue);
			computerTotalValue.setText("Computer total: "+dealerTotal);
		
		//Adding stuff to the player upper panel
		panels[4].add(emptyLabels[0]);
		panels[4].add(restartGameButton);
		panels[4].add(dealButton);
		panels[4].add(hitButton);
		panels[4].add(standButton);
		
		//Adding one more panel to the dealer panel preparing for the cards
		panels[2].add(panels[0]);
		panels[0].setLayout(new GridLayout(2,3));
		
		//Revalidating/refreshing the panels, to update the changes I have made in the method
		for (n=0;n<panels.length;n++){
			panels[n].revalidate();
			panels[n].repaint();
		}
	}

	public int cardDraw(int range){
		//Drawing a random number which is being used later
		
		int cardNumber;
		cardNumber = (int)Math.ceil(Math.random()*range);		
		return cardNumber;
	}
	
	public void cardDeal(){
		int cardDrawn = cardDraw(51);
		
		panels[3].add(deckImgLabel[cardDrawn]);
			dealButton.setVisible(false);
			hitButton.setVisible(true);
			standButton.setVisible(true);
			playerTotal = playerTotal + deckInt[cardDrawn];
			System.out.println(deckInt[cardDrawn]);
				userTotalValue.setText("Player total: "+playerTotal);
			
		cardDrawn = cardDraw(51);
		panels[0].add(deckImgLabel[deckInt[cardDrawn]]);
		panels[0].add(deckImgLabel[52]);
		
		dealerTotal = dealerTotal + deckInt[cardDrawn];
			computerTotalValue.setText("Computer total: "+dealerTotal);
		
		
		panels[3].revalidate();
		panels[3].repaint();
	}
	
	public void reset(){
		
		currentBet = 0;
		currentBetLabel.setFont(defaultFont);
		currentBetLabel.setText("Current bet amount: "+currentBet);
	}
	
	public void winOrLose(){
		hitButton.setVisible(false);
		standButton.setVisible(false);
		dealButton.setVisible(false);
		
		if(playerTotal>21){
			if(aceCounter == 0){
				playerBust();
			}
			if(aceCounter == 1){
				playerTotal = playerTotal - 10;
				aceCounter--;
			}
			if(aceCounter == 2){
				playerTotal = playerTotal - 20;
				aceCounter = aceCounter - 2;
			}
			
		}else{
			while(dealerTotal<16){
				temp = cardDraw(51);
				dealerTotal = dealerTotal + deckInt[temp];
				computerTotalValue.setText("Dealer total: "+dealerTotal);
				panels[0].add(deckImgLabel[temp]);	
				if(dealerTotal>21){
					dealerBust();
				}else{
					if(playerTotal>dealerTotal){
						playerWin();
					}
					if(dealerTotal>playerTotal){
						dealerWin();
					}
				}
			}
		}
	}
	
	public void playerBust(){
		if(playerTotal>21){
			
		}
		currentBalance = currentBalance - currentBet;
		currentBet = 0;
		userTotalValue.setText("<html>You've busted! Your score: "+playerTotal+". </br>You lost your bet and now </br>your total bet is: </br></html>"+(currentBalance));
		
	}
	public void dealerBust(){
		currentBalance = currentBalance + currentBet*2;
		currentBet = 0;
		computerTotalValue.setText("<html>DEALER BUSTED!!1! HELL YEAAAHS YOU WON.</br>You won your bet and now your total bet is: "+(currentBalance+currentBet));
		
	}
	public void playerWin(){
		currentBalance = currentBalance + currentBet*2;
		currentBet = 0;
		userTotalValue.setText("<html>You won! You won your bet and</br> now your total bet is: </html>"+(currentBalance+currentBet));
		
	}
	public void dealerWin(){
		currentBalance = currentBalance - currentBet;
		currentBet = 0;
		userTotalValue.setText("<html>Dealer won! Your score: "+playerTotal+". </br>You lost your bet and now </br>your total bet is: </br></html>"+(currentBalance));
		
	}
	public void restartGame(){
		
		playerTotal = 0;
		dealerTotal = 0;
		
		for(n=0;n<panels.length;n++){
			panels[n].removeAll();
			main.remove(panels[n]);
		}
		for(n=0;n<panels.length;n++){
			panels[n].revalidate();
			panels[n].repaint();
		}
		
		main.setLayout(new GridLayout (0,1,0,0));
		
		hitButton.setVisible(true);
		standButton.setVisible(true);
		dealButton.setVisible(true);
		
		betPage();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0){}
}


public class GameOfBlackJack {

	
	public static void main(String[] args) {
		GameWindow Win = new GameWindow();

	}

}
