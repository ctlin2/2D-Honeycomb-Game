import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JPanel;

class MyJPanel extends JPanel {

	private static final int radius = 50;
	public CellModel m_CellModel;
	
	private ArrayList<QA> m_QA;
	private int m_QA_number; // 目前選到的題目
	private Cell m_CurrentNode = null;

	private boolean m_Hint;
	
	// 建構子
	MyJPanel() {
		m_CellModel = new CellModel();
		//*
		m_CellModel.add(new Cell(0, 0, -3, 3));
		m_CellModel.add(new Cell(0, 1, -3, 2));
		m_CellModel.add(new Cell(0, 2, -3, 1));
		m_CellModel.add(new Cell(0, 3, -3, 0));
		m_CellModel.add(new Cell(0, -1, -2, 3));
		m_CellModel.add(new Cell(0, 0, -2, 2));
		m_CellModel.add(new Cell(0, 1, -2, 1));
		m_CellModel.add(new Cell(0, 2, -2, 0));
		m_CellModel.add(new Cell(0, 3, -2, -1));
		m_CellModel.add(new Cell(0, -2, -1, 3));
		m_CellModel.add(new Cell(0, -1, -1, 2));
		m_CellModel.add(new Cell(0, 0, -1, 1));
		m_CellModel.add(new Cell(0, 1, -1, 0));
		m_CellModel.add(new Cell(0, 2, -1, -1));
		m_CellModel.add(new Cell(0, 3, -1, -2));
		m_CellModel.add(new Cell(0, -3, 0, 3));
		m_CellModel.add(new Cell(0, -2, 0, 2));
		m_CellModel.add(new Cell(0, -1, 0, 1));
		m_CellModel.add(new Cell(0, 0, 0, 0));
		m_CellModel.add(new Cell(0, 1, 0, -1));
		m_CellModel.add(new Cell(0, 2, 0, -2));
		m_CellModel.add(new Cell(0, 3, 0, -3));
		m_CellModel.add(new Cell(0, -3, 1, 2));
		m_CellModel.add(new Cell(0, -2, 1, 1));
		m_CellModel.add(new Cell(0, -1, 1, 0));
		m_CellModel.add(new Cell(0, 0, 1, -1));
		m_CellModel.add(new Cell(0, 1, 1, -2));
		m_CellModel.add(new Cell(0, 2, 1, -3));
		m_CellModel.add(new Cell(0, -3, 2, 1));
		m_CellModel.add(new Cell(0, -2, 2, 0));
		m_CellModel.add(new Cell(0, -1, 2, -1));
		m_CellModel.add(new Cell(0, 0, 2, -2));
		m_CellModel.add(new Cell(0, 1, 2, -3));
		m_CellModel.add(new Cell(0, -3, 3, 0));
		m_CellModel.add(new Cell(0, -2, 3, -1));
		m_CellModel.add(new Cell(0, -1, 3, -2));
		m_CellModel.add(new Cell(0, 0, 3, -3));
		//*/
		/*
		m_CellModel.add(new Cell(0, 0, -1, 1));
		m_CellModel.add(new Cell(0, 1, -1, 0));
		m_CellModel.add(new Cell(0, -1, 0, 1));
		m_CellModel.add(new Cell(0, 0, 0, 0));
		m_CellModel.add(new Cell(0, 1, 0, -1));
		m_CellModel.add(new Cell(0, -1, 1, 0));
		m_CellModel.add(new Cell(0, 0, 1, -1));
		//*/

		// m_Adj = createAdjacencyMatrix();
		
		loadQA();
				
		// select QA
		m_QA_number = (int)(Math.random() * m_QA.size());
		
		initNodeStates();
		
		this.addMouseListener(new MyMouseAdapter());
	}
	
