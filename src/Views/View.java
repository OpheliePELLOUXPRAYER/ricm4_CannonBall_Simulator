package Views;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import Kernel.Controleur;

public class View extends StateBasedGame {
	private final int iDsimulateur = 0;
		
	public View(String name, Controleur c) {
		super(name);
		addState(new Simulateur(c));
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		this.enterState(iDsimulateur);
		
	}
	
}
