import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;

public class HoneycombGame extends JFrame {
	MyJPanel m_JPanel;
	JPanel m_RightPanel;
	JLabel m_Result;
	
	// 建構子
	public HoneycombGame() throws HeadlessException {
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		m_JPanel = new MyJPanel();
		c.add(m_JPanel, BorderLayout.CENTER);
		m_JPanel.setBackground(Color.white);
		
		m_RightPanel = new JPanel(new GridBagLayout());
		c.add(m_RightPanel, BorderLayout.EAST);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0; //水平不拉伸
		gbc.gridwidth = 0;
		gbc.gridheight = 1;
		gbc.ipady = 20; // 拉高
		
		m_Result = new JLabel("尚未完成");
		m_RightPanel.add(m_Result, gbc);
		
		JButton m_New = new JButton("下一題");
		m_New.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自動產生的方法 Stub
				m_JPanel.newQA();
			}
			
		});
		m_RightPanel.add(m_New, gbc);
		
		m_RightPanel.add(new JLabel(" "), gbc);

		gbc.gridwidth = 1;
		gbc.weightx = 0;
		JLabel txt1 = new JLabel("提示：");	
		m_RightPanel.add(txt1, gbc);
		
		JToggleButton hintButton = new JToggleButton ("Off");
		ItemListener itemListener = new ItemListener() {
		    public void itemStateChanged(ItemEvent itemEvent) {
		        int state = itemEvent.getStateChange();
		        if (state == ItemEvent.SELECTED) {
		        	hintButton.setText("On"); // // 顯示提示
		        	m_JPanel.setHint(true);
		        	m_JPanel.repaint();
		        } else {
		        	hintButton.setText("Off"); // 不顯示提示
		        	m_JPanel.setHint(false); 
		        	m_JPanel.repaint();
		        }
		    }
		};
		hintButton.addItemListener(itemListener);
		gbc.weightx = 0; //水平不拉伸
		gbc.gridwidth = 0; //該屬性是設置組件水平所佔用的格子數，如果為0，就說明該組件是該橫列的最後一個
		m_RightPanel.add(hintButton, gbc);
		
		// seperator
		gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
		JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
		sep.setPreferredSize(new Dimension(180,1));
		m_RightPanel.add(sep, gbc);
		
		gbc.gridwidth = 1; //佔水平1格
		gbc.weightx = 0; //水平不拉伸
		JLabel txt2 = new JLabel("數字：");	
		m_RightPanel.add(txt2, gbc);
		
		int n = m_JPanel.m_CellModel.getCells().size();
		String[] labels = new String[n+1];
		for (int i = 0; i < n + 1; i++){
			labels[i] = i == 0 ? " " : Integer.toString(i);
		}
		JComboBox cbox = new JComboBox(labels);
		gbc.ipady = 20; // 拉高
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0; //水平不拉伸
		gbc.gridwidth = 0;
		m_RightPanel.add(cbox, gbc);
		
		cbox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自動產生的方法 Stub
				Cell node = m_JPanel.getCurrentNode();
				if ( node != null){
					node.setText((byte)cbox.getSelectedIndex());
					m_JPanel.repaint();
					
					if (m_JPanel.m_CellModel.isCompleted()){
						m_Result.setText("過關");
					}
				}
			}
			
		});
		
	}

	public static void main(String[] args) {
		HoneycombGame f = new HoneycombGame();
		f.setSize(1000, 800);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
	
}
