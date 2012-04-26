package pl.edu.pjwstk.s8267.nai.lab8;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class BAM extends JComponent{
	private int[] m;
	private int w;

	public BAM(int[] m, int width) {
		this.m = m;
		this.w = width;
		setPreferredSize(new Dimension(10*width, 10*m.length/width));
	}

	public static int[] loadFile(String fileName) throws IOException {
		File f = new File(fileName);
		int bytes = (int) f.length();
		BufferedReader br = new BufferedReader(new FileReader(f));
		int[] ret = null;//new int[(int) (f.length()-1)];
		String line;
		int i = 0;
		while((line = br.readLine())!=null) {
			if(ret == null) {
				int lines = (bytes+1)/(line.length()+1);
				ret = new int[lines*line.length()];
			}
			for(int j = 0; j < line.length(); j++)
				ret[i++] = line.charAt(j) == '0'?-1:1;
		}
		br.close();
		return ret;
	}
	
	public static int[][] multiply(int[] a, int[] b) {
		int[][] ret = new int[a.length][];
		for(int i = 0; i < a.length; i++) {
			ret[i] = new int[b.length];
			for(int j = 0; j < b.length; j++) {
				ret[i][j] = a[i]*b[j];
			}
		}
		return ret;
	}
	
	public static int[] multiply(int[] a, int[][] m) {
		int[] ret = new int[m[0].length];
		Arrays.fill(ret, 0);
		for(int i = 0; i < m.length; i++) {
			for(int j = 0; j < m[i].length; j++) {
				//ret[j] = (a[i]*m[i][j])<0?-1:1;
				ret[j] += a[i]*m[i][j];
			}
		}
		return ret;
	}
	
	public static int[] treshold(int[] a) {
		int[] ret = new int[a.length];
		for(int i = 0; i < a.length; i++) 
			ret[i] = a[i]<0?-1:1;
		return ret;
	}
	
	public static int[][] sum(int[][]... a) {
		int[][] ret = new int[a[0].length][];
		for(int i = 0; i < a[0].length; i++) {
			ret[i] = new int[a[0][0].length];
			for(int j = 0; j < a[0][0].length; j++) {
				ret[i][j] = 0;
				for(int k = 0; k < a.length; k++) {
					ret[i][j] += a[k][i][j];
				}
			}
		}
		return ret;
	}
	
	public static void main(String[] args) throws IOException {
		int[][] M = sum(multiply(loadFile("t.txt"), loadFile("tank.txt")),
						multiply(loadFile("c.txt"), loadFile("copter.txt")),
						multiply(loadFile("p.txt"), loadFile("plane.txt")));
		//draw(treshold(multiply(loadFile("t.txt"), M)), 35);
		//draw(treshold(multiply(loadFile("c.txt"), M)), 35);
		draw(treshold(multiply(loadFile("c1.txt"), M)), 35);
	}

	private static void draw(int[] m, int width) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new BAM(m, width));
		f.pack();
		f.setVisible(true);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		for(int i = 0; i < m.length; i++) {
			g2d.setColor(m[i]==1?Color.GREEN:Color.WHITE);
			g2d.fillRect(10*(i%w), 10*(i/w), 10, 10);
		}
		super.paint(g);
	}
}
