package imult;
/*
 * Class StudentCode: class for students to implement
 * the specific methods required by the assignment:
 * add()
 * sub()
 * koMul()
 * koMulOpt()
 * (See coursework handout for full method documentation)
 */

import java.io.File;

public class StudentCode {
  public static BigInt add(BigInt A, BigInt B) {
      BigInt result = new BigInt();         
      int maxLength = Math.max(A.length(), B.length());  
      
      DigitAndCarry dc =new DigitAndCarry();      
      Unsigned carry = new Unsigned(0);
      
      for(int i=0; i<=maxLength;i++) 
      {          
         dc= Arithmetic.addDigits(A.getDigit(i), B.getDigit(i), carry);
         carry = dc.getCarry();
         result.setDigit(i, dc.getDigit());          
      }
      
    return result;
    // student needs to implement
  }

  public static BigInt sub(BigInt A, BigInt B) {
      BigInt result = new BigInt();
      int maxLength = Math.max(A.length(), B.length());       
      
      DigitAndCarry dc;
      Unsigned carry = new Unsigned(0);
      
      for(int i=0; i<=maxLength;i++) 
      {          
         dc= Arithmetic.subDigits(A.getDigit(i), B.getDigit(i), carry);
         carry = dc.getCarry();
         result.setDigit(i, dc.getDigit());          
      }
      
    return result;
  }

  public static BigInt koMul(BigInt A, BigInt B) {
      
      
      if(A.length() <= 1 && B.length() <= 1) 
      {
          BigInt result = new BigInt();
          
          DigitAndCarry dc = Arithmetic.mulDigits(A.getDigit(0),B.getDigit(0));
          result.setDigit(0, dc.getDigit());//setting result to a0
          result.setDigit(1, dc.getCarry());//setting result to a0b0
          
          return result;
      }
      
      int N= Math.max(A.length(), B.length());
      double floor = Math.floor(N/2);
      
      BigInt alpha0 = A.split(0, (int) (floor-1));//floor
      BigInt alpha1 = A.split((int)(floor), N-1);//ceiling
      
      BigInt beta0 = B.split(0, (int) (floor-1));//floor
      BigInt beta1 = B.split((int)(floor), N-1);//ceiling
      
      
      //compute sub-expressions
      BigInt l = koMul(alpha0,beta0);
      BigInt h = koMul(alpha1,beta1);
      //m = (a0+a1)(b0+b1)-(l+h)
      BigInt m = sub( (koMul(add(alpha0,alpha1),add(beta0,beta1))) , add(l,h)); 
      
      
      m.lshift((int) floor); //B^(n/2)
      h.lshift((int) (2*floor)); //B^2(n/2)
      
      return add(l, add(m, h)); 
      //l +mB^(n/2) + hB^2(n/2)
       
  }

  public static BigInt koMulOpt(BigInt A, BigInt B) {
      
      
      if(Math.min(A.length(), B.length())>10) {//shortest one biggest than 10 KO
          int N= Math.max(A.length(), B.length());
          if(N<=1) {
              BigInt result = new BigInt();
              
              DigitAndCarry dc = Arithmetic.mulDigits(A.getDigit(0),B.getDigit(0));
              result.setDigit(0, dc.getDigit());
              result.setDigit(1, dc.getCarry());
              
              return result;
          }
          
          double floor = Math.floor(N/2);
          
          BigInt alpha0 = A.split(0, (int) (floor-1));//floor
          BigInt alpha1 = A.split((int)(floor), N-1);//ceiling
          
          BigInt beta0 = B.split(0, (int) (floor-1));//floor
          BigInt beta1 = B.split((int)(floor), N-1);//ceiling
          
                    
          //compute sub-expressions
          BigInt l = koMulOpt(alpha0,beta0);
          BigInt h = koMulOpt(alpha1,beta1);
          //m = (a0+a1)(b0+b1)-(l+h)
          BigInt m = sub( (koMulOpt(add(alpha0,alpha1),add(beta0,beta1))) , add(l,h)); 
          
          
          m.lshift((int) floor); //mB^(n/2)
          h.lshift((int) (2*floor)); //hB^2(n/2)
          
          return add(l, add(m, h)); 
          //return l +mB^(n/2) + hB^2(n/2)
      }
      else { //School Method
         return Arithmetic.schoolMul(A, B);
      }
  }

  public static void main(String argv[]) throws java.io.FileNotFoundException {

    Boolean opt = true;
    Unsigned m = new Unsigned(1);
    Unsigned n = new Unsigned(10);
    Unsigned t = new Unsigned(90);
    
    
    //GET RUN TIMES
    File koMulOptTimes = new File("koMulOptTimes.txt");
    BigIntMul.getRunTimes(m, n, t, koMulOptTimes, opt);
    
    //GET RATIOS
    File getRatios = new File("getRatios.txt");
    Unsigned xOver = new Unsigned(80);
    BigIntMul.getRatios(m, n, t, getRatios, xOver);
    
    //PLOT RUN TIMES
    double c = 0.0038524208657259792; 
    double a = 0.001952067673133806;
    BigIntMul.plotRunTimes(c, a, koMulOptTimes);
    
  }
} //end StudentCode class