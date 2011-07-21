package org.indy256.calc;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Calc extends JFrame {

	JLabel expressionLabel;
	JLabel resultLabel;
	JScrollPane expressionScrollPane;
	JScrollPane resultScrollPane;

	JButton exit = new JButton("Exit");

	static String format(double res) {
		if (Double.isNaN(res)) {
			return "Error";
		}
		DecimalFormat df = new DecimalFormat("0.0000000000000000000", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		String s = df.format(res);
		while (s.indexOf('.') != -1 && (s.charAt(s.length() - 1) == '0' || s.charAt(s.length() - 1) == '.'))
			s = s.substring(0, s.length() - 1);
		return s;
	}

	void createComponents() {
		final JTextArea expressionTextArea = new JTextArea(5, 50);
		expressionTextArea.setEditable(true);

		expressionScrollPane = new JScrollPane(expressionTextArea);
		expressionTextArea.setCaretPosition(0);

		final JTextArea resultTextArea = new JTextArea(5, 50);
		resultTextArea.setEditable(true);

		resultScrollPane = new JScrollPane(resultTextArea);
		resultTextArea.setCaretPosition(0);

		expressionLabel = new JLabel("Expression:");
		resultLabel = new JLabel("Result:");
		expressionTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					double res = ExpressionParser.eval(expressionTextArea.getText());
					resultTextArea.setText(format(res));
				} catch (Exception ex) {
					resultTextArea.setText(ex.getMessage());
				}
			}
		});
	}

	JPanel createWorkingDayPanel() {
		JPanel pnl = new JPanel(new GridBagLayout());
		pnl.setBorder(BorderFactory
				.createTitledBorder("Supported operations: + - * / ^ ! sin cos tan log2 log exp asin acos atan factorize(n) isprime(n) nextprime(n) catalan(n) partitions(n) gcd(a,b) lcm(a,b) binomial(n,m),  constants: PI E"));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(4, 4, 4, 4);

		gbc.gridx = 0;
		gbc.gridy = 0;
		pnl.add(expressionLabel, gbc);

		++gbc.gridy;
		pnl.add(expressionScrollPane, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		pnl.add(resultLabel, gbc);

		++gbc.gridy;
		pnl.add(resultScrollPane, gbc);
		return pnl;
	}

	void createLayout() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		add(createWorkingDayPanel(), gbc);
	}

	public Calc() {
		// setSize(new Dimension(600, 130));
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/calc.png")));
		setLayout(new GridBagLayout());
		setTitle("Calc");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		createComponents();
		createLayout();
		pack();
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		JFrame frame = new Calc();
		frame.setVisible(true);
	}
}
