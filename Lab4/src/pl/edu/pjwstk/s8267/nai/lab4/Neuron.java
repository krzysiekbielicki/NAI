package pl.edu.pjwstk.s8267.nai.lab4;

import java.util.Arrays;

public class Neuron {
	private int mInputs;
	private double[] mWeight;

	public Neuron(int inputs) {
		mInputs = inputs;
		mWeight = new double[mInputs];
		Arrays.fill(mWeight, 1);
	}
	
	public void setWeights(double[] weight) throws BadWeightCountException {
		if(weight.length != mInputs)
			throw new BadWeightCountException();
		for(int i = 0; i < mInputs; i++)
			mWeight[i] = weight[i];
	}
	
	public boolean check(double[] input) throws BadInputCountException {
		if(input.length != mInputs)
			throw new BadInputCountException();
		double net = 0;
		for(int i = 0; i < mInputs; i++)
			net += mWeight[i]*input[i];
		return net > 0;
	}
	
	public boolean learn(double[] input, boolean expectedResult) throws BadInputCountException {
		int a = ( expectedResult?1:0 ) - ( check(input)?1:0 );
		if(a == 0)
			return true;
		for(int i = 0; i < mInputs; i++)
			if(i < mInputs-1)
				mWeight[i]+=a*input[i];
			else
				mWeight[i] -= a;
		return false;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(mWeight);
	}
}
