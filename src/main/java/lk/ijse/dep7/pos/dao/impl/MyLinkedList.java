package lk.ijse.dep7.pos.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Generic Type
public class MyLinkedList<E> {

    public void add(E value){

    }
}

//public class MyLinkedList {
//
//    public void add(Object value){
//
//    }
//}

class NonGeneric{
    // Generic Method
    <T> void doSomething(T value){

    }

//    void doSomething(Object value){
//
//    }

}

class Test{
    {
        new NonGeneric().doSomething("IJSE");

        List<String> abc = new ArrayList<>();

        List<List<String>> isThis = new ArrayList<>();

        // RawType
        MyLinkedList nonGenericList = new MyLinkedList();
        // Parameterized Type
        MyLinkedList<String> list = new MyLinkedList<>();

        // Type Erasure

        List myArrayList = new ArrayList();
        List myStringArrayList = new ArrayList();
    }
}
