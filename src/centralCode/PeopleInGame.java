package centralCode;

public class PeopleInGame {

	private String id_1;
	private String id_2;
	private boolean isDone_1;
	private boolean isDone_2;
	private String score1;
	private String score2;
	
	public PeopleInGame(String id_1, String id_2) {
		this.id_1 = id_1;
		this.id_2 = id_2;
		this.isDone_1 = false;
		this.isDone_2 = false;
		score1 = "";
		score2 = "";
	}
	public void setP1(String p1) {
		id_1 = p1;
	}
	
	public void setP2(String p2) {
		id_2 = p2;
	}
	
	public String getPlayer1() {
		return id_1;
	}
	
	public String getPlayer2() {
		return id_2;
	}
	
	public boolean done1() {
		return isDone_1;
	}
	
	public boolean done2() {
		return isDone_2;
	}
	
	public void oneIsDone() {
		isDone_1 = true;
	}
	
	public void twoIsDone() {
		isDone_2 = true;
	}
	
	public void setS1(String score) {
		score1 = score;
	}
	
	public void setS2(String score) {
		score2 = score;
	}
	
	public String getS1() {
		return score1;
	}
	
	public String getS2() {
		return score2;
	}
}
