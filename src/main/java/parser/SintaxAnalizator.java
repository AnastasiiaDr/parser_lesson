package parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class SintaxAnalizator {

    public static void main(String[] args) throws ParserException {
   //     BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        Parser myParser = new Parser();

  //      for (;;) {
            try {
       //         System.out.print("Enter expression\n");
         //       String str = reader.readLine();
              //  String str = "(100 - 5) * 14 / 6";
                String str = "((((100-2)*(2-10+3)-4*2+2)-2)+1)+2";
           //     if (str.equals(""))
             //       break;
                double result = myParser.evaluate(str);
                DecimalFormatSymbols s = new DecimalFormatSymbols();
                s.setDecimalSeparator('.');
                DecimalFormat f = new DecimalFormat("#,###.00", s);
                System.out.printf("%s = %s%n", str, f.format(result));
            } catch (ParserException e) {
                System.out.println(e);
            } catch (Exception e) {
                System.out.println(e);
            }
    //    }

    }

}
