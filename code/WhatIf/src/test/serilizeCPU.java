package test;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
 
public class serilizeCPU implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name="SheepMu";
    private int age=24;
    
    
    public static void main(String[] args)
    {//以下代码实现序列化
        try
        {
        	
        	long startTime = System.currentTimeMillis();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("D:\\a.txt"));//输出流保存的文件名为 my.out ；ObjectOutputStream能把Object输出成Byte流
           
            serilizeCPU myTest=new serilizeCPU();
            oos.writeObject(myTest); 
            oos.flush();  //缓冲流 
            System.out.println(System.currentTimeMillis() - startTime);
            oos.close(); //关闭流
            
        } catch (FileNotFoundException e) 
        {        
            e.printStackTrace();
        } catch (IOException e) 
        {
            e.printStackTrace();
        } 
      //  fan();//调用下面的  反序列化  代码
    }
    
    public static void fan()//反序列的过程
    {    
         ObjectInputStream oin = null;//局部变量必须要初始化
        try
        {
            oin = new ObjectInputStream(new FileInputStream("my.out"));
        } catch (FileNotFoundException e1)
        {        
            e1.printStackTrace();
        } catch (IOException e1)
        {
            e1.printStackTrace();
        }      
        serilizeCPU mts = null;
        try {
            mts = (serilizeCPU ) oin.readObject();//由Object对象向下转型为MyTest对象
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }     
         System.out.println("name="+mts.name);    
         System.out.println("age="+mts.age);    
    }
}