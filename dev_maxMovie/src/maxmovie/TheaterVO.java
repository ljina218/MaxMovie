package maxmovie;

public class TheaterVO {
	  String COMMAND        = null;
	  int PROTOCOL       = 0;
	  int RESULT         = 0;
	  String TICKETING_CODE = null; 
	  String MEM_ID         = null; 
	  String SEAT_CODE      = null; 
	  int TICKET_PRICE  	 = 0 ; 
	  int MOVIE_CODE   		 = 0 ; 
	  String SCR_CODE       = null;
	  
	public String getCOMMAND() {
		return COMMAND;
	}
	public void setCOMMAND(String cOMMAND) {
		COMMAND = cOMMAND;
	}
	public int getPROTOCOL() {
		return PROTOCOL;
	}
	public void setPROTOCOL(int pROTOCOL) {
		PROTOCOL = pROTOCOL;
	}
	public int getRESULT() {
		return RESULT;
	}
	public void setRESULT(int rESULT) {
		RESULT = rESULT;
	}
	public String getTICKETING_CODE() {
		return TICKETING_CODE;
	}
	public void setTICKETING_CODE(String tICKETING_CODE) {
		TICKETING_CODE = tICKETING_CODE;
	}
	public String getMEM_ID() {
		return MEM_ID;
	}
	public void setMEM_ID(String mEM_ID) {
		MEM_ID = mEM_ID;
	}
	public String getSEAT_CODE() {
		return SEAT_CODE;
	}
	public void setSEAT_CODE(String sEAT_CODE) {
		SEAT_CODE = sEAT_CODE;
	}
	public int getTICKET_PRICE() {
		return TICKET_PRICE;
	}
	public void setTICKET_PRICE(int tICKET_PRICE) {
		TICKET_PRICE = tICKET_PRICE;
	}
	public int getMOVIE_CODE() {
		return MOVIE_CODE;
	}
	public void setMOVIE_CODE(int mOVIE_CODE) {
		MOVIE_CODE = mOVIE_CODE;
	}
	public String getSCR_CODE() {
		return SCR_CODE;
	}
	public void setSCR_CODE(String sCR_CODE) {
		SCR_CODE = sCR_CODE;
	} 
}