	private void loadQA(){
		byte[] q = null, a = null;
		m_QA = new ArrayList<QA>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("qa.txt"));
			while(br.ready()){
				String s1, s2;
				s1 = br.readLine();
				s2 = br.readLine();
				
				StringTokenizer st1 = new StringTokenizer(s1, "{}, ");
				StringTokenizer st2 = new StringTokenizer(s2, "{}, ");
				q = new byte[st1.countTokens()];
				a = new byte[st2.countTokens()];

				int i = 0;
				while (st1.hasMoreTokens()){
					q[i++] = Byte.parseByte(st1.nextToken());
				}
				
				i = 0;
				while (st2.hasMoreTokens()){
					a[i++] = Byte.parseByte(st2.nextToken());
				}
				
				m_QA.add(new QA(q, a));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e){
			e.printStackTrace();
		}
		
	}
	
	
	private void initNodeStates(){
		for (int i = 0; i < m_CellModel.getCells().size(); i++){
			Cell node = m_CellModel.getCells().get(i);
			byte text = m_QA.get(m_QA_number).q[i];
			byte goal = m_QA.get(m_QA_number).a[i];
			node.setState(text, goal, text == 0);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (Cell node : m_CellModel.getCells()) {
			double[] a = node.EuCoordinate();
			int x = (int) (a[0] * radius + 0.5 + this.getWidth() / 2);
			int y = (int) (this.getHeight() / 2 - a[1] * radius + 0.5);
			
			//g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
			drawHexagon(g, x, y, false);
			
			if (m_CurrentNode != null && node == m_CurrentNode){
				g.setColor(Color.blue);
				drawHexagon(g, x, y, true);
				g.setColor(Color.black);
			}
			
			g.setFont(new Font("Arial", Font.PLAIN, radius));
			if (node.text != 0){
				if (node.editable){
					if (m_Hint && node.text != node.goal){
						g.setColor(Color.gray);
						drawString(g, x, y, "" + (node.text));
						g.setColor(Color.black);
					}
					else 
						drawString(g, x, y, "" + (node.text));
				}
				else {
					g.setColor(Color.red);
					drawString(g, x, y, "" + (node.text));
					g.setColor(Color.black);
				}
			}
			else {
				/*if (m_ShowAnswer)
					drawString(g, x, y, "" + (node.goal));
				*/
			}
		}
		
	}

	private void drawHexagon(Graphics g, int center_x, int center_y, boolean bold) {
		int[] x = new int[6];
		int[] y = new int[6];
		x[0] = (int) (center_x - radius / Math.sqrt(3) + 0.5);
		y[0] = (int) (center_y + radius + 0.5);
		x[1] = (int) (center_x - radius * 2 / Math.sqrt(3) + 0.5);
		y[1] = center_y;
		x[2] = x[0];
		y[2] = (int) (center_y - radius + 0.5);
		x[3] = (int) (center_x + radius / Math.sqrt(3) + 0.5);
		y[3] = y[2];
		x[4] = (int) (center_x + radius * 2 / Math.sqrt(3) + 0.5);
		y[4] = y[1];
		x[5] = (int) (center_x + radius / Math.sqrt(3) + 0.5);
		y[5] = y[0];
		
		if (bold){
			//creates a copy of the Graphics instance
	        Graphics2D g2d = (Graphics2D) g.create();

	        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, 
	        		BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	        g2d.setStroke(dashed);

	        g2d.drawPolygon(x, y, 6);
	        
	        //gets rid of the copy
	        g2d.dispose();
		}
		else {
			g.drawPolygon(x, y, 6);
		}
		
	}

	private void drawString(Graphics g, int center_x, int center_y, String s) {
		FontMetrics fm = g.getFontMetrics();
		Rectangle r = fm.getStringBounds(s, g).getBounds();
		g.drawString(s, (int) (center_x - r.getWidth() / 2), 
				(int) (center_y + r.getHeight() / 2 - r.getHeight() / 5));
	}

	public Cell getCurrentNode(){
		return m_CurrentNode;
	}
	
	public void setHint(boolean flag){
		m_Hint = flag;
	}
	
	
	private class MyMouseAdapter implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO 自動產生的方法 Stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO 自動產生的方法 Stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO 自動產生的方法 Stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO 自動產生的方法 Stub
			int ex = e.getX();
			int ey = e.getY();
			int r = MyJPanel.radius;
			
			for (Cell node : m_CellModel.getCells()){
				double[] a = node.EuCoordinate();
				int x = (int) (a[0] * radius + 0.5 + MyJPanel.this.getWidth() / 2);
				int y = (int) (MyJPanel.this.getHeight() / 2 - a[1] * radius + 0.5);
				if ((x-ex)*(x-ex) + (y-ey)*(y-ey) < r * r){
					
					if (node.editable){
						// System.out.println(""+states.get(i).text); //DEBUG
						MyJPanel.this.m_CurrentNode = node;
						MyJPanel.this.repaint();
					}
				}
			}
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO 自動產生的方法 Stub
			
		}
		
	}
	
	public void newQA(){
		m_QA_number = (int)(Math.random() * m_QA.size());
		initNodeStates();
		repaint();
	}
}


class QA {
	byte[] q;
	byte[] a;
	
	//建構子
	QA(byte[] q, byte[] a){
		this.q = q;
		this.a = a;
	}
}