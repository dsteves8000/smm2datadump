import java.io.*;
public class idalgorithm //Follows the algorithm described in https://github.com/kinnay/NintendoClients/wiki/Data-Store-Codes#super-mario-maker-2
{
   static int [] k = {0,0,0,1,0,1,1,0,1,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,1,1,1,1,1,0,0}; //377544828 in decimal
   static File oldoutput, output;
   public static void main(String [] args)
   {
      String id = "XXC7CPS6G"; //maker code (1)
      //String id = "9SF77C4MG"; //level code (0)

      //From id to DataID
      int d = idToData(id);
      System.out.println(d);   
         
      //From DataID to id
      String a = dataToId(d, 1);
      //String a = dataToId(d, 0);
      System.out.println(a);
   }
   //DATATOID
   public static String dataToId(int data, int isMaker)
   {
      int [] unxored = populate32(data);
      int [] xored = xor(unxored, k);
      int [] x = populate44(xored, data, isMaker);
      long dec = decid(x);
      return getid(dec);
   }
   public static String getid(long dec)
   {
      String s = "";
      while (dec > 0)
      {
         int a = (int) (dec % 30);
         if(a >= 0  && a < 10) { a += 48; }
         if(a >= 10 && a < 35) { a += 55; }
         if(a >= 81 && a < 85) {a+=5;}
         if(a >= 76 && a < 81) {a+=4;}
         if(a >= 71 && a < 76) {a+=3;}
         if(a >= 68 && a < 71) {a+=2;}
         if(a >= 65 && a < 68) {a++;}               
         s += (char) a;
         dec /= 30;
      }
      return s;
   }
   public static long decid(int [] X)
   {
      String bin = "";
      for(int i = 0; i < X.length; i++) 
      {  
         bin += X[i]; 
      }
      return Long.parseLong(bin,2);
   }
   public static int [] populate44(int [] x, int dataid, int isMaker)
   {
      int [] b = new int[44];
      int [] c = checkSum(dataid);
      b[0]=1;
      for(int i = 4;  i < 10; i++) 
      { 
         b[i] = c[i-4]; 
      }
      for(int i = 10; i < 30; i++)
      { 
         b[i] = x[i+2]; 
      }
      b[30]=isMaker;
      b[31]=1;
      for(int i = 32; i < 44; i++) 
      { 
         b[i] = x[i-32]; 
      }
      return b;
   }
   public static int [] populate32(int dataid)
   {
      String s = Integer.toBinaryString(dataid);
      int l = s.length();
      int [] b = new int[32];
      for(int i = 0; i < l; i++)
      { 
         b[i+(32-l)] = s.charAt(i)-48;
      }
      return b;
   }
   public static int [] checkSum(int dataid)
   {
      if(dataid <= 30) 
      {
         dataid += 64;
      }
      int [] b = new int[6];
      if(dataid > 30)
      {
         String s = Integer.toBinaryString((dataid-31)%64);
         int l = s.length();
         for(int i = 0; i < l; i++)
         {
            b[i+(6-l)] = s.charAt(i)-48;    
         }
      }
      return b;
   }
   //IDTODATA
   public static int idToData(String id)
   {
      long dec = idToDec(id);
      int [] b = decToBinary(dec, 44);
      int [] fieldG = concat(b);
      int [] x = xor(fieldG, k);
      return id(x);
   }
   public static int id(int [] X)
   {
      String bin = ""; 
      for(int i = 0; i < X.length; i++) 
      { 
         bin += X[i]; 
      }
      return Integer.parseInt(bin,2);
   }
   public static int [] xor(int [] G, int [] key)
   {
      int [] X = new int [32];
      for(int i = 0; i < G.length; i++)
      {
         if(G[i] != key[i])
         {
            X[i]++;
         }
      }
      return X;
   }
   public static int [] concat(int [] b)
   {
      int [] G = new int [32];
      for(int i = 0; i < b.length; i++)
      {
         if(i >= 10 && i < 30) 
         {
            G[i+2]  = b[i];
         }
         if(i >= 32 && i < 44) 
         {
            G[i-32] = b[i];
         }
      } 
      return G;  
   }
   public static long idToDec(String id)
   {
      id = id.replace("-", "");
      char[] ch = id.toCharArray();
      long dec = 0;
      long p = 1;
      for(int i = 0; i < ch.length; i++) 
      { 
         dec += charConv(ch[i])*p;  
         p *=30 ;
      }
      return dec;
   }
   public static int [] decToBinary(long n, int l)
   {
        long [] binaryNum = new long[l];
        int [] num = new int [l]; 
        int i = 0;
        while (n > 0) 
        { 
            binaryNum[i] = n % 2; n = n / 2; i++; 
        }
        int k = 0;
        for (int j = i - 1; j >= 0; j--) 
        {  
            num[k] = (int) binaryNum[j]; 
            k++;
        }
        return num;
   }
   public static int charConv (char mander)
   {
      int a = (int) mander;    
      //Modified
      if((a >= 66 && a < 69) || (a >= 98 && a < 101)) {a--;}
      if((a >= 70 && a < 73) || (a >= 102 && a < 105)) {a-=2;}
      if((a >= 74 && a < 79) || (a >= 106 && a < 111)) {a-=3;}
      if((a >= 80 && a < 85) || (a >= 112 && a < 117)) {a-=4;}
      if((a >= 86 && a < 90) || (a >= 118 && a < 122)) {a-=5;}
      //Original
      if(a >= 48 && a < 58) { a -= 48; }
      if(a >= 65 && a < 91) { a -= 55; }
      if(a >= 97 && a < 123){ a -= 87; }
      if(a == 32) { a += 4; }
      return a;
   }
}