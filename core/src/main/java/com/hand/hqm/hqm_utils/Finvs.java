package com.hand.hqm.hqm_utils;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.velocity.exception.MathException;

public class Finvs {
			//FINV:
			public static double Finv(double probability, int degreesOfFreedom1, int degreesOfFreedom2)
			{
			//Excel : FINV(0.05 ,5 ,5) = 5.050329058

			//df1 => 分子自由度
			//df2 => 分母自由度
			FDistribution fDistribution = new FDistribution(degreesOfFreedom2, degreesOfFreedom1);

			try
			{
			return Math.pow(fDistribution.inverseCumulativeProbability(probability), -1);
			}
			catch (MathException e)
			{
			}

			return -1;
			}
		 public  static void main(String[] args){
		      System.out.println(Finv(0.05d,5,5));
		 }
}
