package org.indy256.calc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Calc extends JFrame {

	JLabel expressionLabel;
	JLabel resultLabel;
	JTextArea expressionTextArea;
	JScrollPane expressionScrollPane;
	JScrollPane resultScrollPane;
	JTextArea helpTextArea;

	JButton exit = new JButton("Exit");

	static String format(double res) {
		if (Double.isNaN(res))
			return "Error";
		DecimalFormat df = new DecimalFormat("0.0000000000000000000", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		String s = df.format(res);
		while (s.indexOf('.') != -1 && (s.charAt(s.length() - 1) == '0' || s.charAt(s.length() - 1) == '.'))
			s = s.substring(0, s.length() - 1);
		if (s.indexOf('.') == -1) {
			while (s.length() % 3 != 0)
				s = ' ' + s;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < s.length() / 3; i++)
				sb.append(s.substring(i * 3, i * 3 + 3)).append(' ');
			s = sb.toString().trim();
		}
		return s;
	}

	void createComponents() {
		expressionTextArea = new JTextArea(5, 50);
		expressionScrollPane = new JScrollPane(expressionTextArea);

		final JTextArea resultTextArea = new JTextArea(5, 50);
		resultScrollPane = new JScrollPane(resultTextArea);

		final String info = "Operations: + - * / % ^ ! sin cos tan log2 log exp asin acos atan, Constants: PI E\nFunctions: factorize(n) isprime(n) nextprime(n) catalan(n) partitions(n) gcd(a,b) lcm(a,b) binomial(n,m)";
		helpTextArea = new JTextArea(info, 2, 50);
		helpTextArea.setEditable(false);

		expressionLabel = new JLabel("Expression:");
		resultLabel = new JLabel("Result:");
		expressionTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					double res = ExpressionParser.eval(expressionTextArea.getText().replaceAll(" ", ""));
					resultTextArea.setText(format(res));
				} catch (Exception ex) {
					resultTextArea.setText(ex.getMessage());
				}
			}
		});
	}

	JPanel createWorkingDayPanel() {
		JPanel pnl = new JPanel(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(4, 4, 4, 4);

		gbc.gridx = 0;
		gbc.gridy = 0;
		pnl.add(helpTextArea, gbc);

		++gbc.gridy;
		pnl.add(expressionLabel, gbc);

		++gbc.gridy;
		pnl.add(expressionScrollPane, gbc);

		gbc.gridx = 0;
		++gbc.gridy;
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
		expressionTextArea.requestFocusInWindow();
	}

	public static void main(String[] args) {
		Frame frame = new Calc();
		frame.setVisible(true);
	}
}
