package pl.edu.pjwstk.s8267.nai.lab4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class LearnFlowers {
	public LearnFlowers(String filename) throws IOException,
			BadInputCountException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line;
		// Wczytuję dane testowe
		HashMap<String, ArrayList<double[]>> tests = new HashMap<String, ArrayList<double[]>>();
		int inputs = 0;
		while ((line = reader.readLine()) != null) {
			if (line.equals(""))
				continue;
			String data[] = line.split(",");
			double[] input = new double[data.length];
			for (int i = 0; i < data.length - 1; i++)
				input[i] = Double.parseDouble(data[i]);
			input[input.length - 1] = 1;
			if (!tests.containsKey(data[data.length - 1])) {
				tests.put(data[data.length - 1], new ArrayList<double[]>());
			}
			tests.get(data[data.length - 1]).add(input);
			inputs = input.length;
		}
		// Tworzę neurony
		Neuron[] neuron = new Neuron[tests.size()];
		for (int i = 0; i < tests.size(); i++) {
			neuron[i] = new Neuron(inputs);
		}
		// Uczę
		double successRate = 0;
		int runs = 0;
		String[] keys = tests.keySet().toArray(new String[] {});
		while (successRate < 0.8) {
			runs++;
			int checks = 0;
			int success = 0;
			for (int i = 0; i < keys.length; i++) {
				for (int j = 0; j < keys.length; j++) {
					for (double[] input : tests.get(keys[j])) {
						neuron[i].learn(input, i == j);
					}
				}
			}
			for (int i = 0; i < keys.length; i++) {
				for (int j = 0; j < keys.length; j++) {
					for (double[] input : tests.get(keys[j])) {
						checks++;
						if (neuron[i].check(input) == (i == j)) {
							success++;
						}
					}
				}
			}
			successRate = ((double) success) / ((double) checks);
		}

		System.out.println("Got " + successRate + " in " + runs + " runs!");
		for (int i = 0; i < keys.length; i++) {
			System.out.println(keys[i] + " " + neuron[i]);
		}

		// Wersja z kodowaniem binarnym

		// Uczę
		successRate = 0;
		runs = 0;
		keys = tests.keySet().toArray(new String[] {});
		// Tworzę neurony
		neuron = new Neuron[binlog(keys.length)+1];
		for (int i = 0; i < neuron.length; i++) {
			neuron[i] = new Neuron(inputs);
		}
		boolean[] expectedCode = new boolean[neuron.length];
		while (successRate < 0.8) {
			runs++;
			int checks = 0;
			int success = 0;
			for (int i = 0; i < keys.length; i++) {
				int c = i;
				int p = 1;
				while(c > 0) {
					expectedCode[expectedCode.length-(p++)] = (c%2 == 1);
					c = c/2;
				}
				System.out.println(Arrays.toString(expectedCode));
				for (int j = 0; j < keys.length; j++) {
					for (double[] input : tests.get(keys[j])) {
						neuron[i].learn(input, i == j);
					}
				}
			}
			/*for (int i = 0; i < keys.length; i++) {
				for (int j = 0; j < keys.length; j++) {
					for (double[] input : tests.get(keys[j])) {
						checks++;
						if (neuron[i].check(input) == (i == j)) {
							success++;
						}
					}
				}
			}*/
			successRate = ((double) success) / ((double) checks);
			System.out.println("Got " + successRate + " in " + runs + " run");
		}

		/*System.out.println("Got " + successRate + " in " + runs + " runs!");
		for (int i = 0; i < keys.length; i++) {
			System.out.println(keys[i] + " " + neuron[i]);
		}*/

	}

	public static int binlog( int bits ) // returns 0 for bits=0
	{
	    int log = 0;
	    if( ( bits & 0xffff0000 ) != 0 ) { bits >>>= 16; log = 16; }
	    if( bits >= 256 ) { bits >>>= 8; log += 8; }
	    if( bits >= 16  ) { bits >>>= 4; log += 4; }
	    if( bits >= 4   ) { bits >>>= 2; log += 2; }
	    return log + ( bits >>> 1 );
	}
	
	public static void main(String[] args) throws IOException,
			BadInputCountException {
		new LearnFlowers("iris.data");
	}
}
