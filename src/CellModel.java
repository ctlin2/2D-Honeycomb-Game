import java.util.ArrayList;

class CellModel {

	private ArrayList<Cell> m_Cells;
	
	//«Øºc¤l
	CellModel(){
		m_Cells = new ArrayList<Cell>();
	}
	
	public void add(Cell node){
		m_Cells.add(node);
	}
	
	public ArrayList<Cell> getCells(){
		return m_Cells;
	}
	
	public boolean isCompleted(){
		
		Cell[] index = new Cell[m_Cells.size() + 1];
		for (Cell node : m_Cells){
			index[node.text] = node;
		}
		
		if (index[1] == null){ 
			//System.out.println("index1 null");
			return false;
		}
		
		for (int i = 1; i < m_Cells.size(); i++){
			if (index[i+1] == null || !index[i].isNeighbor(index[i+1])){
				// System.out.println("index["+(i+1)+"]=" + (index[i+1] == null ? null: index[i+1]));
				return false;
			}
		}
		
		return true;
	}
}