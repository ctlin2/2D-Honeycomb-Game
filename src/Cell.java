
public class Cell extends FCCNode {
	byte text;
	byte goal;
	boolean editable;
	
	Cell(int i, int j, int k, int l){
		super(i, j, k, l);
	}
	
	void setState(byte text, byte goal, boolean flag){
		this.text = text;
		this.goal = goal;
		this.editable = flag;
	}
	
	public void setText(byte text){
		this.text = text;
	}
}
