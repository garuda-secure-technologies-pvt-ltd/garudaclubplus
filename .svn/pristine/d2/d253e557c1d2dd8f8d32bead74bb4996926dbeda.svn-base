package com.garuda;

import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;


public class CardReaderStartClass implements CardSwipeNotifier {

	private PropertyUtils properties;
    private CardReader cr;
    
    public CardReaderStartClass() {
    	
    	properties = new PropertyUtils();
    	String portNumber =properties.getProperty("card.portnumber");
    	cr = new CardReader(portNumber,true);
    	try {
			cr.ConfigurePort();
		} catch (UnsupportedCommOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SerialConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	cr.setCardSwipeNotifier(this);
    	
	}
	
	@Override
	public void cardswiped(String custCard) {
		
		System.out.println("Card Swiped = "+custCard );
		
	}
    
}
