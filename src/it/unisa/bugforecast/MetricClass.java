package it.unisa.bugforecast;

public class MetricClass {

	public MetricClass() {
		// TODO Auto-generated constructor stub
		bug = 0; 
	}
	

	public MetricClass(String name, int one, int two, int three, int four, int five, int six, int seven, int eight,
			int bug) {
		super();
		this.name = name;
		this.one = one;
		this.two = two;
		this.three = three;
		this.four = four;
		this.five = five;
		this.six = six;
		this.seven = seven;
		this.eight = eight;
		this.bug = 0;
	}
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getOne() {
		return one;
	}


	public void setOne(int one) {
		this.one = one;
	}


	public int getTwo() {
		return two;
	}


	public void setTwo(int two) {
		this.two = two;
	}


	public int getThree() {
		return three;
	}


	public void setThree(int three) {
		this.three = three;
	}


	public int getFour() {
		return four;
	}


	public void setFour(int four) {
		this.four = four;
	}


	public int getFive() {
		return five;
	}


	public void setFive(int five) {
		this.five = five;
	}


	public int getSix() {
		return six;
	}


	public void setSix(int six) {
		this.six = six;
	}


	public int getSeven() {
		return seven;
	}


	public void setSeven(int seven) {
		this.seven = seven;
	}


	public int getEight() {
		return eight;
	}


	public void setEight(int eight) {
		this.eight = eight;
	}


	public int getBug() {
		return bug;
	}


	public void setBug(int bug) {
		this.bug = bug;
	}

	private String name;
	private int one;
	private int two;
	private int three;
	private int four;
	private int five;
	private int six;
	private int seven;
	private int eight;
	private int bug;
}
