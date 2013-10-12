package org.indy256.calc;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedList;

public class ExpressionParser {

	static enum Operator {
		SIN("sin"), COS("cos"), TAN("tan"), LOG("log"), LOG2("log2"), EXP("exp"), ASIN("asin"), ACOS("acos"), ATAN(
				"atan"), PARTITIONS("partitions"), FACTORIZE("factorize"), ISPRIME("isprime"), NEXTPRIME("nextprime"), CATALAN(
				"catalan"), GCD("gcd"), LCM("lcm"), BINOMIAL("binomial"), PLUS("+"), MINUS("-"), UNARY_PLUS("+"), UNARY_MINUS(
				"-"), MULTIPLY("*"), DIVIDE("/"), MOD("%"), POW("^"), FACTORIAL("!"), OPEN_BRACKET("("), CLOSE_BRACKET(")"), COMMA(
				","), PI("PI"), E("E");
		final String name;

		Operator(String name) {
			this.name = name;
		}

		static Operator byName(String name, boolean canUnary) {
			for (Operator op : values()) {
				if (op.name.equals(name)) {
					if (canUnary) {
						if (op == PLUS) {
							return UNARY_PLUS;
						}
						if (op == MINUS) {
							return UNARY_MINUS;
						}
					}
					return op;
				}
			}
			return null;
		}
	}

	static EnumSet<Operator> functions = EnumSet.of(Operator.SIN, Operator.COS, Operator.TAN, Operator.LOG,
			Operator.LOG2, Operator.EXP, Operator.ASIN, Operator.ACOS, Operator.ATAN, Operator.PARTITIONS,
			Operator.FACTORIZE, Operator.ISPRIME, Operator.NEXTPRIME, Operator.CATALAN, Operator.BINOMIAL,
			Operator.GCD, Operator.LCM);
	static EnumSet<Operator> operators = EnumSet.of(Operator.PLUS, Operator.MINUS, Operator.UNARY_PLUS,
			Operator.UNARY_MINUS, Operator.MULTIPLY, Operator.DIVIDE, Operator.MOD, Operator.POW, Operator.FACTORIAL, Operator.COMMA);
	static EnumSet<Operator> rightAssoc = EnumSet.of(Operator.POW, Operator.UNARY_PLUS, Operator.UNARY_MINUS);
	static EnumSet<Operator> unary = EnumSet.of(Operator.UNARY_PLUS, Operator.UNARY_MINUS);
	static EnumSet<Operator> constants = EnumSet.of(Operator.PI, Operator.E);
	static EnumMap<Operator, Double> constantValues = new EnumMap<Operator, Double>(Operator.class);

	static {
		constantValues.put(Operator.PI, Math.PI);
		constantValues.put(Operator.E, Math.E);
	}

	static boolean isDelim(char c) {
		return Character.isWhitespace(c);
	}

	static boolean isOperator(Operator op) {
		return operators.contains(op);
	}

	static boolean isFunction(Operator op) {
		return functions.contains(op);
	}

	static boolean isConstant(Operator op) {
		return constants.contains(op);
	}

	static boolean isLeftAssoc(Operator op) {
		return !rightAssoc.contains(op);
	}

	static boolean isUnary(Operator op) {
		return unary.contains(op);
	}

	static void assertTrue(boolean ok, String msg) {
		if (!ok) {
			throw new RuntimeException(msg);
		}
	}

