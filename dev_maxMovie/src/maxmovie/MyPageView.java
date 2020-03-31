package maxmovie;

import javax.swing.JPanel;

public class MyPageView extends JPanel{

	EventMapping 				em 				= null;
	MemInfoView 				miv 			= new MemInfoView();
	MemUpdateView 				muv 			= new MemUpdateView();
	TicketHistoryView 			thv 			= new TicketHistoryView();
	
	public MyPageView(EventMapping em) {
		this.em = em;
	}
}
