package com.tsystems.javaschool.tasks.calculator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    /**`
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */

    public String evaluate(String statement) {
        String rpn = expressionToRPN(statement);
        String operand = new String();
        Stack<Double> doubleStack = new Stack<>();
        double answerDouble;
        Integer answerInt;
        DecimalFormat decimalFormat = new DecimalFormat("#.####", new DecimalFormatSymbols(Locale.US));

        if (rpn == null || statement == null) {
            return null;
        } else {
            for (int i = 0; i < rpn.length(); i++) {
                if (rpn.charAt(i) == ' ') continue;
                if (getPriority(rpn.charAt(i)) == 0) {
                    while (rpn.charAt(i) != ' ' && getPriority(rpn.charAt(i)) == 0) {
                        operand += rpn.charAt(i);
                        i++;
                        if (i == rpn.length()) break;
                    }
                    doubleStack.push(Double.parseDouble(operand));
                    operand = new String();
                }

                if (getPriority(rpn.charAt(i)) > 1) {
                    double a = doubleStack.pop();
                    double b = doubleStack.pop();

                    if (rpn.charAt(i) == '+') doubleStack.push(b + a);
                    if (rpn.charAt(i) == '-') doubleStack.push(b - a);
                    if (rpn.charAt(i) == '*') doubleStack.push(b * a);
                    if (rpn.charAt(i) == '/') {
                        if (a == 0) return null;
                        else doubleStack.push(b / a);
                    }
                }
            }
        }

        answerDouble = doubleStack.pop();
        answerInt = (int) answerDouble;

        return answerDouble / answerInt == 1 ? answerInt + "" : String.format( "%s", decimalFormat.format(answerDouble));
    }

    public static String expressionToRPN(String expression) {           //преобразование строки с выражением в обратную польскую нотацию
        String current = "";
        Stack<Character> stack = new Stack<>();
        int currentPriority, check = 0;

        Pattern pattern1, pattern2;
        Matcher matcher1, matcher2;

        if (expression == null) {
            current = null;
        } else {
            pattern1 = Pattern.compile("[\\.\\+\\-\\/\\*]{2,}");
            pattern2 = Pattern.compile("[ a-zA-Zа-яА-Я\\s,\\{\\}\\|\\[\\]]");
            matcher1 = pattern1.matcher(expression);
            matcher2 = pattern2.matcher(expression);

            for (int i = 0; i < expression.length(); i ++) {        //проверяем, правильно ли расставленны скобки
                if (check < 0) {
                    current = null;
                }
                if (expression.charAt(i) == '(') {
                    check++;
                    continue;
                }
                if (expression.charAt(i) == ')') {
                    check--;
                }
            }

            if (check != 0) {       //если скобки расставлены неверно строка будет null;
                current = null;
            } else if (matcher1.find() || matcher2.find() || "".equals(expression)) {       //проверяем на наличие недопустимых знаков, если они есть, то строка = null
                current = null;
            } else {                                                                        //иначе преобразуем выражение в обратную польсукю нотацию
                for (int i = 0; i < expression.length(); i++) {
                    currentPriority = getPriority(expression.charAt(i));

                    if (currentPriority == 0) current += expression.charAt(i);

                    if (currentPriority == 1) stack.push(expression.charAt(i));

                    if (currentPriority > 1) {
                        while (!stack.empty()) {
                            current += ' ';
                            if (getPriority(stack.peek()) >= currentPriority) current += stack.pop();
                            else break;
                        }
                        current += ' ';
                        stack.push(expression.charAt(i));
                    }

                    if (currentPriority == -1) {
                        current += ' ';

                        while (getPriority(stack.peek()) != 1) {
                            current += stack.pop();
                        }

                        stack.pop();
                    }
                }
            }
        }

        while (!stack.empty()) {
            current += stack.pop();
        }

        return current;
    }

    public static int getPriority(char token) {     //назначаем приоритет символам выражения

        int result;

        switch (token) {
            case '*':
            case '/':
                result = 3;
                break;
            case '+':
            case '-':
                result = 2;
                break;
            case '(':
                result = 1;
                break;
            case ')':
                result = -1;
                break;
            default:
                result = 0;
                break;
        }
        return result;
    }
}