	static void processOperator(LinkedList<Double> st, Operator op) {
		assertTrue(op != Operator.OPEN_BRACKET, "Expected )");
		if (isFunction(op) || op == Operator.FACTORIAL || isUnary(op)) {
			assertTrue(st.size() >= 1, "Not enough operands");
			double v = st.removeLast();
			switch (op) {
				case FACTORIAL:
					st.add(Functions.factorial(v));
					break;
				case FACTORIZE:
					Functions.factorize(v);
					break;
				case ISPRIME:
					Functions.isPrime(v);
					break;
				case NEXTPRIME:
					st.add(Functions.nextPrime(v));
					break;
				case CATALAN:
					st.add(Functions.catalan(v));
					break;
				case UNARY_PLUS:
					st.add(v);
					break;
				case UNARY_MINUS:
					st.add(-v);
					break;
				case SIN:
					st.add(Math.sin(v));
					break;
				case COS:
					st.add(Math.cos(v));
					break;
				case TAN:
					st.add(Math.tan(v));
					break;
				case LOG2:
					st.add(Math.log(v) / Math.log(2));
					break;
				case LOG:
					st.add(Math.log(v));
					break;
				case EXP:
					st.add(Math.exp(v));
					break;
				case PARTITIONS:
					st.add(Functions.partitions((int) v));
					break;
				case ASIN:
					st.add(Math.asin(v));
					break;
				case ACOS:
					st.add(Math.acos(v));
					break;
				case ATAN:
					st.add(Math.atan(v));
					break;
			}
			if (op == Operator.BINOMIAL || op == Operator.GCD || op == Operator.LCM) {
				assertTrue(st.size() >= 1, "Not enough operands");
				double w = st.removeLast();
				switch (op) {
					case BINOMIAL:
						st.add(Functions.binomial(v, w));
						break;
					case GCD:
						st.add(Functions.gcd(v, w));
						break;
					case LCM:
						st.add(Functions.lcm(v, w));
						break;
				}
			}
		} else {
			assertTrue(st.size() >= 2, "Not enough operands");
			double r = st.removeLast();
			double l = st.removeLast();
			switch (op) {
				case COMMA:
					st.add(r);
					st.add(l);
					break;
				case PLUS:
					st.add(l + r);
					break;
				case MINUS:
					st.add(l - r);
					break;
				case MULTIPLY:
					st.add(l * r);
					break;
				case DIVIDE:
					assertTrue(Math.abs(r) > 1e-12, "Division by zero");
					st.add(l / r);
					break;
				case MOD:
					assertTrue(Math.abs(r) > 1e-12, "Division by zero");
					st.add(l % r);
					break;
				case POW:
					st.add(Math.pow(l, r));
					break;
			}
		}
	}

	static boolean isDigitOrPoint(char x) {
		return Character.isDigit(x) || x == '.';
	}

	static int priority(Operator op) {
		switch (op) {
			case COMMA:
				return 0;
			case PLUS:
			case MINUS:
				return 1;
			case MULTIPLY:
			case DIVIDE:
			case MOD:
				return 2;
			case POW:
				return 3;
			case FACTORIAL:
				return 4;
			case UNARY_PLUS:
			case UNARY_MINUS:
				return 5;
			default:
				return -1;
		}
	}

	public static double eval(String s) {
		LinkedList<Double> st = new LinkedList<Double>();
		LinkedList<Operator> op = new LinkedList<Operator>();
		boolean canUnary = true;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (isDelim(c)) {
				continue;
			}
			Operator curOp = Operator.byName("" + c, canUnary);
			if (curOp == Operator.OPEN_BRACKET) {
				op.add(Operator.OPEN_BRACKET);
				canUnary = true;
			} else if (curOp == Operator.CLOSE_BRACKET) {
				while (Operator.OPEN_BRACKET != op.getLast()) {
					processOperator(st, op.removeLast());
				}
				op.removeLast();
				if (!op.isEmpty() && isFunction(op.getLast())) {
					processOperator(st, op.removeLast());
				}
				canUnary = false;
			} else if (isOperator(curOp)) {
				while (!op.isEmpty()
						&& (priority(curOp) < priority(op.getLast()) || priority(curOp) == priority(op.getLast())
						&& isLeftAssoc(curOp))) {
					processOperator(st, op.removeLast());
				}
				op.add(curOp);
				canUnary = curOp == Operator.PLUS || curOp == Operator.MINUS || curOp == Operator.MULTIPLY
						|| curOp == Operator.DIVIDE || curOp == Operator.COMMA;
			} else if (isDigitOrPoint(s.charAt(i))) {
				String operand = "";
				while (i < s.length() && isDigitOrPoint(s.charAt(i))) {
					operand += s.charAt(i++);
				}
				--i;
				try {
					st.add(Double.parseDouble(operand));
				} catch (NumberFormatException e) {
					assertTrue(false, "Incorrect number format: " + operand);
				}
				canUnary = false;
			} else if (Character.isLetter(c)) {
				String lexeme = "";
				while (i < s.length() && (Character.isLetter(s.charAt(i)) || Character.isDigit(s.charAt(i)))) {
					lexeme += s.charAt(i++);
				}
				Operator cur = Operator.byName(lexeme, false);
				if (isConstant(cur)) {
					st.add(constantValues.get(cur));
				} else {
					assertTrue(isFunction(cur), "No such function or constant: " + lexeme);
					for (; i < s.length() && isDelim(s.charAt(i)); i++) {
						;
					}
					assertTrue(i != s.length() && s.charAt(i) == '(', "Expected (");
					op.add(cur);
				}
				--i;
				canUnary = false;
			} else {
				assertTrue(false, "Unexpected character: " + c);
			}
		}
		while (!op.isEmpty()) {
			processOperator(st, op.removeLast());
		}
		assertTrue(!st.isEmpty(), "");
		assertTrue(st.size() == 1, "Operator expected");
		return st.get(0);
	}

	public static void main(String[] args) throws Exception {
		final String exp = "3!+1";
		double res = eval(exp);
		System.out.println(res);
	}
}
