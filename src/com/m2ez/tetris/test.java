package com.m2ez.tetris;

class A{ 
    public int getNumber(int a){ 
       return a+1; 
    } 
} 
class test extends A{ 
    public int getNumber(int a, char c){ 
       return a+2; 
   } 
    public static void main(String[] args){ 
       test b=new test(); 
       System.out.println(b.getNumber(0)); 
    } 
 } 