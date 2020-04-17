package com.hand.hqm.hqm_control_chart_value.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "HQM_CONTROL_CHART_VALUE")
public class ControlChartValue extends BaseDTO {

     public static final String FIELD_N = "n";
     public static final String FIELD_A = "a";
     public static final String FIELD_A1 = "a1";
     public static final String FIELD_A2 = "a2";
     public static final String FIELD_A3 = "a3";
     public static final String FIELD_A4 = "a4";
     public static final String FIELD_B3 = "b3";
     public static final String FIELD_B4 = "b4";
     public static final String FIELD_B5 = "b5";
     public static final String FIELD_B6 = "b6";
     public static final String FIELD_D1 = "d1";
     public static final String FIELD_D2 = "d2";
     public static final String FIELD_D3 = "d3";
     public static final String FIELD_D4 = "d4";
     public static final String FIELD_C4 = "c4";
     public static final String FIELD_D = "d";


     @Id
     @GeneratedValue
     private Float n;

     private Float a;

     private Float a1;

     private Float a2;

     private Float a3;

     private Float a4;

     private Float b3;

     private Float b4;

     private Float b5;

     private Float b6;

     private Float d1;

     private Float d2;

     private Float d3;

     private Float d4;

     private Float c4;

     private Float d;


     public void setN(Float n){
         this.n = n;
     }

     public Float getN(){
         return n;
     }

     public void setA(Float a){
         this.a = a;
     }

     public Float getA(){
         return a;
     }

     public void setA1(Float a1){
         this.a1 = a1;
     }

     public Float getA1(){
         return a1;
     }

     public void setA2(Float a2){
         this.a2 = a2;
     }

     public Float getA2(){
         return a2;
     }

     public void setA3(Float a3){
         this.a3 = a3;
     }

     public Float getA3(){
         return a3;
     }

     public void setA4(Float a4){
         this.a4 = a4;
     }

     public Float getA4(){
         return a4;
     }

     public void setB3(Float b3){
         this.b3 = b3;
     }

     public Float getB3(){
         return b3;
     }

     public void setB4(Float b4){
         this.b4 = b4;
     }

     public Float getB4(){
         return b4;
     }

     public void setB5(Float b5){
         this.b5 = b5;
     }

     public Float getB5(){
         return b5;
     }

     public void setB6(Float b6){
         this.b6 = b6;
     }

     public Float getB6(){
         return b6;
     }

     public void setD1(Float d1){
         this.d1 = d1;
     }

     public Float getD1(){
         return d1;
     }

     public void setD2(Float d2){
         this.d2 = d2;
     }

     public Float getD2(){
         return d2;
     }

     public void setD3(Float d3){
         this.d3 = d3;
     }

     public Float getD3(){
         return d3;
     }

     public void setD4(Float d4){
         this.d4 = d4;
     }

     public Float getD4(){
         return d4;
     }

     public void setC4(Float c4){
         this.c4 = c4;
     }

     public Float getC4(){
         return c4;
     }

     public void setD(Float d){
         this.d = d;
     }

     public Float getD(){
         return d;
     }

     }
