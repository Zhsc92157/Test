package com.zhsc.test.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculateUtil {

    /**
     * -2表示字符串中含有非法字符
     * -1表示字符串为空
     * 0表示结果计算错误
     * 1表示结果计算正确
     */
    public static int CAL_CODE = 0;


    /**
     * 判断一个字符串中是否有中文字符
     *
     * @param str
     * @return true包含中文字符或字符串为空 false表示不包含中文字符
     */
    public static boolean isContainChinese(String str) {
        if (str == null) {
            CAL_CODE = -1;
            return true;
        }
        Pattern p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
        if (p.matcher(str).find()) {
            CAL_CODE = -2;
            Log.e("isContainChinese", "true");
            return true;
        }
        return false;
    }

    /**
     * 判断一个字符串中是否有英文字符
     *
     * @param str
     * @return 包含a-z||A-Z返回false
     */
    public static boolean isContainEnglish(String str) {
        Pattern p = Pattern.compile("[a-zA-z]");
        if (p.matcher(str).find()) {
            CAL_CODE = -2;
            Log.e("isContainEnglish", "true");
            return true;
        }
        return false;
    }

    public static boolean isCorrect(String str){

        if (isContainChinese(str))
            return false;

        str = symbolConversion(str);

        if(str.contains("=")){
            String formerPart = str.substring(0,str.indexOf("="));
            String latterPart = str.substring(str.indexOf("=")+1);
            if (latterPart.equals(""))
                return false;
            String pattern = "[^0123456789=\\(\\)+\\-\\*/]";
            if (Pattern.matches(pattern,str))
                return false;
            int result = calculate(formerPart);
            if (result == Integer.parseInt(latterPart))
                return true;
            else
                return false;
        }else
            return false;
    }

    /**
     * 将手写的×转换为*，手写÷转换为/
     * @param str
     */
    private static String symbolConversion(String str) {
        char multi = 215;
        String result;
        result = str.replace('x', '*');
        result = result.replace(multi,'*');
        result = result.replace('X','*');
        result = result.replace('/','1');
        result = result.replace('÷','/');
        System.out.println(result);
        return result;
    }

    private static int calculate(String strExpression)
    {
        String s = simplify(strExpression);
        System.out.println("s : "+s);
        String numStr = "";//记录数字
        Stack<Character> opeStack = new Stack<>();//符号站
        int l = s.length();//字符串长度 l
        List<String> list = new ArrayList<>();

        for(int i=0;i<l;i++)
        {
            char ch = s.charAt(i);

            if(isAllOpe(ch))
            {
                if(!numStr.equals(""))
                {
                    list.add(numStr);
                    numStr="";
                }


                if(ch=='(')
                {
                    opeStack.push(ch);
                }
                else if(isOpe(ch))
                {
                    char top = opeStack.peek();
                    if(isGreater(ch, top))
                    // ch优先级大于top 压栈
                    {
                        opeStack.push(ch);
                    }
                    else
                    //否则,将栈内元素出栈,直到遇见 '(' 然后将ch压栈
                    {
                        while(true)
                        //必须先判断一下 后出栈 否则会有空栈异常
                        {
                            char t=opeStack.peek();
                            if(t=='(')
                                break;
                            if(isGreater(ch, t))
                                break;

                            list.add(Character.toString(t));
                            t=opeStack.pop();
                        }
                        opeStack.push(ch);

                    }

                }
                else if(ch==')')
                {
                    char t = opeStack.pop();
                    while(t!='('&&!opeStack.isEmpty())
                    {
                        list.add(Character.toString(t));
                        t = opeStack.pop();
                    }
                }

            }
            else//处理数字
            {
                numStr+=ch;
            }
        }

        //计算后缀表达式
        System.out.println(list.toString());
        Stack<Integer> num = new Stack<>();
        int size = list.size();
        for(int i=0;i<size;i++)
        {
            String t =list.get(i);
            if(isNumeric(t))
            {//将t转换成int 方便计算
                num.push(Integer.parseInt(t));
            }
            else {
                //如果t为运算符则 只有一位
                char c = t.charAt(0);
                if (num.size()>=2) {
                    int b = num.pop();
                    //如果有 算式是类似于 -8-8 这样的需要判断一下栈是否为空
                    int a = num.pop();
                    switch (c) {
                        case '+':
                            num.push(a + b);
                            break;
                        case '-':
                            num.push(a - b);
                            break;
                        case '*':
                            num.push(a * b);
                            break;
                        case '/':
                            num.push(a / b);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return num.pop();
    }

    public static String simplify(String str)
    {
        //负数的处理
        // 处理负数，这里在-前面的位置加入一个0，如-4变为0-4，
        // 细节：注意-开头的地方前面一定不能是数字或者反括号，如9-0,(3-4)-5，这里地方是不能加0的
        // 它的后面可以是数字或者正括号，如-9=>0-9, -(3*3)=>0-(3*3)
        String s = str.replaceAll("(?<![0-9)}\\]])(?=-[0-9({\\[])", "0");
        //将表达式中的 {}[]替换为()
        s = s.replace('[', '(');
        s = s.replace('{', '(');
        s = s.replace(']', ')');
        s = s.replace(']', ')');
        //为了方便将中缀转换为后缀在字符串前后分别加上(,)
        s="("+s+")";

        return s ;
    }

    /**判断字符c是否为合理的运算符
     *
     * @param c
     * @return
     */
    public static boolean isOpe(char c)
    {
        if(c=='+'||c=='-'||c=='*'||c=='/')
            return true;
        else
            return false;
    }

    public static boolean isAllOpe(char c)
    {
        if(c=='+'||c=='-'||c=='*'||c=='/')
            return true;

        else if(c=='('||c==')')
            return true;
        else
            return false;
    }

    public static boolean isGreater(char a,char b) {
        int a1 = getLevel(a);
        int b1 = getLevel(b);
        if(a1>b1)
            return true;
        else
            return false;
    }

    public static int getLevel(char a) {
        if(a=='+')
            return 0;
        else if(a=='-')
            return 1;
        else if(a=='*')
            return 3;
        else if(a=='/')
            return 4;
        else
            return -1;

    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}


