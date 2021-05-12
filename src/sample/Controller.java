package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class Controller {

    @FXML
    private TextField TextField1;

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private Button button4;

    @FXML
    private TextField TextField4;

    @FXML
    private TextField TextField2;

    @FXML
    private TextField TextField3;

    @FXML
    private Button button5;


    @FXML
    private Label Label;


    @FXML
    private TextField TextField5;

    @FXML
    private TextField TextField6;

    @FXML
    private TextField TextField7;

    @FXML
    private TextField TextField8;

    @FXML
    private TextField TextField9;

    String outputFileName="";
    byte [] startFile;
    byte [] finishFile;
    int sizeOfFile;
    Stage primaryStage;
    @FXML
    void initialize() {
        //подгрузка
        button1.setOnAction(actionEvent -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("D:\\Laba3_1\\src\\sample\\Tests\\Rezylts"));
            FileInputStream inputStream = null;
            try {
                File file= fileChooser.showOpenDialog(primaryStage);
                outputFileName=file.getName().replace(".","_2.");
                inputStream = new FileInputStream(file);
                sizeOfFile=(int)file.length();
                startFile=new byte[sizeOfFile];
                inputStream.read(startFile);

                int len=100;
                StringBuilder temp=new StringBuilder();
                if(startFile.length<100){
                    len=startFile.length;
                }

                for (int i = 0; i <len; i++) {
                    temp.append(startFile[i]+" ");
                }

                TextField8.setText(temp.toString());
                inputStream.close();
            } catch (IOException | NullPointerException e) {

            }
        });
        button2.setOnAction(actionEvent -> {
            if(finishFile!=null){
                File file3=new File("src/sample"+outputFileName);
                FileOutputStream fileOutputStream= null;
                try {
                    fileOutputStream = new FileOutputStream(file3);
                    fileOutputStream.write(finishFile);
                    fileOutputStream.close();
                } catch (IOException e) {

                }
            }else{
                showErrorWindow("файл ещё не сформирован","");
            }


        });
        button3.setOnAction(actionEvent -> {
            if(isCorrectInput2()){
                finishFile=new byte[sizeOfFile*2];
                int e=Integer.parseInt(TextField4.getText());
                int r=Integer.parseInt(TextField5.getText());
                StringBuilder temp=new StringBuilder();
                int counter=0;
                for (int i = 0; i <startFile.length; i++) {
                    int shifr=fastPowMod((int)startFile[i]+128,e,r);
                    System.out.println();
                    System.out.println("ШИФР"+fastPowMod((int)startFile[i]+128,e,r));
                    String str=getBitsFromInt(shifr);
                    String str1=str.substring(0,8);
                    String str2=str.substring(8,16);
                    System.out.println("s1="+str1);
                    System.out.println("s2="+str2);
                    byte b1=binaryStringToByte(str1);
                    byte b2=binaryStringToByte(str2);
                    finishFile[2*i]=b1;
                    finishFile[2*i+1]=b2;

                    if(counter<100){
                        temp.append(shifr+" ");
                        counter++;
                    }
                }


                TextField9.setText(temp.toString());
            }
        });
        button4.setOnAction(actionEvent -> {
            if(isCorrectInput3()){
                int d=Integer.parseInt(TextField6.getText());
                int r=Integer.parseInt(TextField7.getText());
                StringBuilder temp=new StringBuilder();
                int counter=0;
                finishFile=new byte[sizeOfFile/2];
                for (int i = 0; i <startFile.length; i+=2) {
                    String s1=byteToCorrectString(startFile[i]);
                    String s2=byteToCorrectString(startFile[i+1]);

                    int shifr=Integer.parseInt(s1+s2,2);
                    byte rez=(byte) (fastPowMod(shifr,d,r)-128);
                    System.out.println();
                    System.out.println("shifr="+shifr);
                    System.out.println("deshifr="+fastPowMod(shifr,d,r));
                    System.out.println("s1="+s1);
                    System.out.println("s2="+s2);
                    finishFile[i/2]=rez;
                    if(counter<100){
                        temp.append(rez+" ");
                        counter++;
                    }
                }
                TextField9.setText(temp.toString());
            }

        });
        button5.setOnAction(actionEvent -> {
            if(isCorrectInput1()){
                int p,q,d;
                String text1=TextField1.getText();
                String text2=TextField2.getText();
                String text3=TextField3.getText();
                p=Integer.parseInt(text1);
                q=Integer.parseInt(text2);
                d=Integer.parseInt(text3);
                int[] masOf_E=calcEuclide(calcEilor(p,q),d);
                if(masOf_E[1]<0){
                    masOf_E[1]+=calcEilor(p,q);
                }

                Label.setText("e="+String.valueOf(masOf_E[1])+" r="+p*q);
            }
        });

}


    void showErrorWindow(String str1,String str2) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(str1);
        alert.setContentText(str2);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(new ButtonType("Понятно", ButtonBar.ButtonData.CANCEL_CLOSE));
        alert.showAndWait();
    }

    boolean isCorrectInput1(){
        String text1=TextField1.getText();
        String text2=TextField2.getText();
        String text3=TextField3.getText();
        if(text1.equals("") || text2.equals("") || text3.equals("")){
            showErrorWindow("Не все данные введены","введите исходные данные");
            return false;
        }
        int p,q,d;
        try {
            p=Integer.parseInt(text1);
            q=Integer.parseInt(text2);
            d=Integer.parseInt(text3);
        }catch (NumberFormatException e){
            showErrorWindow("Не все данные  являются целыми числами","проверьте исходные данные");
            return false;
        }

        if(!(isPozitive(p) && isPozitive(q) && isPozitive(d))){
            showErrorWindow("Не все данные  являются положительными","проверьте исходные данные");
            return false;
        }

        if(p*q>=255*255 || p*q<=255){
            showErrorWindow("Произведение параметров P и Q должно быть меньше 65025 и больше чем 255","проверьте исходные данные");
            return false;
        }

        if(!isPrimeNumber(p)){
            showErrorWindow("Параметр P не являеться простым числом","проверьте исходные данные");
            return false;
        }

        if(!isPrimeNumber(q)){
            showErrorWindow("Параметр Q не являеться простым числом","проверьте исходные данные");
            return false;
        }

        if(d<=1 || d>=calcEilor(p,q) || !isCoprime(calcEilor(p,q),d) ){
            showErrorWindow("Параметр D не удовлеетворяет нужным условиям","1) быть больше 1\n2) быть меньше фи от r\n3) быть взаимно простым с фи от r");
            return false;
        }
        /*if(startFile==null){
            showErrorWindow("Файл для работы не загружен","откройте файл");
            return false;
        }*/

        return true;
    }

    boolean isCorrectInput2(){
        boolean answer=true;
        String text1=TextField4.getText();
        String text2=TextField5.getText();

        if(text1.equals("") || text2.equals("")){
            showErrorWindow("Не все данные введены","введите исходные данные");
            return false;
        }
        int e,r;
        try {
            e=Integer.parseInt(text1);
            r=Integer.parseInt(text2);
        }catch (NumberFormatException ex){
            showErrorWindow("Не все данные  являются целыми числами","проверьте исходные данные");
            return false;
        }


        if(e<1  || e>=r || r<=255){
            showErrorWindow("Параметры e и r не удовлетворяют нужным условиям","1) e должно быть больше 1\n2) e должно быть меньше чем r\n3) r должно быть больше 255 ");
            return false;
        }
        if(startFile==null ){
            showErrorWindow("Файл для работы не загружен","откройте файл");
            return false;
        }
        return answer;
    }

    boolean isCorrectInput3(){
        boolean answer=true;
        String text1=TextField6.getText();
        String text2=TextField7.getText();

        if(text1.equals("") || text2.equals("")){
            showErrorWindow("Не все данные введены","введите исходные данные");
            return false;
        }
        int d,r;
        try {
            d=Integer.parseInt(text1);
            r=Integer.parseInt(text2);
        }catch (NumberFormatException ex){
            showErrorWindow("Не все данные  являются целыми числами","проверьте исходные данные");
            return false;
        }


        if(d<1 || r<=255 || d>=r){
            showErrorWindow("Параметры d и r не удовлетворяют нужным условиям","1) d должно быть больше 1\n2) d должно быть меньше чем r\n3) r должно быть больше 255 ");
            return false;
        }
        if(startFile==null){
            showErrorWindow("Файл для работы не загружен","откройте файл");
            return false;
        }
        return answer;
    }

    boolean isPozitive(int num){
        return num>0;
    }

    boolean isCoprime(int x,int y){
        int[] mas=calcEuclide(x,y);
        return mas[2]==1;
    }

    public static int calcEilor(int p,int q){
        int x=(p-1)*(q-1);
        return x;
    }

    public  boolean isPrimeNumber(int number){
        if (number<2){
            return false;
        }
        if(number==2){
            return true;
        }
        boolean ansver=true;
        for (int i = 2; i <number; i++) {
            if(number%i==0){
                ansver=false;
                break;
            }
        }

        return ansver;
    }

    //A>=B
    public  int[] calcEuclide(int a,int b){
        int[] rez=new int[3];
        int d0=a;
        int d1=b;
        int x0=1;
        int x1=0;
        int y0=0;
        int y1=1;
        while(d1>1){
            int q=d0/d1;
            int d2=d0%d1;
            int x2=x0-q*x1;
            int y2=y0-q*y1;

            d0=d1;
            d1=d2;
            x0=x1;
            x1=x2;
            y0=y1;
            y1=y2;
        }
        rez[0]=x1;
        rez[1]=y1;
        rez[2]=d1;
        return rez;
    }

    //a^z mod n
    public  int fast_exp(int a,int z,int n){
        int a1=a;
        int z1=z;
        int x=1;
        while (z1!=0){
            while (z1%2!=0){
                z1=z1%2;
                a1=(a1*a1)%n;
            }
            z1=z1-1;
            x=(x*a1)%n;
        }
        return x;
    }
    //a^z mod n
    static int fastPowMod(int a,int z,int n)
    {
        int a1=a;
        int z1=z;
        int x=1;

        while(z1!=0){
            while (z1 % 2==0){
                z1=z1/2;
                a1=(a1*a1) %n;
            }
            z1-=1;
            x=(x*a1)%n;
        }
        return x;
    }


    private  boolean getBitFromByte(byte b, int position) {
        return ((b & (1<< position))>> position) == 1;
    }
    byte setBitInByte(byte b, boolean bit, int position) {
        if (bit)
            return (byte)(b | (1<< position));
        else
            return (byte)(b & ~(1>> position));
    }
    byte setBitInByteFast(byte b, int bit, int position) {
        return (byte) (((1 - bit) * ~0) ^ ((((1 - bit) * ~0) ^ b) | (1<< position)));
    }



    public static byte binaryStringToByte(String str){
        if (str.equals("10000000")){
            return -128;
        }
        if (str.charAt(0)=='1'){
            str=str.replaceFirst("1","-0");
        }
        return Byte.parseByte(str,2);
    }

    public  String byteToCorrectString(byte b){
        String str;
        if (b==-128){
            return "10000000";
        }
        if (b<0){
            b=(byte)Math.abs(b);
            str=getBitsFromByte(b);
            str=str.replaceFirst("0","1");

        }else{
            str=getBitsFromByte(b);
        }
        return str;
    }

    private  String getBitsFromInt(int b) {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < 16; i++)
            result.append((b & (1 << i)) == 0 ? "0" : "1");
        return reverseString(result.toString());
    }

    private  String getBitsFromByte(byte b){
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < 8; i++)
            result.append((b & (1 << i)) == 0 ? "0" : "1");
        return reverseString(result.toString());
    }

    public  String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }
}
