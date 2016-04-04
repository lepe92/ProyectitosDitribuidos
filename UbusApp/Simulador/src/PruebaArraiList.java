
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author eejl_
 */
public class PruebaArraiList {
    public static void main(String[] args) {
        ArrayList<String> m= new ArrayList();
        m.add("hola");
        m.add("hola2");
        m.add("hola3");
        ArrayList<String> n= new ArrayList();
        n.add("hola");
        n.add("hola2");
        n.add("hola3");
        System.out.println(m.size());
        
        n.remove(1);
        m.remove(1);
        
        System.out.println(n.get(1));
        System.out.println(m.get(1));
        
    }
}
